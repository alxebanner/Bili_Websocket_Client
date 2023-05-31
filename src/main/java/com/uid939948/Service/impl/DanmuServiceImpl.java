package com.uid939948.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.Controller.DanmuWebsocket;
import com.uid939948.DO.MedalInfo;
import com.uid939948.DO.danmu.ANCHOR_LOT_START.Anchor_lot;
import com.uid939948.DO.danmu.ANCHOR_LOT_START.Anchor_lot_award;
import com.uid939948.DO.danmu.ANCHOR_LOT_START.Award_users;
import com.uid939948.DO.danmu.DanMu_MSG.DanMu_MSG_Info;
import com.uid939948.DO.danmu.DanmuVO.DanmuDto;
import com.uid939948.DO.danmu.GuardBuy_MSG.GuardBuy;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.DO.danmu.Send_Gift.SimpleGift;
import com.uid939948.DO.danmu.SuperChat_MSG.SuperChatMessage;
import com.uid939948.DO.danmu.Toast_MSG.Toast;
import com.uid939948.DO.danmu.interact.InteractWord;
import com.uid939948.DO.danmu.like.LikeInfo;
import com.uid939948.DO.dianGe.MusicInfo;
import com.uid939948.DO.temp.FacePicture;
import com.uid939948.Enuns.GuardLevelEnum;
import com.uid939948.Enuns.InteractWordEnum;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.Service.DanmuService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DanmuServiceImpl implements DanmuService {
    @Resource
    DanmuWebsocket danmuWebsocket;

    @Override
    public void danmuFunction(String message) {
        // 1、转换实体
        // 生成 消息实体，并且显示是否是表情
        DanMu_MSG_Info danMuMsgInfo = format_danMuMsgInfo(message);

        // 2、 定制内容
        // 2、1 系统表情
        format_SystemEmoji(danMuMsgInfo);
        // 2、2 自定义表情

        // 2、3 天选屏蔽 抽奖弹幕
        if (MainConf.centerSetConf.getIsAnchorLot()) {
            // 如果存在天选，和天选过期时间， 这里在天选推送结束的地方也要过一遍，防止意外
            if (ObjectUtils.isNotEmpty(MainConf.anchorLot) && ObjectUtils.isNotEmpty(MainConf.anchorLot_endTime)) {
                // 存在天选时， 如果时间未到屏蔽，
                //               时间已到，则删除本地的天选 和天选过期时间
                long current_now_tIme = System.currentTimeMillis();
                String str_current_time = String.valueOf(current_now_tIme).substring(0, 10);
                long now_Ten_Time = Long.parseLong(str_current_time);

                // 显示时间 大于 天选过期时间， 则删除天选 和天选过期时间
                if (now_Ten_Time > MainConf.anchorLot.getCurrent_time()) {
                    MainConf.anchorLot = null;
                    MainConf.anchorLot_endTime = 0L;
                }
            }
        }

        // 2、4 红包屏蔽 抽奖弹幕
        if (MainConf.centerSetConf.getIsRedPocket()) {
            String red_pocket_message = "老板大气！点点红包抽礼物！";
            if (red_pocket_message.equals(danMuMsgInfo.getMessage())) {
                return;
            }
        }

        // 2.5 点歌列表
        if (false) {
            String danmuMessage = danMuMsgInfo.getMessage();
            if (danmuMessage.startsWith("点歌 ")) {
                String musicName = danmuMessage.replace("点歌 ", "");
                // 如果是点歌， 再判断是否有歌曲重复
                //
                if (ObjectUtils.isEmpty(MainConf.musicInfoList)) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setMusicName(musicName);
                    musicInfo.setUid(danMuMsgInfo.getUid());
                    musicInfo.setName(danMuMsgInfo.getUname());
                    musicInfo.setNum(1);
                    // todo 搜索网易云 歌曲黑名单判断
                    MainConf.musicInfoList.add(musicInfo);
                    log.info("点歌成功 + danMuMsgInfo.getMessage() ");
                } else if (MainConf.musicInfoList.stream().map(MusicInfo::getMusicName).anyMatch(a -> a.equals(musicName))) {
                    // 存在重复歌曲，不上榜单
                    log.info(danMuMsgInfo.getUname() + "  存在重复名称，不上点歌榜单");
                } else {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setMusicName(musicName);
                    musicInfo.setUid(danMuMsgInfo.getUid());
                    musicInfo.setName(danMuMsgInfo.getUname());
                    musicInfo.setNum(MainConf.musicInfoList.size() + 1);
                    // todo 搜索网易云 歌曲黑名单判断。，、
                    MainConf.musicInfoList.add(musicInfo);
                    log.info("点歌成功 + danMuMsgInfo.getMessage() ");
                }
            }
        }


        // 2.6 通过缓存获取头像地址 没有就自己获取
        danMuMsgInfo.setFaceUrl(getFaceUrlByCache(danMuMsgInfo.getUid()));


        // 提督颜色 #E17AFF
        // 舰长颜色 #00D1F1


        // 3、内容打印 输出弹幕给前端
        print_danmu(danMuMsgInfo);
    }

    /**
     * 输出弹幕到前端 并且打印日志
     *
     * @param danMuMsgInfo 弹幕实体类
     */
    private void print_danmu(DanMu_MSG_Info danMuMsgInfo) {
        String str_xun_zhang = addFull(danMuMsgInfo.getGuard_level(), danMuMsgInfo.getMedal_name(), String.valueOf(danMuMsgInfo.getMedal_level()), danMuMsgInfo.getUname());
        String str_danmu = danMuMsgInfo.getMessage();
        String str_fangguan = danMuMsgInfo.getIsManager() ? "[房管]" : "";
        if (danMuMsgInfo.getIsEmoticon()) {
            log.info("表情 " + str_fangguan + str_xun_zhang + " : " + str_danmu);
            danmuWebsocket_sendMessage(danMuMsgInfo, "emoticon");
        } else {
            log.info("弹幕 " + str_fangguan + str_xun_zhang + " : " + str_danmu);
            danmuWebsocket_sendMessage(danMuMsgInfo, "danmu");
        }
    }

    /**
     * 格式化系统弹幕
     *
     * @param danMuMsgInfo 弹幕实体
     */
    private void format_SystemEmoji(DanMu_MSG_Info danMuMsgInfo) {
        if (MainConf.centerSetConf.getIsSystemEmoji()) {
            // todo 系统表情可能会变更 这里不需要写死，可能会变
            List<String> systemEmojiList = Arrays.asList("[dog]", "[花]", "[妙]", "[哇]", "[爱]", "[手机]", "[撇嘴]", "[委屈]", "[抓狂]", "[比心]", "[赞]", "[滑稽]", "[吃瓜]", "[笑哭]", "[捂脸]", "[喝彩]", "[偷笑]", "[大笑]", "[惊喜]", "[傲娇]", "[疼]", "[吓]", "[阴险]", "[惊讶]", "[生病]", "[嘘]", "[奸笑]", "[囧]", "[捂脸2]", "[出窍]", "[吐了啊]", "[鼻子]", "[调皮]", "[酸]", "[冷]", "[OK]", "[微笑]", "[藏狐]", "[龇牙]", "[防护]", "[笑]", "[一般]", "[嫌弃]", "[无语]", "[哈欠]", "[可怜]", "[歪嘴笑]", "[亲亲]", "[问号]", "[波吉]", "[OH]", "[再见]", "[白眼]", "[鼓掌]", "[大哭]", "[呆]", "[流汗]", "[生气]", "[加油]", "[害羞]", "[虎年]", "[doge2]", "[金钱豹]", "[瓜子]", "[墨镜]", "[难过]", "[抱抱]", "[跪了]", "[摊手]", "[热]", "[三星堆]", "[鼠]", "[汤圆]", "[泼水]", "[鬼魂]", "[不行]", "[响指]", "[牛]", "[保佑]", "[抱拳]", "[给力]", "[耶]");
            List<String> emojiList = new ArrayList<>();
            String temp_danmu = danMuMsgInfo.getMessage();
            for (String emojiName : systemEmojiList) {
                if (temp_danmu.contains(emojiName)) {
                    danMuMsgInfo.setIsHaveSystemEmoji(true);
                    emojiList.add(emojiName);
                    // 后台不替换  前端替换
                    // String div = "";
                    // str_danmu.replace(systemEmojiList.get(i), systemEmojiList.get(i));
                }
            }
            danMuMsgInfo.setEmojiList(emojiList);
        }
    }

    @Override
    public void LikeFunction(String message) {
        String like_info_data = JSONObject.parseObject(message).getString("data");
        LikeInfo likeInfo = JSONObject.parseObject(like_info_data, LikeInfo.class);

        String temp_stt = addFull(likeInfo.getFans_medal().getGuard_level(), likeInfo.getFans_medal().getMedal_name(), String.valueOf(likeInfo.getFans_medal().getMedal_level()),
                likeInfo.getUname());
        log.info(temp_stt + "点赞了");

        if (MainConf.centerSetConf.getIsLikeMessage()) {
            danmuWebsocket_sendMessage(likeInfo, "likeInfo");
        }
    }

    @Override
    public void LikeNumFunction(String message) {
        // {"cmd":"LIKE_INFO_V3_UPDATE","data":{"click_count":13}}
        Long click_count = JSONObject.parseObject(message).getJSONObject("data").getLong("click_count");
        MainConf.ROOM_CLICK = click_count;
        log.debug("房间点赞数量为" + click_count);
        if (MainConf.centerSetConf.getIsLikeNumMessage()) {
            danmuWebsocket_sendMessage(click_count, "click_count");
        }
    }

    @Override
    public void watchNumFunction(String message) {
//        {"cmd":"WATCHED_CHANGE","data":{"num":154,"text_small":"154","text_large":"154人看过"}}
        Long watchNum = JSONObject.parseObject(message).getJSONObject("data").getLong("text_small");
        MainConf.ROOM_WATCHER = watchNum;
        log.debug("房间曾观看数量为" + watchNum);
        // todo
        if (false) {
            danmuWebsocket_sendMessage(watchNum, "watch_count");
        }

    }

    @Override
    public void giftFunction(String message) {

        // 1、生成实体类
        SimpleGift simpleGift = format_SimpleGift(message);

        // 2、礼物过滤（比如屏蔽特定礼物， 辣条 银瓜子礼物）

        // 2、1 礼物白名单，特殊礼物直接显示。比如银瓜子的粉丝团灯牌 金瓜子的粉丝团灯牌
        // todo 银瓜子 粉丝牌灯牌 白名单

        // 2、2 礼物黑名单，比如屏蔽银瓜子的 辣条
        if (MainConf.centerSetConf.getIsGift()) {
            if ("silver".equals(simpleGift.getCoin_type())) {
                if (!MainConf.centerSetConf.getIsSilverGift()) {
                    log.info("银瓜礼物显示已关闭");
                    return;
                }
                log.info("收到 银瓜子礼物 " + simpleGift.getUname() + "赠送了 " + simpleGift.getGiftName());
            } else if ("gold".equals(simpleGift.getCoin_type())) {
                log.info("收到 金瓜子礼物 " + simpleGift.getUname() + "赠送了" + simpleGift.getGiftName() + " 价值为 " + simpleGift.getPrice());

                // 金瓜子礼物 过滤
                // 金瓜子， 低于x元礼物不显示
                float f1 = strToFloat(MainConf.centerSetConf.getMinGoldPrice());
                float giftFloat = ((float) simpleGift.getPrice()) / 1000;
                // log.info("最低金额为 " + f1 + "元");
                // log.info("投喂金额金额为 " + giftFloat + "元");
                if (giftFloat < f1) {
                    log.debug("不需要显示金瓜子礼物");
                    return;
                } else {
                    log.debug("需要显示金瓜子礼物");
                }
            } else {
                log.info("收到 未知礼物 " + simpleGift.getUname() + "赠送了" + simpleGift.getGiftName() + " 价值为 " + simpleGift.getPrice());
            }
        } else {
            log.info("礼物显示已关闭");
            return;
        }

        // 2、2 礼物堆叠， 后端实现 比如1秒内重复收到礼物会推迟推送 给 前端，叠加后推送
        //               前端实现 比如1秒内重复收到礼物会由前端实现d叠加

        // 3、输出到前端 打印日志
        danmuWebsocket_sendMessage(simpleGift, "gift");
    }

    /**
     * 把字串转化为Float型数据,若转化失败，则返回0
     *
     * @param str 字符串
     * @return 转换后的float
     */
    private float strToFloat(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(str + "转换成float类型失败，请检查数据来源");
        }
        return 0;
    }


    @Override
    public void guardBuyFunction(String message) {
        JSONObject jsonObject_data = JSONObject.parseObject(message);
        JSONObject array1 = jsonObject_data.getJSONObject("data");
        GuardBuy guardBuy = JSONObject.toJavaObject(array1, GuardBuy.class);
        log.info(guardBuy.getUsername() + "在本房间开通了" + guardBuy.getNum() + "个月 " + GuardLevelEnum.getCountryValue(guardBuy.getGuard_level()));

        // 这里不推送，用USER_TOAST_MSG 来推送开通舰长，因为USER_TOAST_MSG里面信息比较多。
        // {"cmd":"USER_TOAST_MSG","data":{"anchor_show":true,"color":"#00D1F1","dmscore":90,"effect_id":397,"end_time":1682179214,"face_effect_id":44,"gift_id":10003,"guard_level":3,"is_show":0,"num":1,"op_type":3,"payflow_id":"2304230000001102550590844","price":138000,"role_name":"舰长","room_effect_id":590,"start_time":1682179214,"svga_block":0,"target_guard_count":132,"toast_msg":"\u003c%水禾禾禾%\u003e 续费了舰长，今天是TA陪伴主播的第1058天","uid":175635059,"unit":"月","user_show":true,"username":"水禾禾禾"}}

    }

    @Override
    public void ToastFunction(String message) {
        JSONObject jsonObject_data = JSONObject.parseObject(message);
        JSONObject jsonObject = jsonObject_data.getJSONObject("data");
        Toast toast = JSONObject.toJavaObject(jsonObject, Toast.class);

        danmuWebsocket_sendMessage(toast, "Toast");

        log.info("上船消息推送" + toast); // Toast(anchor_show=true, color=#00D1F1, dmscore=90, effect_id=397, end_time=1685328094, face_effect_id=44, gift_id=10003, guard_level=3, is_show=0, num=1, op_type=1, payflow_id=2305291041138892124930450, price=138000, role_name=舰长, room_effect_id=590, start_time=1685328094, svga_block=0, target_guard_count=17, toast_msg=<%毕业留在成都%> 在主播冼知了的直播间开通了舰长，今天是TA陪伴主播的第1天, uid=385372493, unit=月, user_show=true, username=毕业留在成都)
        // 上船消息推送Toast(anchor_show=true, color=#E17AFF, dmscore=96, effect_id=398, end_time=1684712543, face_effect_id=43, gift_id=10002, guard_level=2, is_show=0, num=1, op_type=1, payflow_id=2305220742069152162161257, price=1998000, role_name=提督, room_effect_id=591, start_time=1684712543, svga_block=0, target_guard_count=58, toast_msg=<%Eden-A%> 开通了提督，今天是TA陪伴主播的第1天, uid=15486216, unit=月, user_show=true, username=Eden-A)
    }

    @Override
    public void superChatFunction(String message) {
        JSONObject jsonObject_data = JSONObject.parseObject(message);
        JSONObject jsonObject = jsonObject_data.getJSONObject("data");

        SuperChatMessage superChatMessage = JSONObject.toJavaObject(jsonObject, SuperChatMessage.class);
//        System.out.println("sc 内容为 :" + superChatMessage.getMessage());
//        System.out.println("sc 发送时间为 :" + timestamp2Date(String.valueOf(superChatMessage.getStart_time()),"yyyy-MM-dd HH:mm:ss" )   );
//        System.out.println("sc 二进制时间为 :" + superChatMessage.getEnd_time());
//        System.out.println("sc 结束时间为 :" +  timestamp2Date(String.valueOf(superChatMessage.getEnd_time()),"yyyy-MM-dd HH:mm:ss" )   );
//        System.out.println("sc 二进制时间为 :" + superChatMessage.getEnd_time());
//
//        System.out.println("sc 颜色为 :" + superChatMessage.getMessage_font_color());
//        System.out.println("sc 的价格为 :" + superChatMessage.getPrice());
//        System.out.println("sc 的持续时间为 :" + superChatMessage.getTime());
//
//        System.out.println("sc 的发送人为 :" + superChatMessage.getUser_info().getUname());
//        System.out.println("sc 的发送人uid为 :" + superChatMessage.getUid());
//        System.out.println("sc 的发送人头像地址 :" + superChatMessage.getUser_info().getFace());

        log.info("收到 " + superChatMessage.getUser_info().getUname() + "的 " + superChatMessage.getPrice() + "元 +superChatMessage.getTime()" + "秒 醒目留言" + "并且说 " + superChatMessage.getMessage());

        // todo 暂时伪装成的弹幕发送出去
        DanMu_MSG_Info danMuMsgInfo = new DanMu_MSG_Info();
        danMuMsgInfo.setMessage("醒目留言: " + superChatMessage.getMessage());
        danMuMsgInfo.setUname(superChatMessage.getUser_info().getUname());
        danMuMsgInfo.setFaceUrl(superChatMessage.getUser_info().getFace());
        danMuMsgInfo.setIsHaveSystemEmoji(false);

        danmuWebsocket_sendMessage(danMuMsgInfo, "emoticon");
    }

    @Override
    public void specialGiftFunction(String message) {
        // todo 需要抓节奏风暴
    }

    @Override
    public void interactFunction(String message) {
        InteractWord interactWord = JSONObject.parseObject(JSONObject.parseObject(message).getString("data"), InteractWord.class);
//        long l1 = interactWord.getTimestamp();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d1;
//        String timestamp = String.valueOf(l1);
//        try {
//            d1 = simpleDateFormat.parse(simpleDateFormat.format(Long.parseLong(timestamp) * 1000));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        String d2 = simpleDateFormat.format(d1);
        String temp_str = addFull(interactWord.getFans_medal().getGuard_level(),
                interactWord.getFans_medal().getMedal_name(),
                String.valueOf(interactWord.getFans_medal().getMedal_level()),
                interactWord.getUname()
        );


        switch (interactWord.getMsg_type()) {
            case 1:
                if (MainConf.centerSetConf.getIsEnterMessage()) {
                    // log.info("推送 进入 房间信息");
                    log.info(temp_str + InteractWordEnum.getCountryValue(interactWord.getMsg_type()) + "了 直播间");
                } else {
                    return;
                }
                break;
            case 2:
                if (MainConf.centerSetConf.getIsAttentionMessage()) {
                    // log.info("推送 关注 房间信息");
                    log.info(temp_str + InteractWordEnum.getCountryValue(interactWord.getMsg_type()) + "了 直播间");
                } else {
                    return;
                }
                break;
            case 3:
                if (MainConf.centerSetConf.getIsShareMessage()) {
                    //  log.info("推送 分享 房间信息");
                    log.info(temp_str + InteractWordEnum.getCountryValue(interactWord.getMsg_type()) + "了 直播间");
                } else {
                    return;
                }
                break;
            default:
                log.warn("未知推送interact 消息， 请联系管理员 " + interactWord);
                return;
        }
        // interactWord推送前端
        danmuWebsocket_sendMessage(interactWord, "interactWord");
    }

    @Override
    public void anchor_lot_start_Function(String message) {
        JSONObject jsonObject_data = JSONObject.parseObject(message);
        JSONObject array1 = jsonObject_data.getJSONObject("data");
        Anchor_lot anchorLot = JSONObject.toJavaObject(array1, Anchor_lot.class);

        // 保存天选和 天选过期时间到 本地
        MainConf.anchorLot = anchorLot;
        MainConf.anchorLot_endTime = anchorLot.getCurrent_time() + anchorLot.getMax_time();

        log.info("发现天选，发送弹幕" + anchorLot);
        System.out.println(timestamp2Date(String.valueOf(anchorLot.getCurrent_time()), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(timestamp2Date(String.valueOf(anchorLot.getCurrent_time() + anchorLot.getMax_time()), "yyyy-MM-dd HH:mm:ss"));
        // todo 天选中奖推送 前端
    }

    @Override
    public void anchorLotAward_Function(String message) {
        // ANCHOR_LOT_END 和ANCHOR_LOT_AWARD  为天选结束 和天选中奖人消息 一起出现，可以一起处理。结束就有抽奖人了
        JSONObject jsonObject_data1 = JSONObject.parseObject(message);
        JSONObject array2 = jsonObject_data1.getJSONObject("data");
        Anchor_lot_award anchorLotAward = JSONObject.toJavaObject(array2, Anchor_lot_award.class);

        // 天选可以有多个人中
        List<String> l1 = anchorLotAward.getAward_users().stream().map(Award_users::getUname).collect(Collectors.toList());
        log.info("恭喜用户 " + l1 + "  获得天选礼物" + anchorLotAward.getAward_price_text() + " " + anchorLotAward.getAward_name());
        // todo 天选中奖推送 前端
        if (MainConf.centerSetConf.getIsAnchorLot()) {
            // 如果存在天选，和天选过期时间， 这里在天选推送结束的地方也要过一遍，防止意外
            if (ObjectUtils.isNotEmpty(MainConf.anchorLot) && ObjectUtils.isNotEmpty(MainConf.anchorLot_endTime)) {
                // 存在天选时， 如果时间未到屏蔽，
                //               时间已到，则删除本地的天选 和天选过期时间
                long current_now_tIme = System.currentTimeMillis();
                // 阿b的时间戳只有10位，需要适配
                String str_current_time = String.valueOf(current_now_tIme).substring(0, 10);
                long now_Ten_Time = Long.parseLong(str_current_time);
                // 显示时间 大于 天选过期时间， 则删除天选 和天选过期时间
                if (now_Ten_Time > MainConf.anchorLot.getCurrent_time()) {
                    MainConf.anchorLot = null;
                    MainConf.anchorLot_endTime = 0L;
                }
            }
        }
    }

    @Override
    public void danmu_aggregation_Function(String message) {
        JSONObject jsonObject_data3 = JSONObject.parseObject(message);
        JSONObject array3 = jsonObject_data3.getJSONObject("data");
        if (MainConf.centerSetConf.getIsAnchorLot()) {
            if (ObjectUtils.isEmpty(MainConf.anchorLot) || ObjectUtils.isEmpty(MainConf.anchorLot_endTime)) {
                // 天选为空，则说明   因为未获取天选的开始的推送， 所以通过 堆叠弹幕抓取天选弹幕
                Anchor_lot anchorLot = new Anchor_lot();
                anchorLot.setDanmu(array3.getString("msg"));
                anchorLot.setCurrent_time(array3.getLong("timestamp"));
                MainConf.anchorLot_endTime = 0L;
            }
        }
    }

    /**
     * 格式化礼物， 将重要的内容简化显示
     *
     * @param message 弹幕消息
     * @return 简单的礼物实体
     */
    private SimpleGift format_SimpleGift(String message) {
        JSONObject jsonObject_data = JSONObject.parseObject(message);
        JSONObject jsonObject_gift = jsonObject_data.getJSONObject("data");

        Long uid = jsonObject_gift.getLong("uid");
        int giftId = jsonObject_gift.getInteger("giftId");
        String giftName = jsonObject_gift.getString("giftName");
        int num = jsonObject_gift.getInteger("num");
        String uname = jsonObject_gift.getString("uname");
        int guard_level = jsonObject_gift.getInteger("guard_level");
        int price = jsonObject_gift.getInteger("price");
        Long gift_timestamp = jsonObject_gift.getLong("timestamp");
        String action = jsonObject_gift.getString("action");
        String coin_type = jsonObject_gift.getString("coin_type");
        MedalInfo medalInfo = jsonObject_gift.getObject("medal_info", MedalInfo.class);
        String faceUrl = jsonObject_gift.getString("face");
        SimpleGift simpleGift = new SimpleGift(uid, giftId, giftName, num, uname, guard_level, price, gift_timestamp, action, coin_type, medalInfo, "", faceUrl);
        GiftConfigData giftConfigData = MainConf.giftMap.get(simpleGift.getGiftId());

        if (ObjectUtils.isEmpty(giftConfigData)) {
            log.info("找不到礼物图片");
            // 可能中途新增的礼物 todo 新增查询礼物列表
            log.info("" + simpleGift.getGiftId());
            log.info("" + MainConf.giftMap);
            return simpleGift;
        }

        if (StringUtils.isNotEmpty(giftConfigData.getImg_basic())) {
            simpleGift.setImg_basic(giftConfigData.getImg_basic());
        }
        return simpleGift;
    }

    /**
     * 格式化消息实体
     *
     * @param message 弹幕消息
     * @return 弹幕消息实体
     */
    private DanMu_MSG_Info format_danMuMsgInfo(String message) {
        DanMu_MSG_Info danMuMsgInfo = new DanMu_MSG_Info();
        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONArray array = jsonObject.getJSONArray("info");
        boolean isFansMedal;
        // 1、判断 勋章
        JSONArray j3 = (JSONArray) array.get(3);
        if (j3.size() < 1) {
            // System.out.println("为空 未佩戴勋章");
            isFansMedal = false;
        } else {
            isFansMedal = true;
            // String m4 = j3.get(0).toString();
            // String m5 = j3.get(1).toString();
            // String m6 = j3.get(2).toString();
            // String m7 = j3.get(3).toString();
            // String m8 = j3.get(12).toString();
            // String m9 = j3.get(10).toString();
            // System.out.println("勋章等级 " + m4);
            // System.out.println("勋章名称 " + m5);
            // System.out.println("所属主播 " + m6);
            // System.out.println("所属主播房间号 " + m7);
            // System.out.println("所属主播uid " + m8);
            // System.out.println("勋章级别 " + GuardLevelEnum.getCountryValue(Integer.valueOf(m9)));
        }
        // 2、判断 表情
        String b1 = ((JSONArray) array.get(0)).get(12).toString();
        boolean isEmoticon;
        if (StringUtils.isNotEmpty(b1) && "1".equals(b1)) {
            // 表情处理
            // log.info("是表情");
            isEmoticon = true;
            // log.info("表情地址" + ((JSONObject) ((JSONArray) array1.get(0)).get(13)).get("url"));
        } else {
            isEmoticon = false;
            // 弹幕处理
            //  log.info("是弹幕");
        }

        JSONArray j2 = (JSONArray) array.get(2);
        JSONArray j4 = (JSONArray) array.get(4);
        danMuMsgInfo.setUid(Long.valueOf(j2.get(0).toString()));
        danMuMsgInfo.setUname(j2.get(1).toString());
        danMuMsgInfo.setIsManager("1".equals(j2.get(2).toString()));

        danMuMsgInfo.setDanmuColor(j2.get(7).toString());

        // log.info("弹幕颜色为: " + danMuMsgInfo.getDanmuColor());

        danMuMsgInfo.setMessage(array.get(1).toString());
        danMuMsgInfo.setIsEmoticon(isEmoticon);
        danMuMsgInfo.setIsFansMedal(isFansMedal);
        danMuMsgInfo.setUl_level(Long.parseLong(j4.get(0).toString()));

        // 有勋章时 赋值勋章
        if (isFansMedal) {
            danMuMsgInfo.setMedal_level(Integer.parseInt(j3.get(0).toString()));
            danMuMsgInfo.setMedal_name(j3.get(1).toString());
            danMuMsgInfo.setGuard_level(Integer.parseInt(j3.get(10).toString()));
        }

        // 有表情时 赋值表情
        if (danMuMsgInfo.getIsEmoticon()) {
            danMuMsgInfo.setEmoticon_url(String.valueOf(((JSONObject) ((JSONArray) array.get(0)).get(13)).get("url")));
            // 是否系统表情标识 秒啊 有点东西 点赞 等 前端需要额外显示 （普通表情是正方形， 系统表情为长方形）
            String bulge_display = array.getJSONArray(0).getJSONObject(13).getString("bulge_display");

            // 为1 是用户表情  为0 是系统表情
            danMuMsgInfo.setBulge_display("1".equals(bulge_display));
        }
        return danMuMsgInfo;
    }


    /**
     * 优化显示数据
     *
     * @param level       舰队等级
     * @param medal_name  勋章名称
     * @param Medal_level 勋章等级
     * @param uname       用户名称
     * @return 优化后的数据
     */
    private String addFull(int level, String medal_name, String Medal_level, String uname) {
        // 级别
        String s1 = GuardLevelEnum.getCountryValue(level);
        // 等级
        String s2 = medal_name;
        // 勋章名称
        String s3 = String.valueOf(Medal_level);

        // 改造 增加[]
        String s11 = StringUtils.isEmpty(s1) ? "" : "[" + s1 + "]";
        String s12 = StringUtils.isEmpty(s2) ? "" : "[" + s2 + "]";
        String s13 = "0".equals(s3) ? "" : "[" + s3 + "]";

        return s11 + s12 + s13 + uname + " ";
    }

    /**
     * 通过缓存获取用户头像
     *
     * @param uid 用户uid
     * @return 头像地址
     */
    private String getFaceUrlByCache(Long uid) {
        // 先判断是否有
        String nowFaceUrl;
        if (ObjectUtils.isEmpty(MainConf.facePictureList)) {
            String faceUrl = HttpRoomUtil.httpGetFaceV2(uid);
            // HttpRoomUtil.httpGetFaceUrl_V2(uid);备用接口
            nowFaceUrl = faceUrl;
            FacePicture facePicture = new FacePicture(uid,
                    faceUrl, 1, System.currentTimeMillis());
            MainConf.facePictureList.add(facePicture);
        } else {
            // 如果存在历史头像 取历史头像 遍历+1 否则重新获得
            Map<Long, FacePicture> map = MainConf.facePictureList.stream()
                    .collect(Collectors.toMap(FacePicture::getUid, v -> v, (p1, p2) -> p1));
            if (map.containsKey(uid)) {
                nowFaceUrl = map.get(uid).getFaceUrl();
                // log.info("使用历史头像");

                FacePicture new_facePicture = map.get(uid);
                new_facePicture.setCount(new_facePicture.getCount() + 1);

                // 判断是否使用历史头像
                /*
                FacePicture old_facePicture = map.get(uid);
                if (Collections.replaceAll(MainConf.facePictureList, old_facePicture, new_facePicture)) {
                    log.info("替换成功");
                    Map<Long, FacePicture> map1 = MainConf.facePictureList.stream()
                            .collect(Collectors.toMap(FacePicture::getUid, v -> v, (p1, p2) -> p1));

                    if (map1.containsKey(uid)) {
                        int count = map.get(uid).getCount();
                        log.info("新次数为 " + count);
                    }

                } else {
                    log.info("替换失败");
                }
                */
            } else {
                String faceUrl = HttpRoomUtil.httpGetFaceV2(uid);
                // HttpRoomUtil.httpGetFaceUrl_V2(uid);
                nowFaceUrl = faceUrl;
                FacePicture facePicture = new FacePicture(uid,
                        faceUrl, 1, System.currentTimeMillis());
                MainConf.facePictureList.add(facePicture);
            }
        }
        return nowFaceUrl;
    }

    private String getFaceInThread(String uid) {
        // 先看最近查询时间和 待查询的列表
        long nowTime = System.currentTimeMillis();
        if (ObjectUtils.isEmpty(MainConf.noFaceUidList) || MainConf.noFaceUidList.size() == 0) {

            // todo 获取头像
            MainConf.lastTimeFace = nowTime;
        } else {
            List<FacePicture> list = MainConf.noFaceUidList;
            if (list.stream().allMatch(mo -> (mo.getUid() + "").equals(uid + ""))) {
                // 如果是重复的无头像的，只需要counts+1
                //
            } else {
                FacePicture facePicture = new FacePicture();
                facePicture.setCount(1);
                facePicture.setUid(Long.valueOf(uid));
                facePicture.setTimestamp(nowTime);
                MainConf.noFaceUidList.add(facePicture);
            }
        }
        return "";
    }

    /**
     * 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     * format = "yyyy-MM-dd HH:mm:ss"
     *
     * @param str_num 时间
     * @param format  格式
     * @return 转换后时间
     */
    private static String timestamp2Date(String str_num, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
            log.debug("timestamp2Date" + "将13位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
            log.debug("timestamp2Date" + "将10位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        }
    }

    /**
     * 输出弹幕
     *
     * @param object 弹幕诶人
     * @param type   类型
     */
    private void danmuWebsocket_sendMessage(Object object, String type) {
        try {
            danmuWebsocket.sendMessage(DanmuDto.toJson(type, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
