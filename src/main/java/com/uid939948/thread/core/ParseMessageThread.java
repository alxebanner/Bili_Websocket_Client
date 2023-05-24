package com.uid939948.thread.core;

import com.alibaba.fastjson.JSONObject;

import com.uid939948.Service.DanmuService;
import com.uid939948.Until.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class ParseMessageThread extends Thread {
    private final static Logger log = LogManager.getLogger(ParseMessageThread.class);
    public volatile boolean FLAG = false;

    private CenterSetConf centerSetConf;

    private DanmuService danmuService = SpringUtils.getBean(DanmuService.class);

    @Override
    public void run() {
        // TODO 自动生成的方法存根
        super.run();
        try {
            JSONObject jsonObject = null;
            String message = "";
            String cmd = "";
            //high_level danmu
            StringBuilder stringBuilder = new StringBuilder(200);
            while (!FLAG) {
                if (FLAG) {
                    log.info("数据解析线程手动中止");
                    return;
                }
                if (null != MainConf.resultStrs && !MainConf.resultStrs.isEmpty()
                        && !StringUtils.isEmpty(MainConf.resultStrs.get(0))) {
                    message = MainConf.resultStrs.get(0);
                    try {
                        jsonObject = JSONObject.parseObject(message);
                    } catch (Exception e) {
                        // TODO: handle exception
                        log.info("抛出解析异常:" + e);
                        //					LOGGER.info(message);
                        synchronized (MainConf.parseMessageThread) {
                            try {
                                MainConf.parseMessageThread.wait();
                            } catch (InterruptedException e1) {
                                // TODO 自动生成的 catch 块
                                log.info("处理弹幕包信息线程关闭:" + e1);
                            }
                        }
                    }
                    cmd = jsonObject.getString("cmd");
                    if (StringUtils.isEmpty(cmd)) {
                        synchronized (MainConf.parseMessageThread) {
                            try {
                                MainConf.parseMessageThread.wait();
                            } catch (InterruptedException e1) {
                                // TODO 自动生成的 catch 块
                                log.info("处理弹幕包信息线程关闭:" + e1);
                                //							e.printStackTrace();
                            }
                        }
                    }

//                     log.info("消息类型为" + cmd);
//                     log.info("消息为" + message);

                    // 消息打印黑名单 （已经检查的名单）
                    List<String> writeList = new ArrayList<>();
                    // 弹幕
                    writeList.add("DANMU_MSG");
                    // 礼物
                    writeList.add("SEND_GIFT");
                    // 有人上船 简单推送
                    writeList.add("GUARD_BUY");
                    // 醒目留言
                    writeList.add("SUPER_CHAT_MESSAGE");

                    writeList.add("ONLINE_RANK_COUNT");
                    // 在线排行榜， 不关心
                    writeList.add("ONLINE_RANK_V2");
                    // 未知
                    writeList.add("STOP_LIVE_ROOM_LIST");
                    // 在线排名
                    writeList.add("ONLINE_RANK_COUNT");

                    //  直播横幅广告推送
                    writeList.add("WIDGET_BANNER");

                    writeList.add("WATCHED_CHANGE"); // 人看过"
                    writeList.add("INTERACT_WORD"); // 新用户进入
                    writeList.add("ONLINE_RANK_TOP3"); // 成为高能用户 // {"cmd":"ONLINE_RANK_TOP3","data":{"dmscore":112,"list":[{"msg":"恭喜 \u003c%小夜酱爱断线%\u003e 成为高能用户","rank":3}]}}
                    writeList.add("AREA_RANK_CHANGED"); // 当前分区排名
                    writeList.add("POPULAR_RANK_CHANGED");// 人气排名
                    writeList.add("ENTRY_EFFECT"); // xx成为高能用户
                    // pk相关

                    writeList.add("PK_BATTLE_PROCESS_NEW"); // {"cmd":"PK_BATTLE_PROCESS_NEW","data":{"battle_type":1,"init_info":{"room_id":27213421,"votes":0,"best_uname":"","vision_desc":0},"match_info":{"room_id":25675792,"votes":2,"best_uname":"剑如君子","vision_desc":0}},"pk_id":323770729,"pk_status":201,"timestamp":1682383599}
                    // pk过程
                    writeList.add("PK_BATTLE_PROCESS"); // {"cmd":"PK_BATTLE_PROCESS","data":{"battle_type":1,"init_info":{"room_id":27213421,"votes":0,"best_uname":"","vision_desc":0},"match_info":{"room_id":25675792,"votes":2,"best_uname":"剑如君子","vision_desc":0}},"pk_id":323770729,"pk_status":201,"timestamp":1682383599}
                    writeList.add("NOTICE_MSG");// 广告消息推送
                    writeList.add("LIKE_INFO_V3_UPDATE");//  统计点击数量
                    writeList.add("LIKE_INFO_V3_CLICK");//  手机双击点赞推送

                    if (!writeList.contains(cmd)) {
//                        log.info("消息类型为" + cmd);
//                        log.info("消息为" + message);
                    }

                    switch (cmd) {
                        // 弹幕
                        case "DANMU_MSG":
                            // log.info("弹幕 message");
                            // log.info(message);
                            danmuService.danmuFunction(message);
                            break;

                        // 送普通礼物
                        case "SEND_GIFT":
                            // log.info("SEND_GIFT");
                            // log.info(message);

                            danmuService.giftFunction(message);
//                            action: "投喂"
//                            coin_type: "silver"
//                            giftId: 1
//                            giftName: "辣条"
//                            guard_level: 0
//                            medalInfo: {anchor_roomid: '0', anchor_uname: '', guard_level: 3, icon_id: 0, is_lighted: 1, …}
//                            num: 1
//                            price: 100
//                            timestamp: 1681049747
//                            uid: 939948
//                            uname: "春风十里不如一路有语"


                            //{"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"1c8a7b39-af71-4a6b-9aa8-9b8947e5d609","batch_combo_send":{"action":"投喂","batch_combo_id":"1c8a7b39-af71-4a6b-9aa8-9b8947e5d609","batch_combo_num":1,"blind_gift":{"blind_gift_config_id":55,"from":0,"gift_action":"爆出","gift_tip_price":106000,"original_gift_id":32369,"original_gift_name":"至尊盲盒","original_gift_price":100000},"gift_id":32362,"gift_name":"马戏之王","gift_num":1,"send_master":null,"uid":552667853,"uname":"黑听の居居"},"beatId":"0","biz_source":"Live","blind_gift":{"blind_gift_config_id":55,"from":0,"gift_action":"爆出","gift_tip_price":106000,"original_gift_id":32369,"original_gift_name":"至尊盲盒","original_gift_price":100000},"broadcast_id":0,"coin_type":"gold","combo_resources_id":3,"combo_send":{"action":"投喂","combo_id":"7af3862d-8de0-4fcb-b8aa-f18a8cb3138f","combo_num":1,"gift_id":32362,"gift_name":"马戏之王","gift_num":1,"send_master":null,"uid":552667853,"uname":"黑听の居居"},"combo_stay_time":12,"combo_total_coin":106000,"crit_prob":0,"demarcation":3,"discount_price":106000,"dmscore":120,"draw":0,"effect":2,"effect_block":0,"face":"https://i2.hdslb.com/bfs/face/5c8420d2b23ee3223b99b17633cb2966f4f3d228.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":2,"giftId":32362,"giftName":"马戏之王","giftType":0,"gold":0,"guard_level":3,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":3,"icon_id":0,"is_lighted":1,"medal_color":398668,"medal_color_border":6809855,"medal_color_end":6850801,"medal_color_start":398668,"medal_level":25,"medal_name":"被融化","special":"","target_id":629601798},"name_color":"#00D1F1","num":1,"original_gift_name":"","price":106000,"rcost":4207567,"receive_user_info":{"uid":629601798,"uname":"野比大融official"},"remain":0,"rnd":"1681001819111300001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":1,"super_gift_num":1,"svga_block":0,"switch":true,"tag_image":"","tid":"1681001819111300001","timestamp":1681001819,"top_list":null,"total_coin":100000,"uid":552667853,"uname":"黑听の居居"}}
                            //2023-04-09 08:56:59:收到道具:黑听の居居 投喂的:马戏之王 x 1

                            // 收到辣条
//                            2023-04-04 11:39:46.019 | Thread-4raceId | INFO  | 4260 | Thread-4 | com.uid939948.thread.core.ParseMessageThread.run(ParseMessageThread.java:388) : {"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"","batch_combo_send":null,"beatId":"0","biz_source":"Live","blind_gift":null,"broadcast_id":0,"coin_type":"silver","combo_resources_id":1,"combo_send":null,"combo_stay_time":5,"combo_total_coin":0,"crit_prob":0,"demarcation":1,"discount_price":0,"dmscore":36,"draw":0,"effect":0,"effect_block":1,"face":"http://i2.hdslb.com/bfs/face/338455da6654898872921213b8f2a45c4ad02c44.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":0,"giftId":1,"giftName":"辣条","giftType":5,"gold":0,"guard_level":0,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":0,"icon_id":0,"is_lighted":1,"medal_color":13081892,"medal_color_border":13081892,"medal_color_end":13081892,"medal_color_start":13081892,"medal_level":18,"medal_name":"卿言","special":"","target_id":8192168},"name_color":"","num":50,"original_gift_name":"","price":100,"rcost":171567126,"receive_user_info":{"uid":8192168,"uname":"叶落莫言"},"remain":0,"rnd":"1680579688110400001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":0,"super_gift_num":0,"svga_block":0,"switch":true,"tag_image":"","tid":"1680579688110400001","timestamp":1680579688,"top_list":null,"total_coin":5000,"uid":37718504,"uname":"Savoki九酱"}}
//                        2023-04-04 11:41:28:收到道具:Savoki九酱 投喂的:辣条 x 50


//                            if (getCenterSetConf().is_gift()) {
//                                jsonObject = JSONObject.parseObject(jsonObject.getString("data"));
//                                gift = Gift.getGift(jsonObject.getInteger("giftId"), jsonObject.getShort("giftType"),
//                                        jsonObject.getString("giftName"), jsonObject.getInteger("num"),
//                                        jsonObject.getString("uname"), jsonObject.getString("face"),
//                                        jsonObject.getShort("guard_level"), jsonObject.getLong("uid"),
//                                        jsonObject.getLong("timestamp"), jsonObject.getString("action"),
//                                        jsonObject.getInteger("price"),
//                                        ParseIndentityTools.parseCoin_type(jsonObject.getString("coin_type")),
//                                        jsonObject.getLong("total_coin"), jsonObject.getObject("medal_info", MedalInfo.class));
//                                //							giftFile = new GiftFile(jsonObject.getInteger("giftId"), jsonObject.getString("giftName"),
//                                //									jsonObject.getInteger("price"), jsonObject.getString("coin_type"));
//                                //							GiftFileTools.addGiftToFile(giftFile.toJson());
//                                stringBuilder.append(JodaTimeUtils.formatDateTime(gift.getTimestamp() * 1000));
//                                stringBuilder.append(":收到道具:");
//                                stringBuilder.append(gift.getUname());
//                                stringBuilder.append(" ");
//                                stringBuilder.append(gift.getAction());
//                                stringBuilder.append("的:");
//                                stringBuilder.append(gift.getGiftName());
//                                stringBuilder.append(" x ");
//                                stringBuilder.append(gift.getNum());
//                                //控制台打印
////                                if (getCenterSetConf().is_cmd()) {
////                                    System.out.println(stringBuilder.toString());
////                                    log.info(stringBuilder.toString());
////                                }
//                                try {
////                                    danmuWebsocket.sendMessage(WsPackage.toJson("gift", (short) 0, gift));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            } else {
//                                //感谢礼物御前处理
////                                if (MainConf.sendBarrageThread != null) {
////                                    if (!MainConf.sendBarrageThread.FLAG && !MainConf.parsethankGiftThread.TFLAG) {
////                                        jsonObject = JSONObject.parseObject(jsonObject.getString("data"));
////                                        gift = Gift.getGift(jsonObject.getInteger("giftId"), jsonObject.getShort("giftType"),
////                                                jsonObject.getString("giftName"), jsonObject.getInteger("num"),
////                                                jsonObject.getString("uname"), jsonObject.getString("face"),
////                                                jsonObject.getShort("guard_level"), jsonObject.getLong("uid"),
////                                                jsonObject.getLong("timestamp"), jsonObject.getString("action"),
////                                                jsonObject.getInteger("price"),
////                                                ParseIndentityTools.parseCoin_type(jsonObject.getString("coin_type")),
////                                                jsonObject.getLong("total_coin"), jsonObject.getObject("medal_info", MedalInfo.class));
////                                    }
////                                }
//                            }


                            // 感谢礼物处理
//                            if (gift != null && getCenterSetConf().getThank_gift().is_giftThank()) {
//                                if (MainConf.sendBarrageThread != null && !MainConf.sendBarrageThread.FLAG) {
//                                    if (ParseSetStatusTools.getGiftShieldStatus(
//                                            getCenterSetConf().getThank_gift().getShield_status()) != ShieldGift.CUSTOM_RULE) {
//                                        gift = ShieldGiftTools.shieldGift(gift,
//                                                ParseSetStatusTools.getListGiftShieldStatus(
//                                                        getCenterSetConf().getThank_gift().getList_gift_shield_status()),
//                                                ParseSetStatusTools.getListPeopleShieldStatus(
//                                                        getCenterSetConf().getThank_gift().getList_people_shield_status()),
//                                                ParseSetStatusTools
//                                                        .getGiftShieldStatus(getCenterSetConf().getThank_gift().getShield_status()),
//                                                getCenterSetConf().getThank_gift().getGiftStrings(), null);
//                                    }
//                                    if (gift != null) {
//                                        if (!StringUtils.isEmpty(MainConf.SHIELDGIFTNAME)) {
//                                            if (gift.getGiftName().equals(MainConf.SHIELDGIFTNAME)) {
//                                                gift = null;
//                                            }
//                                        }
//                                    }
//
//                                    try {
//                                        parseGiftSetting(gift);
//                                    } catch (Exception e) {
//                                        // TODO 自动生成的 catch 块
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
////                            LOGGER.info("让我看看是谁送礼物:::"+jsonObject);
                            break;

                        // 部分金瓜子礼物连击
                        case "COMBO_SEND":
                            //					LOGGER.info("部分金瓜子礼物连击:::" + message);
                            break;// 练级
// {"cmd":"COMBO_SEND","data":{"action":"投喂","batch_combo_id":"batch:gift:combo_id:3493113530157391:477482558:32345:1682377966.6607","batch_combo_num":20,"combo_id":"gift:combo_id:3493113530157391:477482558:32345:1682377966.6573","combo_num":20,"combo_total_coin":2000,"dmscore":120,"gift_id":32345,"gift_name":"人气票","gift_num":0,"is_join_receiver":false,"is_naming":false,"is_show":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":3,"icon_id":0,"is_lighted":1,"medal_color":1725515,"medal_color_border":6809855,"medal_color_end":5414290,"medal_color_start":1725515,"medal_level":21,"medal_name":"逗ni玩","special":"","target_id":477482558},"name_color":"#00D1F1","r_uname":"兔豆ni","receive_user_info":{"uid":477482558,"uname":"兔豆ni"},"ruid":477482558,"send_master":null,"total_num":20,"uid":3493113530157391,"uname":"阿阳摸不着头"}}
                        // 部分金瓜子礼物连击
                        case "COMBO_END":
                            //					LOGGER.info("部分金瓜子礼物连击:::" + message);
                            break;

                        // 上舰
                        case "GUARD_BUY":
//                            log.info("有人上船");
//                            log.info("GUARD_BUY");
//                            log.info(message);
                            danmuService.guardBuyFunction(message);


//                            if (getCenterSetConf().is_gift()) {
//                                guard = JSONObject.parseObject(jsonObject.getString("data"), Guard.class);
//                                stringBuilder.append(JodaTimeUtils.formatDateTime(guard.getStart_time() * 1000));
//                                stringBuilder.append(":有人上船:");
//                                stringBuilder.append(guard.getUsername());
//                                stringBuilder.append("在本房间开通了");
//                                stringBuilder.append(guard.getNum());
//                                stringBuilder.append("个月");
//                                stringBuilder.append(guard.getGift_name());
//                                //控制台打印
////                                if (getCenterSetConf().is_cmd()) {
////                                    System.out.println(stringBuilder.toString());
////                                    log.info(stringBuilder.toString());
////                                }
//                                gift = new Gift();
//                                gift.setGiftName(guard.getGift_name());
//                                gift.setNum(guard.getNum());
//                                gift.setPrice(guard.getPrice());
//                                gift.setTotal_coin((long) guard.getNum() * guard.getPrice());
//                                gift.setTimestamp(guard.getStart_time());
//                                gift.setAction("赠送");
//                                gift.setCoin_type((short) 1);
//                                gift.setUname(guard.getUsername());
//                                gift.setUid(guard.getUid());
//                                try {
////                                    danmuWebsocket.sendMessage(WsPackage.toJson("gift", (short) 0, gift));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }


//                            if (getCenterSetConf().getThank_gift().is_giftThank()) {
//                                if (MainConf.parsethankGiftThread != null && !MainConf.parsethankGiftThread.TFLAG) {
//                                    guard = JSONObject.parseObject(jsonObject.getString("data"), Guard.class);
//                                    gift = new Gift();
//                                    gift.setGiftName(guard.getGift_name());
//                                    gift.setNum(guard.getNum());
//                                    gift.setPrice(guard.getPrice());
//                                    gift.setTotal_coin((long) guard.getNum() * guard.getPrice());
//                                    gift.setTimestamp(guard.getStart_time());
//                                    gift.setAction("赠送");
//                                    gift.setCoin_type((short) 1);
//                                    gift.setUname(guard.getUsername());
//                                    gift.setUid(guard.getUid());
//                                    gift = ShieldGiftTools.shieldGift(gift,
//                                            ParseSetStatusTools.getListGiftShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_gift_shield_status()),
//                                            ParseSetStatusTools.getListPeopleShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_people_shield_status()),
//                                            ParseSetStatusTools.getGiftShieldStatus(getCenterSetConf().getThank_gift().getShield_status()),
//                                            getCenterSetConf().getThank_gift().getGiftStrings(), null);
//                                    if (gift != null) {
//                                        try {
//                                            parseGiftSetting(gift);
//                                        } catch (Exception e) {
//                                            // TODO 自动生成的 catch 块
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            }
                            //开启舰长存放本地
//                            if (getCenterSetConf().getThank_gift().is_guard_local()) {
//                                guard = JSONObject.parseObject(jsonObject.getString("data"), Guard.class);
//                                Hashtable<Long, String> guardHashtable_local = GuardFileTools.read();
//                                if (guardHashtable_local == null) {
//                                    guardHashtable_local = new Hashtable<Long, String>();
//                                }
//                                //写入
//                                if (!guardHashtable_local.containsKey(guard.getUid())) {
//                                    GuardFileTools.write(guard.getUid() + "," + guard.getUsername());
//
//                                }
//                            }
                            // 发送上舰私聊
//                            if (getCenterSetConf().getThank_gift().is_guard_report()) {
//                                guard = JSONObject.parseObject(jsonObject.getString("data"), Guard.class);
//                                short guard_level = guard.getGuard_level();
//                                String report = StringUtils.replace(getCenterSetConf().getThank_gift().getReport(), "\n", "\\n");
//                                //替换名称
//                                report = StringUtils.replace(report, "%uName%", guard.getUsername());
//                                //替换舰队级别
//                                if (guard_level == 1) {
//                                    report = StringUtils.replace(report, "%guardLevel%", "总督");
//                                } else if (guard_level == 2) {
//                                    report = StringUtils.replace(report, "%guardLevel%", "提督");
//                                } else if (guard_level == 3) {
//                                    report = StringUtils.replace(report, "%guardLevel%", "舰长");
//                                } else {
//                                    report = StringUtils.replace(report, "%guardLevel%", "上船");
//                                }
//                                //礼品码
//                                if (getCenterSetConf().getThank_gift().is_gift_code()
//                                        && !CollectionUtils.isEmpty(getCenterSetConf().getThank_gift().getCodeStrings())) {
//                                    report = StringUtils.replace(report, "%giftCode%", this.sendCode(guard_level));
//                                } else {
//                                    report = StringUtils.replace(report, "%giftCode%", "");
//                                }
//                                try {
//                                    if (!MainConf.TEST_MODE) {
//                                        if (!StringUtils.isEmpty(getCenterSetConf().getThank_gift().getReport_barrage().trim())) {
//                                            if (HttpUserData.httpPostSendMsg(guard.getUid(), report) == 0) {
//                                                MainConf.barrageString.add(getCenterSetConf().getThank_gift().getReport_barrage());
//                                                synchronized (MainConf.sendBarrageThread) {
//                                                    MainConf.sendBarrageThread.notify();
//                                                }
//                                            }
//                                        } else {
//                                            HttpUserData.httpPostSendMsg(guard.getUid(), report);
//                                        }
//                                    } else {
//                                        LOGGER.info("私信姬：发送的弹幕:{}",getCenterSetConf().getThank_gift().getReport_barrage());
//                                        LOGGER.info("私信姬：发送的私聊:{}",report);
//                                    }
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                    LOGGER.error("发送舰长私信失败，原因：" + e);
//                                }
//                            }
////                            LOGGER.info("有人上舰长啦:::" + message);
                            break;

                        // 上舰消息推送
                        case "GUARD_LOTTERY_START":
                            //					LOGGER.info("上舰消息推送:::" + message);
                            break;

                        // 上舰抽奖消息推送
                        case "USER_TOAST_MSG":
                            //					LOGGER.info("上舰抽奖消息推送:::" + message);
                            danmuService.ToastFunction(message);

                            break;

                        // 醒目留言
                        case "SUPER_CHAT_MESSAGE":
//
//                            log.info("SUPER_CHAT_MESSAGE");
//                            log.info("醒目留言");
//                            log.info(message);

                            danmuService.superChatFunction(message);

// {"cmd":"SUPER_CHAT_MESSAGE","data":{"background_bottom_color":"#2A60B2","background_color":"#EDF5FF","background_color_end":"#405D85","background_color_start":"#3171D2","background_icon":"","background_image":"https://i0.hdslb.com/bfs/live/a712efa5c6ebc67bafbe8352d3e74b820a00c13e.png","background_price_color":"#7497CD","color_point":0.7,"dmscore":80,"end_time":1680952417,"gift":{"gift_id":12000,"gift_name":"醒目留言","num":1},"id":6876274,"is_ranked":1,"is_send_audit":0,"medal_info":{"anchor_roomid":24697277,"anchor_uname":"爱尔薇特Elverte","guard_level":0,"icon_id":0,"is_lighted":1,"medal_color":"#8d7ca6","medal_color_border":9272486,"medal_color_end":9272486,"medal_color_start":9272486,"medal_level":12,"medal_name":"白狼团","special":"","target_id":2066486696},"message":"二百一周年快乐！从狼团子一直白嫖到现在，不想要返50那就不上舰发条sc吧doge","message_font_color":"#A3F6FF","message_trans":"","price":30,"rate":1000,"start_time":1680952357,"time":59,"token":"7B5398B7","trans_mark":0,"ts":1680952358,"uid":382169855,"user_info":{"face":"https://i2.hdslb.com/bfs/face/18bba4b78b187693328be05d3b58607313cfe835.jpg","face_frame":"","guard_level":0,"is_main_vip":0,"is_svip":0,"is_vip":0,"level_color":"#61c05a","manager":0,"name_color":"#666666","title":"0","uname":"這都幾點啦","user_level":13}},"roomid":24697277}
//2023-04-08 19:12:37:收到留言:這都幾點啦 他用了300电池留言了60秒说: 二百一周年快乐！从狼团子一直白嫖到现在，不想要返50那就不上舰发条sc吧doge
//                            if (getCenterSetConf().is_gift()) {
//                                superChat = JSONObject.parseObject(jsonObject.getString("data"), SuperChat.class);
//                                stringBuilder.append(JodaTimeUtils.formatDateTime(superChat.getStart_time() * 1000));
//                                stringBuilder.append(":收到留言:");
//                                stringBuilder.append(superChat.getUser_info().getUname());
//                                stringBuilder.append(" 他用了");
//                                //适配6.11破站更新金瓜子为电池  叔叔真有你的
//                                stringBuilder.append(superChat.getPrice() * 10);
//                                stringBuilder.append("电池留言了");
//                                stringBuilder.append(ParseIndentityTools.parseTime(superChat.getTime()));
//                                stringBuilder.append("秒说: ");
//                                stringBuilder.append(superChat.getMessage());
//                                superChat.setTime(ParseIndentityTools.parseTime(superChat.getTime()));
//                                //控制台打印
////                                if (getCenterSetConf().is_cmd()) {
////                                    System.out.println(stringBuilder.toString());
////                                    log.info(stringBuilder.toString());
////                                }
//                                try {
////                                    danmuWebsocket.sendMessage(WsPackage.toJson("superchat", (short) 0, superChat));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }

//                            if (getCenterSetConf().getThank_gift().is_giftThank()) {
//                                if (MainConf.parsethankGiftThread != null && !MainConf.parsethankGiftThread.TFLAG) {
//                                    superChat = JSONObject.parseObject(jsonObject.getString("data"), SuperChat.class);
//                                    gift = new Gift();
//                                    stringBuilder.append(ParseIndentityTools.parseTime(superChat.getTime()));
//                                    stringBuilder.append("秒");
//                                    stringBuilder.append(superChat.getGift().getGift_name());
//                                    gift.setGiftName(stringBuilder.toString());
//                                    gift.setNum(superChat.getGift().getNum());
//                                    //适配6.11破站更新金瓜子为电池  叔叔真有你的
//                                    gift.setPrice(superChat.getPrice() * 10);
//                                    gift.setTotal_coin((long) superChat.getPrice() * 10l);
//                                    gift.setTimestamp(superChat.getStart_time() * 1000);
//                                    gift.setAction("赠送");
//                                    gift.setCoin_type((short) 1);
//                                    gift.setUname(superChat.getUser_info().getUname());
//                                    gift.setUid(superChat.getUid());
//                                    gift.setMedal_info(superChat.getMedal_info());
//                                    gift = ShieldGiftTools.shieldGift(gift,
//                                            ParseSetStatusTools.getListGiftShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_gift_shield_status()),
//                                            ParseSetStatusTools.getListPeopleShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_people_shield_status()),
//                                            ParseSetStatusTools.getGiftShieldStatus(getCenterSetConf().getThank_gift().getShield_status()),
//                                            getCenterSetConf().getThank_gift().getGiftStrings(), null);
//                                    if (gift != null) {
//                                        try {
//                                            parseGiftSetting(gift);
//                                        } catch (Exception e) {
//                                            // TODO 自动生成的 catch 块
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }
//                            //					LOGGER.info("收到醒目留言:::" + message);
                            break;

                        // 醒目留言日文翻译
                        case "SUPER_CHAT_MESSAGE_JPN":
                            //					LOGGER.info("醒目留言日文翻译消息推送:::" + message);
                            break;

                        // 删除醒目留言
                        case "SUPER_CHAT_MESSAGE_DELETE":
                            //					LOGGER.info("该条醒目留言已被删除:::" + message);
                            break;

                        // 欢迎老爷进来本直播间
                        case "WELCOME":
                            // 区分年月费老爷
                            /*
                             * if(welcomVip.getSvip()==1) {
                             * System.out.println(JodaTimeUtils.getCurrentTimeString()+":欢迎年费老爷:"+welcomeVip
                             * .getUname()+" 进入直播间"); }else {
                             * System.out.println(JodaTimeUtils.getCurrentTimeString()+":欢迎月费老爷:"+welcomeVip
                             * .getUname()+" 进入直播间"); }
                             */
//                            if (getCenterSetConf().is_welcome_ye()) {
//                                welcomeVip = JSONObject.parseObject(jsonObject.getString("data"), WelcomeVip.class);
//                                stringBuilder.append(JodaTimeUtils.getCurrentDateTimeString());
//                                stringBuilder.append(":欢迎老爷:");
//                                stringBuilder.append(welcomeVip.getUname());
//                                stringBuilder.append(" 进入直播间");
//                                //控制台打印
//                                if (getCenterSetConf().is_cmd()) {
//                                    System.out.println(stringBuilder.toString());
//                                }
//                                try {
//                                    danmuWebsocket.sendMessage(WsPackage.toJson("welcomeVip", (short) 0, welcomeVip));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }

                            //					LOGGER.info("让我看看哪个老爷大户进来了:::" + message);
                            break;

                        // 欢迎舰长进入直播间
                        case "WELCOME_GUARD":

//                            if (getCenterSetConf().is_welcome_ye()) {
//                                welcomeGuard = JSONObject.parseObject(jsonObject.getString("data"), WelcomeGuard.class);
//                                stringBuilder.append(JodaTimeUtils.getCurrentDateTimeString());
//                                switch (welcomeGuard.getGuard_level()) {
//                                    case 3:
//                                        stringBuilder.append(":欢迎舰长:");
//                                        break;
//                                    case 2:
//                                        stringBuilder.append(":欢迎提督:");
//                                        break;
//                                    case 1:
//                                        stringBuilder.append(":欢迎总督:");
//                                        break;
//                                }
//                                stringBuilder.append(welcomeGuard.getUsername());
//                                stringBuilder.append(" 进入直播间");
//                                //控制台打印
////                                if (getCenterSetConf().is_cmd()) {
////                                    System.out.println(stringBuilder.toString());
////                                    log.info(stringBuilder.toString());
////                                }
//                                try {
////                                    danmuWebsocket.sendMessage(WsPackage.toJson("welcomeGuard", (short) 0, welcomeGuard));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }
                            //					LOGGER.info("舰长大大进来直播间了:::" + message);
                            break;

                        // 舰长进入直播间消息
                        case "ENTRY_EFFECT":
//                            log.info("舰长大大进入直播间消息推送:::" + message);
//                            舰长大大进入直播间消息推送:::{"cmd":"ENTRY_EFFECT","data":{"id":253,"uid":7835489,"target_id":16510117,"mock_effect":0,"face":"https://i2.hdslb.com/bfs/face/01e1eab50f5dbb72ff44bba62aff5cd1aa59f5ed.jpg","privilege_type":0,"copy_writing":"欢迎\u003c%星月盲盒%\u003e进入直播间","copy_color":"#ffffff","highlight_color":"#ffea18","priority":1,"basemap_url":"https://i0.hdslb.com/bfs/live/mlive/6d38ab463be28a130870c8c43d109473f215963e.png","show_avatar":1,"effective_time":3,"web_basemap_url":"https://i0.hdslb.com/bfs/live/mlive/6d38ab463be28a130870c8c43d109473f215963e.png","web_effective_time":3,"web_effect_close":1,"web_close_time":900,"business":4,"copy_writing_v2":"欢迎\u003c%星月盲盒%\u003e进入直播间","icon_list":[],"max_delay_time":7,"trigger_time":1680861344828289458,"identities":1,"effect_silent_time":0,"effective_time_new":0,"web_dynamic_url_webp":"","web_dynamic_url_apng":"","mobile_dynamic_url_webp":""}}
                            break; // 2023-04-25 07:47:44.884 | Thread-5raceId | INFO  | 15044 | Thread-5 | com.uid939948.thread.core.ParseMessageThread.run(ParseMessageThread.java:619) : 舰长大大进入直播间消息推送:::{"cmd":"ENTRY_EFFECT","data":{"id":253,"uid":32524769,"target_id":511911998,"mock_effect":0,"face":"https://i2.hdslb.com/bfs/face/047b315239aa05c587e0aae135045ea406588c65.jpg","privilege_type":0,"copy_writing":"欢迎\u003c%变成光守护电子...%\u003e进入直播间","copy_color":"#ffffff","highlight_color":"#ffea18","priority":1,"basemap_url":"https://i0.hdslb.com/bfs/live/mlive/6d38ab463be28a130870c8c43d109473f215963e.png","show_avatar":1,"effective_time":3,"web_basemap_url":"https://i0.hdslb.com/bfs/live/mlive/6d38ab463be28a130870c8c43d109473f215963e.png","web_effective_time":3,"web_effect_close":1,"web_close_time":900,"business":4,"copy_writing_v2":"欢迎\u003c%变成光守护电…%\u003e进入直播间","icon_list":[],"max_delay_time":7,"trigger_time":1682380167497227574,"identities":1,"effect_silent_time":0,"effective_time_new":0,"web_dynamic_url_webp":"","web_dynamic_url_apng":"","mobile_dynamic_url_webp":""}}
//  舰长大大进入直播间消息推送:::{"cmd":"ENTRY_EFFECT","data":{"id":2,"uid":1638764183,"target_id":16510117,"mock_effect":0,"face":"https://i2.hdslb.com/bfs/face/68701359a6a92c374440a3be3f1cd6c8489e543c.jpg","privilege_type":2,"copy_writing":"欢迎提督 \u003c%机智大玖奕%\u003e 进入直播间","copy_color":"#ffffff","highlight_color":"#FFF100","priority":1,"basemap_url":"https://i0.hdslb.com/bfs/live/mlive/74a41c65e422116d230d433042881fa5556f7870.png","show_avatar":1,"effective_time":3,"web_basemap_url":"https://i0.hdslb.com/bfs/live/mlive/74a41c65e422116d230d433042881fa5556f7870.png","web_effective_time":3,"web_effect_close":0,"web_close_time":0,"business":1,"copy_writing_v2":"欢迎提督 \u003c%机智大玖奕%\u003e 进入直播间","icon_list":[],"max_delay_time":7,"trigger_time":1680861831272252135,"identities":7,"effect_silent_time":0,"effective_time_new":0,"web_dynamic_url_webp":"","web_dynamic_url_apng":"","mobile_dynamic_url_webp":""}}
                        // 节奏风暴推送 action 为start和end
                        case "SPECIAL_GIFT":
                            //					LOGGER.info("节奏风暴推送:::" + message);
                            log.info("节奏风暴推送:::" + message);
                            break;

                        // 禁言消息
                        case "ROOM_BLOCK_MSG":
//                            if (getCenterSetConf().is_block()) {
//                                blockMessage = JSONObject.parseObject(jsonObject.getString("data"), BlockMessage.class);
//                                stringBuilder.append(JodaTimeUtils.getCurrentDateTimeString());
//                                stringBuilder.append(":禁言消息:");
//                                stringBuilder.append(blockMessage.getUname());
//                                if (blockMessage.getOperator() == 2) {
//                                    stringBuilder.append("已被主播禁言");
//                                } else if (blockMessage.getOperator() == 1) {
//                                    stringBuilder.append("已被房管禁言");
//                                } else {
//                                    stringBuilder.append("已被管理员禁言");
//                                }
//                                //控制台打印
//                                if (getCenterSetConf().is_cmd()) {
//                                    System.out.println(stringBuilder.toString());
//                                }
//                                try {
//                                    danmuWebsocket.sendMessage(WsPackage.toJson("block", (short) 0, blockMessage));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
//                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
//                                    MainConf.logString.add(stringBuilder.toString());
//                                    synchronized (MainConf.logThread) {
//                                        MainConf.logThread.notify();
//                                    }
//                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }
//                            //					LOGGER.info("谁这么惨被禁言了:::" + message);
//                            break;

                            // 本主播在本分区小时榜排名更新推送 不会更新页面的排行显示信息
                        case "ACTIVITY_BANNER_UPDATE_V2":
                            //					LOGGER.info("小时榜消息更新推送:::" + message);
                            break;

                        // 本房间分区修改
                        case "ROOM_CHANGE":
                            //					LOGGER.info("房间分区已更新:::" + message);
                            break;

                        // 本房间分区排行榜更新 更新页面的排行显示信息
                        case "ROOM_RANK":
                            //					rannk = JSONObject.parseObject(jsonObject.getString("data"), Rannk.class);
                            //					stringBuilder.append(JodaTimeUtils.format(rannk.getTimestamp() * 1000)).append(":榜单更新:")
                            //							.append(rannk.getRank_desc());
                            //
                            //					System.out.println(stringBuilder.toString());
                            //					stringBuilder.delete(0, stringBuilder.length());
                            //					LOGGER.info("小时榜信息更新推送:::" + message);
                            break;
                        // 推测为获取本小时榜榜单第一名主播的信息 推测激活条件为本直播间获得第一
                        case "new_anchor_reward":
                            //					LOGGER.info("获取本小时榜榜单第一名主播的信息:::" + message);
                            break;

                        // 小时榜榜单信息推送 推测激活条件为本直播间获得第一
                        case "HOUR_RANK_AWARDS":
                            //					LOGGER.info("恭喜xxxx直播间获得:::" + message);
                            break;

                        // 直播间粉丝数更新 经常
//                        case "ROOM_REAL_TIME_MESSAGE_UPDATE":
//                            //					fans = JSONObject.parseObject(jsonObject.getString("data"), Fans.class);
//                            //					stringBuilder.append(JodaTimeUtils.getCurrentTimeString()).append(":消息推送:").append("房间号")
//                            //							.append(fans.getRoomid()).append("的粉丝数:").append(fans.getFans());
//                            MainConf.FANSNUM = JSONObject.parseObject(jsonObject.getString("data")).getLong("fans");
//                            //					System.out.println(stringBuilder.toString());
//                            //					stringBuilder.delete(0, stringBuilder.length());
//                            //					LOGGER.info("直播间粉丝数更新消息推送:::" + message);
//                            break;
//{"cmd":"ROOM_REAL_TIME_MESSAGE_UPDATE","data":{"roomid":25675792,"fans":3063,"red_notice":-1,"fans_club":107}}
                        // 直播间许愿瓶消息推送更新
                        case "WISH_BOTTLE":
                            //					LOGGER.info("直播间许愿瓶消息推送更新:::" + message);
                            break;

                        // 广播小电视类抽奖信息推送,包括本房间的舰长礼物包括,本直播间所在小时榜榜单主播信息的推送 需要unicode转义 免费辣条再见！！！！
                        case "NOTICE_MSG":
                            //					message = ByteUtils.unicodeToString(message);
//                            log.info("小电视类抽奖信息推送:::" + message);
                            // todo 只收集统计

                            // xx小电视 城堡 或者 周五宝盒翻倍
                            //小电视类抽奖信息推送:::{"cmd":"NOTICE_MSG","id":1,"name":"全区道具抽奖广播样式","full":{"head_icon":"http://i0.hdslb.com/bfs/live/b29add66421580c3e680d784a827202e512a40a0.webp","tail_icon":"http://i0.hdslb.com/bfs/live/822da481fdaba986d738db5d8fd469ffa95a8fa1.webp","head_icon_fa":"http://i0.hdslb.com/bfs/live/49869a52d6225a3e70bbf1f4da63f199a95384b2.png","tail_icon_fa":"http://i0.hdslb.com/bfs/live/38cb2a9f1209b16c0f15162b0b553e3b28d9f16f.png","head_icon_fan":24,"tail_icon_fan":4,"background":"#66A74EFF","color":"#FFFFFFFF","highlight":"#FDFF2FFF","time":20},"half":{"head_icon":"http://i0.hdslb.com/bfs/live/ec9b374caec5bd84898f3780a10189be96b86d4e.png","tail_icon":"","background":"#85B971FF","color":"#FFFFFFFF","highlight":"#FDFF2FFF","time":15},"side":{"head_icon":"","background":"","color":"","highlight":"","border":""},"roomid":27295483,"real_roomid":27295483,"msg_common":"\u003c%白白的艺桐%\u003e投喂\u003c%周艺桐IUV%\u003e1个浪漫城堡，点击前往TA的房间吧！","msg_self":"\u003c%白白的艺桐%\u003e投喂\u003c%周艺桐IUV%\u003e1个浪漫城堡，快来围观吧！","link_url":"https://live.bilibili.com/27295483?broadcast_type=1\u0026is_room_feed=1\u0026from=28003\u0026extra_jump_from=28003\u0026live_lottery_type=1","msg_type":2,"shield_uid":-1,"business_id":"32132","scatter":{"min":0,"max":0},"marquee_id":"","notice_type":0}
                            break;

                        // 本房间开启活动抽奖(33图,小电视图,任意门等) 也指本房间内赠送的小电视 摩天大楼类抽奖
                        case "RAFFLE_START":
                            //					LOGGER.info("本房间开启了活动抽奖:::" + message);
                            break;

                        // 本房间活动中奖用户信息推送 也指抽奖结束
                        case "RAFFLE_END":
                            //					LOGGER.info("看看谁是幸运儿:::" + message);
                            break;
                        // 本房间主播开启了天选时刻
                        case "ANCHOR_LOT_START":
                            danmuService.anchor_lot_start_Function(message);
                            // log.info("天选开启" + message);
                            // 天选开启{"cmd":"ANCHOR_LOT_START","data":{"asset_icon":"https://i0.hdslb.com/bfs/live/627ee2d9e71c682810e7dc4400d5ae2713442c02.png","asset_icon_webp":"https://i0.hdslb.com/bfs/live/b47453a0d42f30673b6d030159a96d07905d677a.webp","award_image":"","award_name":"情书","award_num":1,"award_price_text":"价值52电池","award_type":1,"cur_gift_num":0,"current_time":1680861702,"danmu":"好耶","danmu_new":[{"danmu":"好耶","danmu_view":"","reject":false}],"danmu_type":0,"gift_id":0,"gift_name":"","gift_num":0,"gift_price":0,"goaway_time":180,"goods_id":-99998,"id":4146038,"is_broadcast":1,"join_type":0,"lot_status":0,"max_time":900,"require_text":"当前主播粉丝勋章至少1级","require_type":2,"require_value":1,"room_id":623698,"send_gift_ensure":0,"show_panel":1,"start_dont_popup":0,"status":1,"time":899,"url":"https://live.bilibili.com/p/html/live-lottery/anchor-join.html?is_live_half_webview=1\u0026hybrid_biz=live-lottery-anchor\u0026hybrid_half_ui=1,5,100p,100p,000000,0,30,0,0,1;2,5,100p,100p,000000,0,30,0,0,1;3,5,100p,100p,000000,0,30,0,0,1;4,5,100p,100p,000000,0,30,0,0,1;5,5,100p,100p,000000,0,30,0,0,1;6,5,100p,100p,000000,0,30,0,0,1;7,5,100p,100p,000000,0,30,0,0,1;8,5,100p,100p,000000,0,30,0,0,1","web_url":"https://live.bilibili.com/p/html/live-lottery/anchor-join.html"}}
                            break;
                        // 本房间天选时刻结束
                        case "ANCHOR_LOT_END":
                            log.info("本房间天选时刻结束:::" + message);
                            // todo 待抓 本房间天选时刻结束:::{"cmd":"ANCHOR_LOT_END","data":{"id":4146146}}
                            break;

                        // 本房间天选时刻获奖信息推送
                        case "ANCHOR_LOT_AWARD":
                            danmuService.anchorLotAward_Function(message);


//                            log.info("本房间天选时刻中奖用户是:::" + message);
                            // todo 待抓  本房间天选时刻中奖用户是:::{"cmd":"ANCHOR_LOT_AWARD","data":{"award_dont_popup":1,"award_image":"","award_name":"情书","award_num":1,"award_price_text":"价值52电池","award_type":1,"award_users":[{"uid":1638764183,"uname":"机智大玖奕","face":"https://i2.hdslb.com/bfs/face/68701359a6a92c374440a3be3f1cd6c8489e543c.jpg","level":13,"color":6406234,"bag_id":5536349,"gift_id":31250,"num":1}],"id":4146146,"lot_status":2,"ruid":16510117,"url":"https://live.bilibili.com/p/html/live-lottery/anchor-join.html?is_live_half_webview=1\u0026hybrid_biz=live-lottery-anchor\u0026hybrid_half_ui=1,5,100p,100p,000000,0,30,0,0,1;2,5,100p,100p,000000,0,30,0,0,1;3,5,100p,100p,000000,0,30,0,0,1;4,5,100p,100p,000000,0,30,0,0,1;5,5,100p,100p,000000,0,30,0,0,1;6,5,100p,100p,000000,0,30,0,0,1;7,5,100p,100p,000000,0,30,0,0,1;8,5,100p,100p,000000,0,30,0,0,1",
                            break;

                        // 获得推荐位推荐消息
                        case "ANCHOR_NORMAL_NOTIFY":
                            //					LOGGER.info("本房间获得推荐位:::" + message);
                            break;
                        // 周星消息推送
                        case "WEEK_STAR_CLOCK":
                            //			        LOGGER.info("周星消息推送:::" + message);
                            break;

                        // 推测本主播周星信息更新
                        case "ROOM_BOX_MASTER":
                            //					LOGGER.info("周星信息更新:::" + message);
                            break;

                        // 周星消息推送关闭
                        case "ROOM_SKIN_MSG":
                            //					LOGGER.info("周星消息推送关闭:::" + message);
                            break;

                        // 中型礼物多数量赠送消息推送 例如b克拉 亿元
                        case "SYS_GIFT":
                            //					LOGGER.info("中型礼物多数量赠送消息推送:::" + message);
                            break;

                        // lol活动礼物？？？
                        case "ACTIVITY_MATCH_GIFT":
                            //					LOGGER.info("lol专属房间礼物赠送消息推送:::" + message);
                            break;

                        //----------------------------------------pk信息多为要uicode解码-------------------------------------------------
                        // 推测为房间pk信息推送
                        case "PK_BATTLE_ENTRANCE":
                            //			        LOGGER.info("房间pk活动信息推送:::" + message);
                            break;

                        // 活动pk准备
                        case "PK_BATTLE_PRE":
                            //			        LOGGER.info("房间活动pk准备:::" + message);
                            break;

                        // 活动pk开始
                        case "PK_BATTLE_START":
//                            log.info("房间活动pk开始:::" + message);
                            break;

                        // 活动pk中
                        case "PK_BATTLE_PROCESS":
//                            log.info("房间活动pk中:::" + message);
                            break;
                        case "PK_BATTLE_PROCESS_NEW":
                            // K_BATTLE_START_NEW
                            // PK_BATTLE_PRE_NEW
                            // PK_BATTLE_PRE
                            // PK_BATTLE_START  ---
                            // PK_BATTLE_PROCESS
                            //


                            // 送乱斗卡
//                            2023-04-07 13:42:16.424 | Thread-4raceId | INFO  | 8016 | Thread-4 | com.uid939948.thread.core.ParseMessageThread.run(ParseMessageThread.java:409) : {"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"","batch_combo_send":null,"beatId":"0","biz_source":"Live","blind_gift":null,"broadcast_id":0,"coin_type":"silver","combo_resources_id":1,"combo_send":null,"combo_stay_time":5,"combo_total_coin":0,"crit_prob":0,"demarcation":1,"discount_price":0,"dmscore":56,"draw":0,"effect":0,"effect_block":1,"face":"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":0,"giftId":30768,"giftName":"10乱斗值卡","giftType":5,"gold":0,"guard_level":0,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":0,"medal_color_border":0,"medal_color_end":0,"medal_color_start":0,"medal_level":0,"medal_name":"","special":"","target_id":0},"name_color":"","num":5,"original_gift_name":"","price":0,"rcost":2120673,"receive_user_info":{"uid":1007320348,"uname":"koi绾绾"},"remain":0,"rnd":"1680846239110800001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":0,"super_gift_num":0,"svga_block":0,"switch":true,"tag_image":"","tid":"1680846239110800001","timestamp":1680846239,"top_list":null,"total_coin":0,"uid":939948,"uname":"春风十里不如一路有语"}}
//                        2023-04-07 13:43:59:收到道具:春风十里不如一路有语 投喂的:10乱斗值卡 x 5

                            // 房间活动pk中 PK_BATTLE_PROCESS_NEW:::{"cmd":"PK_BATTLE_PROCESS_NEW","data":{"battle_type":1,"init_info":{"room_id":425837,"votes":0,"best_uname":"","vision_desc":0},"match_info":{"room_id":25914701,"votes":50,"best_uname":"春风十里不如一路有语","vision_desc":0}},"pk_id":322990390,"pk_status":201,"timestamp":1680846239}
//                            log.info("房间活动pk中 PK_BATTLE_PROCESS_NEW:::" + message);
                            break;


                        // 活动pk详细信息
                        case "PK_BATTLE_CRIT":
                            //			        LOGGER.info("房间活动pk详细信息推送:::" + message);
                            break;

                        // 活动pk类型推送
                        case "PK_BATTLE_PRO_TYPE":
                            //			        LOGGER.info("房间活动pk类型推送:::" + message);
                            break;

                        // 房间活动pk结束
                        case "PK_BATTLE_END":
//                            log.info("房间pk活动结束::" + message);
                            break;
//
//                        房间pk活动结束::{"cmd":"PK_BATTLE_END","pk_id":"322990390","pk_status":501,"timestamp":1680846390,"data":{"battle_type":1,"timer":10,"init_info":{"room_id":425837,"votes":0,"winner_type":-1,"best_uname":""},"match_info":{"room_id":25914701,"votes":110,"winner_type":3,"best_uname":"\u98df\u4e0d\u98df\u6cb9\u9905\u963f"}}}
//{"cmd":"PK_BATTLE_END","pk_id":"323879839","pk_status":501,"timestamp":1682602487,"data":{"battle_type":1,"timer":10,"init_info":{"room_id":22953683,"votes":1,"winner_type":-1,"best_uname":"\u6f29\u6da1\u76ae\u76ae"},"match_info":{"room_id":25675792,"votes":1050,"winner_type":3,"best_uname":"\u963f\u6728\u6728\u624d\u4e0d\u4f1a\u54ed\u5462"}}}
                        // 活动pk结果用户 推送
                        case "PK_BATTLE_SETTLE_USER":
//                            log.info("活动pk结果用户 推送::" + message);
                            break;

//                        活动pk结果用户 推送::{"cmd":"PK_BATTLE_SETTLE_USER","pk_id":322990390,"pk_status":501,"settle_status":1,"timestamp":1680846390,"data":{"pk_id":"322990390","season_id":60,"settle_status":1,"result_type":3,"battle_type":0,"result_info":{"total_score":22,"result_type_score":22,"pk_votes":110,"pk_votes_name":"\u4e71\u6597\u503c","pk_crit_score":-1,"pk_resist_crit_score":-1,"pk_extra_score_slot":"","pk_extra_value":0,"pk_extra_score":0,"pk_task_score":0,"pk_times_score":0,"pk_done_times":2,"pk_total_times":-1,"win_count":2,"win_final_hit":-1,"winner_count_score":0,"task_score_list":[]},"winner":{"room_id":25914701,"uid":1007320348,"uname":"koi\u7efe\u7efe","face":"https:\/\/i1.hdslb.com\/bfs\/face\/dd1e2774e5a2084f84ccc44df42352dfdc575d1f.jpg","face_frame":"https:\/\/i0.hdslb.com\/bfs\/live\/2a790ce8d2dd5f7c5c922f5ecda6e169e20d97b3.png","exp":{"color":6406234,"user_level":13,"master_level":{"color":10512625,"level":23}},"best_user":{"uid":1083112186,"uname":"\u98df\u4e0d\u98df\u6cb9\u9905\u963f","face":"https:\/\/i1.hdslb.com\/bfs\/face\/1d94bf2f24d8d5de675be56b836414abd58125e0.jpg","pk_votes":60,"pk_votes_name":"\u4e71\u6597\u503c","exp":{"color":6406234,"level":1},"face_frame":"","badge":{"url":"","desc":"","position":0},"award_info":null,"award_info_list":[],"end_win_award_info_list":{"list":[]}}},"my_info":{"room_id":25914701,"uid":1007320348,"uname":"koi\u7efe\u7efe","face":"https:\/\/i1.hdslb.com\/bfs\/face\/dd1e2774e5a2084f84ccc44df42352dfdc575d1f.jpg","face_frame":"https:\/\/i0.hdslb.com\/bfs\/live\/2a790ce8d2dd5f7c5c922f5ecda6e169e20d97b3.png","exp":{"color":6406234,"user_level":13,"master_level":{"color":10512625,"level":23}},"best_user":{"uid":1083112186,"uname":"\u98df\u4e0d\u98df\u6cb9\u9905\u963f","face":"https:\/\/i1.hdslb.com\/bfs\/face\/1d94bf2f24d8d5de675be56b836414abd58125e0.jpg","pk_votes":60,"pk_votes_name":"\u4e71\u6597\u503c","exp":{"color":6406234,"level":1},"face_frame":"","badge":{"url":"","desc":"","position":0},"award_info":null,"award_info_list":[],"end_win_award_info_list":{"list":[]}}},"level_info":{"first_rank_name":"\u9752\u94dc\u840c\u65b0","second_rank_num":2,"first_rank_img":"https:\/\/i0.hdslb.com\/bfs\/live\/bd6ca767900adbda7cd7148db06f72726bef7813.png","second_rank_icon":"https:\/\/i0.hdslb.com\/bfs\/live\/1f8c2a959f92592407514a1afeb705ddc55429cd.png"}}}


                        // 活动pk礼物开始 1辣条
                        case "PK_LOTTERY_START":
                            //			        LOGGER.info("活动pk礼物开始 推送::" + message);
                            break;

                        // 活动pk结果房间
                        case "PK_BATTLE_SETTLE":
                            //			        LOGGER.info("活动pk结果房间推送::" + message);
                            break;

                        // pk开始
                        case "PK_START":
                            //					LOGGER.info("房间pk开始:::" + message);
                            break;

                        // pk准备中
                        case "PK_PRE":
                            //					LOGGER.info("房间pk准备中:::" + message);
                            break;

                        // pk载入中
                        case "PK_MATCH":
                            //					LOGGER.info("房间pk载入中:::" + message);
                            break;

                        // pk再来一次触发
                        case "PK_CLICK_AGAIN":
                            //					LOGGER.info("房间pk再来一次:::" + message);
                            break;
                        // pk结束
                        case "PK_MIC_END":
                            //					LOGGER.info("房间pk结束:::" + message);
                            break;

                        // pk礼物信息推送 激活条件推测为pk胜利 可获得一个辣条
                        case "PK_PROCESS":
                            //					LOGGER.info("房间pk礼物推送:::" + message);
                            break;

                        // pk结果信息推送
                        case "PK_SETTLE":
                            //					LOGGER.info("房间pk结果信息推送:::" + message);
                            break;

                        // pk结束信息推送
                        case "PK_END":
                            //					LOGGER.info("房间pk结束信息推送:::" + message);
                            break;

                        // 系统信息推送
                        case "SYS_MSG":
                            //					LOGGER.info("系统信息推送:::" + message);
                            break;

                        // 总督登场消息
                        case "GUARD_MSG":
                            //					LOGGER.info("总督帅气登场:::" + message);
                            break;

                        // 热门房间？？？？广告房间？？ 不知道这是什么 推测本直播间激活 目前常见于打广告的官方直播间 例如手游 碧蓝航线 啥的。。
                        case "HOT_ROOM_NOTIFY":
                            //					LOGGER.info("热门房间推送消息:::" + message);
                            break;

                        // 小时榜面板消息推送
                        case "PANEL":
                            //					LOGGER.info("热小时榜面板消息推送:::" + message);
                            break;

                        // 星之耀宝箱使用 n
                        case "ROOM_BOX_USER":
                            //					LOGGER.info("星之耀宝箱使用:::" + message);
                            break;

                        // 语音加入？？？？ 暂不知道
                        case "VOICE_JOIN_ROOM_COUNT_INFO":
                            //					LOGGER.info("语音加入:::" + message);
                            break;

                        // 语音加入list？？？？ 暂不知道
                        case "VOICE_JOIN_LIST":
                            //					LOGGER.info("语音加入list:::" + message);
                            break;

                        // lol活动
                        case "LOL_ACTIVITY":
                            //					LOGGER.info("lol活动:::" + message);
                            break;

                        // 队伍礼物排名 目前只在6号lol房间抓取过
                        case "MATCH_TEAM_GIFT_RANK":
                            //					LOGGER.info("队伍礼物排名:::" + message);
                            break;

                        // 6.13端午节活动粽子新增活动更新命令 激活条件有人赠送活动礼物
                        case "ROOM_BANNER":
                            //					LOGGER.info("收到活动礼物赠送，更新信息:::" + message);
                            break;

                        // 设定房管消息 新房管的诞生
                        case "room_admin_entrance":
                            //					LOGGER.info("有人被设为了房管:::" + message);
                            break;

                        // 房管列表更新消息 激活条件为新房管的诞生
                        case "ROOM_ADMINS":
                            //					LOGGER.info("房管列表更新推送:::" + message);
                            break;

                        // 房间护盾 推测推送消息为破站官方屏蔽的关键字 触发条件未知
                        case "ROOM_SHIELD":
                            //					LOGGER.info("房间护盾触发消息:::" + message);
                            break;

                        // 主播开启房间全局禁言
                        case "ROOM_SILENT_ON":
                            //					LOGGER.info("主播开启房间全局禁言:::" + message);
                            break;

                        // 主播关闭房间全局禁言
                        case "ROOM_SILENT_OFF":
                            //					LOGGER.info("主播关闭房间全局禁言:::" + message);
                            break;

                        // 主播状态检测 直译 不知道什么情况 statue 1 ，2 ，3 ，4
                        case "ANCHOR_LOT_CHECKSTATUS":
                            //					LOGGER.info("主播房间状态检测:::" + message);
                            break;

                        // 房间警告消息 目前已知触发条件为 房间分区不正确
                        case "WARNING":
                            //					LOGGER.info("房间警告消息:::" + message);
                            break;
                        // 直播开启
                        case "LIVE":
                            MainConf.lIVE_STATUS = 1;
                            //					room_id = jsonObject.getLong("roomid");
                            //					if (room_id == MainConf.ROOMID) {
                            // 仅在直播有效 广告线程 改为配置文件
//                            setService.holdSet(getCenterSetConf());
//                            MainConf.IS_ROOM_POPULARITY = true;
                            //					LOGGER.info("直播开启:::" + message);


//                        {"cmd":"LIVE","live_key":"364276409968653039","voice_background":"","sub_session_key":"364276409968653039sub_time:1683106692","live_platform":"pc_link","live_model":0,"roomid":3961583}
                            break;

                        // 直播超管被切断
                        case "CUT_OFF":
                            //					LOGGER.info("很不幸，本房间直播被切断:::" + message);
                            break;

                        // 本房间已被封禁
                        case "ROOM_LOCK":
                            //					LOGGER.info("很不幸，本房间已被封禁:::" + message);
                            break;

                        // 直播准备中(或者是关闭直播)
                        case "PREPARING":
//                            MainConf.lIVE_STATUS = 0;
//                            setService.holdSet(getCenterSetConf());
//                            MainConf.IS_ROOM_POPULARITY = false;
//                            //					LOGGER.info("直播准备中(或者是关闭直播):::" + message);
//                            break;

//                        {"cmd":"PREPARING","roomid":"3961583"}
                            // 关闭直播了
                            // 勋章亲密度达到上每日上限通知
                        case "LITTLE_TIPS":
                            //					LOGGER.info("勋章亲密度达到上每日上限:::" + message);
                            break;

                        // msg_type 1 为进入直播间 2 为关注 3为分享直播间
                        case "INTERACT_WORD":
                            danmuService.interactFunction(message);
//
//                            // 21级牌子
////                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":20,"fans_medal":{"anchor_roomid":5096,"guard_level":0,"icon_id":0,"is_lighted":1,"medal_color":1725515,"medal_color_border":1725515,"medal_color_end":5414290,"medal_color_start":1725515,"medal_level":21,"medal_name":"猛男","score":50001680,"special":"","target_id":183430},"identities":[3,1],"is_spread":0,"msg_type":1,"privilege_type":0,"roomid":24441484,"score":1730236842911,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680225162,"trigger_time":1680225161789221000,"uid":939948,"uname":"春风十里不如一路有语","uname_color":""}}
//                            // 未点亮的牌子
////                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":10,"fans_medal":{"anchor_roomid":24441484,"guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":6126494,"medal_color_border":12632256,"medal_color_end":12632256,"medal_color_start":12632256,"medal_level":5,"medal_name":"崽总","score":2600,"special":"","target_id":1448028647},"identities":[1],"is_spread":0,"msg_type":3,"privilege_type":0,"roomid":24441484,"score":1680225235365,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680225235,"trigger_time":1680225235353997800,"uid":939948,"uname":"春风十里不如一路有语","uname_color":""}}
//// 流量包 消息为{"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":6,"fans_medal":{"anchor_roomid":25463741,"guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":6126494,"medal_color_border":12632256,"medal_color_end":12632256,"medal_color_start":12632256,"medal_level":6,"medal_name":"排骨吖","score":3200,"special":"","target_id":1691762261},"identities":[1],"is_spread":1,"msg_type":1,"privilege_type":0,"roomid":25675792,"score":1681900562667,"spread_desc":"流量包推广","spread_info":"#FF649E","tail_icon":0,"timestamp":1681900552,"trigger_time":1681900552657976300,"uid":1662202023,"uname":"迷鹿_迷了路","uname_color":""}}
//                            InteractWord interactWord = JSONObject.parseObject(JSONObject.parseObject(message).getString("data"), InteractWord.class);
//                            long l1 = interactWord.getTimestamp();
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date d1;
//                            String timestamp = String.valueOf(l1);
//                            try {
//                                d1 = simpleDateFormat.parse(simpleDateFormat.format(Long.parseLong(timestamp) * 1000));
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//                            String d2 = simpleDateFormat.format(d1);
////                            log.info("勋章级别" + GuardLevelEnum.getCountryValue(interactWord.getFans_medal().getGuard_level())
////                                    + " 勋章牌子名称" + interactWord.getFans_medal().getMedal_name()
////                                    + " 勋章等级" + interactWord.getFans_medal().getMedal_level());
////
////                            log.info("uid" + interactWord.getUid()
////                            +  " name" + interactWord.getUname()
////                                            + " 被发送的主播房间号" + interactWord.getRoomid()
////                                            + " 发送时间 :" + d2
////                            );
//
////                            // 级别
////                            String s1 = GuardLevelEnum.getCountryValue(interactWord.getFans_medal().getGuard_level());
////                            // 等级
////                            String s2 = interactWord.getFans_medal().getMedal_name();
////                            // 勋章名称
////                            String s3 = String.valueOf(interactWord.getFans_medal().getMedal_level());
////
////                            // 改造 增加[]
////                            String s11 = StringUtils.isEmpty(s1) ? "" : "[" + s1 + "]";
////                            String s12 = StringUtils.isEmpty(s2) ? "" : "[" + s2 + "]";
////                            String s13 = "0".equals(s3) ? "" : "[" + s3 + "]";
//
//                            String temp_str = addFull(interactWord.getFans_medal().getGuard_level(),
//                                    interactWord.getFans_medal().getMedal_name(),
//                                    String.valueOf(interactWord.getFans_medal().getMedal_level()),
//                                    interactWord.getUname()
//                            );
//                            log.info(temp_str + InteractWordEnum.getCountryValue(interactWord.getMsg_type()) + "了 直播间");

//                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":50,"fans_medal":{"anchor_roomid":5275,"guard_level":3,"icon_id":0,"is_lighted":1,"medal_color":398668,"medal_color_border":6809855,"medal_color_end":6850801,"medal_color_start":398668,"medal_level":28,"medal_name":"大冰棒","score":50272228,"special":"","target_id":3295},"identities":[3,1],"is_spread":0,"msg_type":2,"privilege_type":0,"roomid":27071484,"score":1730479901950,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680197673,"trigger_time":1680197671941076500,"uid":939948,"uname":"春风十里不如一路有语","uname_color":""}}

                            // 分享

//                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":10,"fans_medal":{"anchor_roomid":5275,"guard_level":3,"icon_id":0,"is_lighted":1,"medal_color":398668,"medal_color_border":6809855,"medal_color_end":6850801,"medal_color_start":398668,"medal_level":28,"medal_name":"大冰棒","score":50272228,"special":"","target_id":3295},"identities":[3,1],"is_spread":0,"msg_type":3,"privilege_type":0,"roomid":27071484,"score":1730480139740,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680197911,"trigger_time":1680197911731348500,"uid":939948,"uname":"春风十里不如一路有语","uname_color":""}}

                            // 无勋章
//                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":2,"fans_medal":{"anchor_roomid":0,"guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":0,"medal_color_border":0,"medal_color_end":0,"medal_color_start":0,"medal_level":0,"medal_name":"","score":0,"special":"","target_id":0},"identities":[1],"is_spread":0,"msg_type":1,"privilege_type":0,"roomid":24596323,"score":1680224265482,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680224265,"trigger_time":1680224264379088600,"uid":1955253870,"uname":"雷打的折耳根","uname_color":""}}

                            // 关注
//                        {"cmd":"INTERACT_WORD","data":{"contribution":{"grade":0},"core_user_type":0,"dmscore":70,"fans_medal":{"anchor_roomid":0,"guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":0,"medal_color_border":12632256,"medal_color_end":12632256,"medal_color_start":12632256,"medal_level":0,"medal_name":"","score":0,"special":"","target_id":0},"identities":[1],"is_spread":0,"msg_type":2,"privilege_type":0,"roomid":24596323,"score":1680224299551,"spread_desc":"","spread_info":"","tail_icon":0,"timestamp":1680224299,"trigger_time":1680224297542643000,"uid":333713066,"uname":"面包酱Official","uname_color":""}}

                            // 关注
//                            if (getCenterSetConf().is_follow_dm()) {
//                                msg_type = JSONObject.parseObject(jsonObject.getString("data")).getShort("msg_type");
//                                if (msg_type == 2) {
//                                    interact = JSONObject.parseObject(jsonObject.getString("data"), Interact.class);
//                                    stringBuilder.append(JodaTimeUtils.formatDateTime(System.currentTimeMillis())).append(":新的关注:")
//                                            .append(interact.getUname()).append(" 关注了直播间");
//                                    //控制台打印
//                                    if (getCenterSetConf().is_cmd()) {
//                                        System.out.println(stringBuilder.toString());
//                                        log.info(stringBuilder.toString());
//                                    }
////                                    //日志
////                                    if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                        MainConf.logString.add(stringBuilder.toString());
////                                        synchronized (MainConf.logThread) {
////                                            MainConf.logThread.notify();
////                                        }
////                                    }
//                                    //前端弹幕发送
//                                    try {
////                                        danmuWebsocket.sendMessage(WsPackage.toJson("follow", (short) 0, interact));
//                                    } catch (Exception e) {
//                                        // TODO 自动生成的 catch 块
//                                        e.printStackTrace();
//                                    }
//                                    stringBuilder.delete(0, stringBuilder.length());
//                                }
//                            }
//                            if (getCenterSetConf().getFollow().is_followThank()) {
////                                if (!MainConf.ISSHIELDFOLLOW) {
////                                    msg_type = JSONObject.parseObject(jsonObject.getString("data")).getShort("msg_type");
////                                    if (msg_type == 2) {
////                                        interact = JSONObject.parseObject(jsonObject.getString("data"), Interact.class);
////                                        try {
////                                            parseFollowSetting(interact);
////                                        } catch (Exception e) {
////                                            // TODO 自动生成的 catch 块
////                                            e.printStackTrace();
////                                        }
////                                    }
////                                }
//                            }


                            //欢迎进入直播间
//                            if (getCenterSetConf().is_welcome_all()) {
//                                msg_type = JSONObject.parseObject(jsonObject.getString("data")).getShort("msg_type");
//                                if (msg_type == 1) {
//                                    interact = JSONObject.parseObject(jsonObject.getString("data"), Interact.class);
//                                    stringBuilder.append(JodaTimeUtils.formatDateTime(System.currentTimeMillis())).append(":新的访客:")
//                                            .append(interact.getUname()).append(" 进入了直播间");
//                                    //控制台打印
////                                    if (getCenterSetConf().is_cmd()) {
////                                        System.out.println(stringBuilder.toString());
////                                        log.info(stringBuilder.toString());
////                                    }
////                                    //日志
////                                    if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                        MainConf.logString.add(stringBuilder.toString());
////                                        synchronized (MainConf.logThread) {
////                                            MainConf.logThread.notify();
////                                        }
////                                    }
//                                    //前端显示
//                                    //						try {
//                                    //							danmuWebsocket.sendMessage(WsPackage.toJson("welcome", (short)0, interact));
//                                    //						} catch (Exception e) {
//                                    //							// TODO 自动生成的 catch 块
//                                    //							e.printStackTrace();
//                                    //						}
//                                    stringBuilder.delete(0, stringBuilder.length());
//                                }
//                            }


//                            if (getCenterSetConf().getWelcome().is_welcomeThank()) {
//                                if (!MainConf.ISSHIELDWELCOME) {
//                                    msg_type = JSONObject.parseObject(jsonObject.getString("data")).getShort("msg_type");
//                                    if (msg_type == 1) {
//                                        interact = JSONObject.parseObject(jsonObject.getString("data"), Interact.class);
//                                        try {
//                                            parseWelcomeSetting(interact);
//                                        } catch (Exception e) {
//                                            // TODO 自动生成的 catch 块
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            }

                            //打印测试用
//                            msg_type = JSONObject.parseObject(jsonObject.getString("data")).getShort("msg_type");
//                            if (msg_type != 3 && msg_type != 2) {
//                                LOGGER.info("直播间信息:::" + message);
//                            }
                            break;
                        // 礼物bag bot
                        case "GIFT_BAG_DOT":
                            //					LOGGER.info("礼物bag" + message);
                            break;
                        case "ONLINERANK":
                            //					LOGGER.info("新在线排名更新信息推送:::" + message);
                            break;
                        case "ONLINE_RANK_COUNT":
//                            log.info("在线排名人数更新信息推送:::" + message);
                            // todo 收集即可
                            // 收集在线人数
                            // {"cmd":"ONLINE_RANK_COUNT","data":{"count":13}}
                            break;
                        case "ONLINE_RANK_V2":
//                            log.info("在线排名v2版本信息推送(即高能榜:::" + message);
                            // todo 收集即可
                            // (ParseMessageThread.java:1244) : 在线排名v2版本信息推送(即高能榜:::{"cmd":"ONLINE_RANK_V2","data":{"list":[{"uid":14286,"face":"http://i0.hdslb.com/bfs/face/b497598a1bec9881cfa563e19f0dbdfce1d74d16.jpg","score":"9470","uname":"幻觉天使","rank":1,"guard_level":2},{"uid":401033671,"face":"https://i1.hdslb.com/bfs/face/b68e53a7dbdb00e57e6860308ac20da961c26b35.jpg","score":"422","uname":"黛黛的毛得怪","rank":2,"guard_level":3},{"uid":3158250,"face":"https://i0.hdslb.com/bfs/face/5310311f808eee3984502f4d84aec9872fb66d1d.jpg","score":"391","uname":"三石叠成磊","rank":3,"guard_level":3},{"uid":319181307,"face":"http://i0.hdslb.com/bfs/face/d23cee137f55bc1192cdab177afa9900f074f5f3.jpg","score":"21","uname":"风口浪尖而非","rank":4,"guard_level":0},{"uid":939948,"face":"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg","score":"18","uname":"春风十里不如一路有语","rank":5,"guard_level":0},{"uid":327373066,"face":"https://i1.hdslb.com/bfs/face/36608388d959f793437fd3da1e64563bd09cfa51.jpg","score":"12","uname":"白鸽今天又咕啦","rank":6,"guard_level":0},{"uid":524085837,"face":"http://i0.hdslb.com/bfs/face/d1cdf49634e97ea4a495f90dc67e2d7316a3eb4e.jpg","score":"10","uname":"冰箱的主人o","rank":7,"guard_level":0}],"rank_type":"gold-rank"}}
                            break;
                        case "ONLINE_RANK_TOP3":
//                            log.info("在线排名前三信息推送(即高能榜:::" + message);
                            // todo 收集即可
                            // **成为榜 一/二/三


//                        {"cmd":"ONLINE_RANK_TOP3","data":{"dmscore":112,"list":[{"msg":"恭喜 \u003c%面包酱Official%\u003e 成为高能用户","rank":2}]}}

                            break;
                        case "HOT_RANK_CHANGED":
                            //					LOGGER.info("热门榜推送:::" + message);
                            break;
                        case "HOT_RANK_CHANGED_V2":
                            //					LOGGER.info("热门榜v2版本changed推送:::" + message);
                            break;
                        case "HOT_RANK_SETTLEMENT_V2":
                            //					LOGGER.info("热门榜v2版本set推送:::" + message);
                            break;
                        case "WIDGET_BANNER":
//                            log.info("直播横幅广告推送:::" + message);


                            //直播横幅广告推送:::{"cmd":"WIDGET_BANNER","data":{"timestamp":1680846398,"widget_list":{"2":{"id":2,"title":"大乱斗","cover":"","web_cover":"","tip_text":"PK大乱斗","tip_text_color":"#A1F8FF","tip_bottom_color":"#7349D5","jump_url":"https://live.bilibili.com/activity/live-activity-battle/index.html?app_name=chaos\u0026is_live_half_webview=1\u0026hybrid_rotate_d=1\u0026hybrid_half_ui=1,3,100p,70p,0,0,0,0,12,0;2,2,375,100p,0,0,0,0,12,0;3,3,100p,70p,0,0,0,0,12,0;4,2,375,100p,0,0,0,0,12,0;5,3,100p,70p,0,0,0,0,12,0;6,3,100p,70p,0,0,0,0,12,0;7,3,100p,70p,0,0,0,0,12,0;8,3,100p,70p,0,0,0,0,12,0\u0026room_id=25914701\u0026uid=1007320348#/","url":"","stay_time":5,"site":1,"platform_in":["live","blink","live_link","web","pc_link"],"type":3,"band_id":0,"sub_key":"pk_info","sub_data":"%7B%22id%22%3A60%2C%22status%22%3A1%2C%22uid%22%3A1007320348%2C%22rank%22%3A208%2C%22score%22%3A71%2C%22season_name%22%3A%22PK%E5%A4%A7%E4%B9%B1%E6%96%97S32%E8%B5%9B%E5%AD%A3%22%2C%22season_start%22%3A1680840000%2C%22season_end%22%3A1682870399%2C%22has_battled%22%3A1%2C%22next_rank_need_score%22%3A80%2C%22pk%22%3A%7B%22level1_id%22%3A1%2C%22level1_name%22%3A%22%E9%9D%92%E9%93%9C%E8%90%8C%E6%96%B0%22%2C%22level1_img%22%3A%22https%3A%2F%2Fi0.hdslb.com%2Fbfs%2Flive%2Fbd6ca767900adbda7cd7148db06f72726bef7813.png%22%2C%22level2_count%22%3A2%2C%22level2_img%22%3A%22https%3A%2F%2Fi0.hdslb.com%2Fbfs%2Flive%2F1f8c2a959f92592407514a1afeb705ddc55429cd.png%22%2C%22is_top_level%22%3Afalse%7D%2C%22champion_info%22%3Anull%2C%22star_light_info%22%3A%7B%22threshold_limit%22%3A0%2C%22current_gold%22%3A0%2C%22current_starLights%22%3A0%2C%22max_starLights%22%3A0%2C%22platformStrIn%22%3A%5B%5D%2C%22pk_status%22%3A0%2C%22winner_status%22%3A0%7D%2C%22final_info%22%3A%7B%22status%22%3A2%2C%22result%22%3A1%2C%22duration%22%3A0%2C%22score%22%3A100%7D%2C%22top100_rank_info%22%3A%7B%22rank%22%3A0%2C%22next100_need_score%22%3A0%2C%22next10_need_score%22%3A0%2C%22next1_need_score%22%3A0%2C%22over2_need_score%22%3A0%7D%2C%22task_info%22%3A%7B%22win_task%22%3A%7B%22task_info%22%3A%7B%22need_num%22%3A10%2C%22current_num%22%3A2%2C%22status%22%3A0%7D%2C%22reward_list%22%3A%5B%7B%22name%22%3A%22%E6%B5%81%E9%87%8F%E5%8C%85%2A5%E4%B8%AA%22%2C%22pic%22%3A%22http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Flive%2F495b158d45d8c3016e22552fc846dd4934f9efcb.png%22%2C%22num%22%3A5%7D%2C%7B%22name%22%3A%22%E5%A4%A7%E4%B9%B1%E6%96%97%E4%B9%8B%E7%8E%8B%2A1%E5%A4%A9%22%2C%22pic%22%3A%22http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Flive%2F9e841ccdd17e692ade28a8c9ba1cf439ddb21e64.png%22%2C%22num%22%3A0%7D%5D%7D%2C%22comp_task%22%3A%7B%22task_info%22%3A%7B%22need_num%22%3A15%2C%22current_num%22%3A2%2C%22status%22%3A0%7D%2C%22reward_list%22%3A%5B%7B%22name%22%3A%22%E5%A4%A7%E4%B9%B1%E6%96%97%E9%AB%98%E6%89%8B%2A1%E5%A4%A9%22%2C%22pic%22%3A%22http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Flive%2F9859865b15bb8c90d2e738ad2fe618d63c71b886.png%22%2C%22num%22%3A0%7D%5D%7D%2C%22time_stamp%22%3A1680846398%2C%22is_has_task%22%3A1%2C%22task_status%22%3A0%7D%7D","is_add":true}}}}

                            break;
                        case "MESSAGEBOX_USER_MEDAL_CHANGE":
                            //					LOGGER.info("本人勋章升级推送:::" + message);
                            break;
                        case "LIVE_INTERACTIVE_GAME":
                            //					LOGGER.info("互动游戏？？？推送:::" + message);
                            break;
                        case "WATCHED_CHANGE":
                            //{"cmd":"WATCHED_CHANGE","data":{"num":184547,"text_small":"18.4万","text_large":"18.4万人看过"}}
                            MainConf.ROOM_WATCHER = JSONObject.parseObject(jsonObject.getString("data")).getLong("num");
//                            log.info("多少人观看过:::" + message);
                            // todo 只收集
//                            log.info(MainConf.ROOM_WATCHER + " 人看过:::");
                            break;
                        case "STOP_LIVE_ROOM_LIST":
                            //					LOGGER.info("直播间关闭集合推送:::" + message);
                            break;
                        case "DANMU_AGGREGATION":
                            danmuService.danmu_aggregation_Function(message);


//                            					log.info("天选时刻条件是表情推送:::" + message);
// 注意 弹幕聚合  DANMU_AGGREGATION 天选时刻条件是表情推送:::{"cmd":"DANMU_AGGREGATION","data":{"activity_identity":"4262300","activity_source":1,"aggregation_cycle":1,"aggregation_icon":"https://i0.hdslb.com/bfs/live/c8fbaa863bf9099c26b491d06f9efe0c20777721.png","aggregation_num":113,"broadcast_msg_type":0,"dmscore":144,"msg":"关注冰冰不迷路~每天看新游戏~","show_rows":1,"show_time":2,"timestamp":1682952230}}
//                            log.info("注意 弹幕聚合  DANMU_AGGREGATION 天选时刻条件是表情推送:::" + message);
//注意 弹幕聚合  DANMU_AGGREGATION 天选时刻条件是表情推送:::{"cmd":"DANMU_AGGREGATION","data":{"activity_identity":"4146038","activity_source":1,"aggregation_cycle":1,"aggregation_icon":"https://i0.hdslb.com/bfs/live/c8fbaa863bf9099c26b491d06f9efe0c20777721.png","aggregation_num":8,"broadcast_msg_type":0,"dmscore":144,"msg":"好耶","show_rows":1,"show_time":2,"timestamp":1680861713}}
                            break;
                        case "COMMON_NOTICE_DANMAKU":
//                            log.info("警告信息推送（例如任务快完成之类的）:::" + message);
//消息为{"cmd":"COMMON_NOTICE_DANMAKU","data":{"content_segments":[{"font_color":"#FB7299","text":"我方主播在绝杀时刻领先对手1000乱斗值，触发绝杀！","type":1}],"dmscore":144,"terminals":[1,2,3,4,5]}}
//                            警告信息推送（例如任务快完成之类的）:::{"cmd":"COMMON_NOTICE_DANMAKU","data":{"content_segments":[{"font_color":"#FB7299","highlight_font_color":"#FCA622","text":"本场PK大乱斗我方获胜！感谢\u003c%\u003c$食不食油餅阿$\u003e%\u003e为胜利做出的贡献","type":1}],"dmscore":144,"terminals":[1,2,3,4,5]}}


                            break;
                        case "POPULARITY_RED_POCKET_NEW":

                            // 红包抽奖推送:::{"cmd":"POPULARITY_RED_POCKET_NEW","data":{"lot_id":10483261,"start_time":1680952449,"current_time":1680952449,"wait_num":0,"uname":"直播小电视","uid":1407831746,"action":"送出","num":1,"gift_name":"红包","gift_id":13000,"price":600,"name_color":"","medal_info":{"target_id":0,"special":"","icon_id":0,"anchor_uname":"","anchor_roomid":0,"medal_level":0,"medal_name":"","medal_color":0,"medal_color_start":0,"medal_color_end":0,"medal_color_border":0,"is_lighted":0,"guard_level":0}}}

//                            log.info("红包抽奖推送:::" + message);

//                        {"cmd":"POPULARITY_RED_POCKET_NEW","data":{"lot_id":10483908,"start_time":1680954326,"current_time":1680953743,"wait_num":5,"uname":"手残大树","uid":89739012,"action":"送出","num":1,"gift_name":"红包","gift_id":13000,"price":20,"name_color":"#00D1F1","medal_info":{"target_id":0,"special":"","icon_id":0,"anchor_uname":"","anchor_roomid":0,"medal_level":0,"medal_name":"","medal_color":0,"medal_color_start":0,"medal_color_end":0,"medal_color_border":0,"is_lighted":0,"guard_level":0}}}
                            //{"cmd":"POPULARITY_RED_POCKET_NEW",
                            // "data":{"lot_id":8677977,"start_time":1674572461,"current_time":1674572461,
                            // "wait_num":0,"uname":"直播小电视","uid":1407831746,"action":"送出",
                            // "num":1,"gift_name":"红包","gift_id":13000,"price":5000,"name_color":"",
                            // "medal_info":{"target_id":0,"special":"","icon_id":0,"anchor_uname":"",
                            // "anchor_roomid":0,"medal_level":0,"medal_name":"","medal_color":0,"medal_color_start":0,
                            // "medal_color_end":0,"medal_color_border":0,"is_lighted":0,"guard_level":0}}}
//                            if (getCenterSetConf().is_gift()) {
//                                redPackage = JSONObject.parseObject(jsonObject.getString("data"), RedPackage.class);
//                                stringBuilder.append(JodaTimeUtils.formatDateTime(redPackage.getStart_time() * 1000));
//                                stringBuilder.append(":收到红包:");
//                                stringBuilder.append(redPackage.getUname());
//                                stringBuilder.append(" ");
//                                stringBuilder.append(redPackage.getAction());
//                                stringBuilder.append("的:");
//                                stringBuilder.append(redPackage.getGift_name());
//                                stringBuilder.append(" x ");
//                                stringBuilder.append(redPackage.getNum());
//                                //控制台打印
//                                if (getCenterSetConf().is_cmd()) {
//                                    System.out.println(stringBuilder.toString());
//                                }
//                                gift = new Gift();
//                                gift.setGiftName(redPackage.getGift_name());
//                                gift.setNum(redPackage.getNum());
//                                gift.setPrice(redPackage.getPrice());
//                                gift.setTotal_coin((long) redPackage.getNum() * redPackage.getPrice());
//                                gift.setTimestamp(redPackage.getStart_time());
//                                gift.setAction(redPackage.getAction());
//                                gift.setCoin_type((short) 1);
//                                gift.setUname(redPackage.getUname());
//                                gift.setUid(redPackage.getUid());
//                                gift.setMedal_info(redPackage.getMedal_info());
//                                try {
//                                    danmuWebsocket.sendMessage(WsPackage.toJson("gift", (short) 0, gift));
//                                } catch (Exception e) {
//                                    // TODO 自动生成的 catch 块
//                                    e.printStackTrace();
//                                }
////                                if (MainConf.logThread != null && !MainConf.logThread.FLAG) {
////                                    MainConf.logString.add(stringBuilder.toString());
////                                    synchronized (MainConf.logThread) {
////                                        MainConf.logThread.notify();
////                                    }
////                                }
//                                stringBuilder.delete(0, stringBuilder.length());
//                            }
//                            if (getCenterSetConf().getThank_gift().is_giftThank()) {
//                                if (MainConf.parsethankGiftThread != null && !MainConf.parsethankGiftThread.TFLAG) {
//                                    redPackage = JSONObject.parseObject(jsonObject.getString("data"), RedPackage.class);
//                                    gift = new Gift();
//                                    gift.setGiftName(redPackage.getGift_name());
//                                    gift.setNum(redPackage.getNum());
//                                    gift.setPrice(redPackage.getPrice());
//                                    gift.setTotal_coin((long) redPackage.getNum() * redPackage.getPrice());
//                                    gift.setTimestamp(redPackage.getStart_time());
//                                    gift.setAction(redPackage.getAction());
//                                    gift.setCoin_type((short) 1);
//                                    gift.setUname(redPackage.getUname());
//                                    gift.setUid(redPackage.getUid());
//                                    gift.setMedal_info(redPackage.getMedal_info());
//                                    gift = ShieldGiftTools.shieldGift(gift,
//                                            ParseSetStatusTools.getListGiftShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_gift_shield_status()),
//                                            ParseSetStatusTools.getListPeopleShieldStatus(
//                                                    getCenterSetConf().getThank_gift().getList_people_shield_status()),
//                                            ParseSetStatusTools.getGiftShieldStatus(getCenterSetConf().getThank_gift().getShield_status()),
//                                            getCenterSetConf().getThank_gift().getGiftStrings(), null);
//                                    if (gift != null) {
//                                        try {
//                                            parseGiftSetting(gift);
//                                        } catch (Exception e) {
//                                            // TODO 自动生成的 catch 块
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            }
                            //					LOGGER.info("红包赠送:::" + message);
                            break;
                        case "POPULARITY_RED_POCKET_WINNER_LIST":
//                            log.info("红包抽奖结果推送:::" + message);
                            // 红包抽奖结果推送:::{"cmd":"POPULARITY_RED_POCKET_WINNER_LIST","data":{"lot_id":10483261,"total_num":46,"winner_info":[[382169855,"這都幾點啦",5536553,31215],[26563730,"慈母手中剑游子身上扎",5547574,30971],[592298660,"200虫",5547326,30971],[5579618,"xiaobaizhi",5558028,30971],[3493110071954202,"拜伦伦拜",5553877,30971],[11260471,"笑叔家的次元魂",5498704,30971],[381835230,"暮影晓白",5547576,30971],[36224080,"寶貝貝",5547575,30971],[87577734,"华樱白猫",5503642,30971],[359746905,"GAI-Alone",5536554,30971],[349251604,"无铭智者龘龘龘龘龘",5503867,31278],[2994493,"年年の柴宝",5549049,31278],[504313725,"用名字长来引起人注意",5536324,31278],[3968797,"不忆的阿凌",5493011,31278],[1454342769,"IamMI_awa",5525245,31278],[334425358,"京东大会员",5558029,31278],[504738866,"可爱的可莹",5569751,31278],[12304672,"正经的五月",5553878,31278],[245888157,"醉酒暖茶-世一欧",5493010,31278],[253789290,"兰州挂面",5547287,31278],[332869200,"孤独回家天",5547577,31278],[324365756,"清影醉是兔免吖",5569720,31278],[241054561,"天上院幻雪",5498962,31278],[15798212,"彤彤的十六年铁粉",5553879,31278],[1233162176,"月影入画vci",5569715,31278],[697632781,"那个好吃这个也好吃w",5498961,31278],[75463100,"小短腿不舒服斯基",5547283,31278],[6046036,"无前Namako",5566534,31278],[555871534,"以己之心待他人",5503868,31278],[403319092,"ZHJ赵浙",5553880,31278],[489885564,"瓈筱",5503869,31278],[43109537,"衣同_",5488966,31278],[17283628,"喝水打豆豆吃饭",5557104,31278],[27856887,"刹二豹独家认证轧钢厂",5493012,31278],[88145446,"苍炎真司",5569976,31278],[179808226,"想吃芒果的Rinko酱",5569716,31278],[524345660,"爱吃桃桃的胡桃",5547327,31278],[3493271846259246,"嘉嘉-宝贝吖",5569977,31278],[26264583,"君敏丶",5548784,31278],[1379568084,"涵包蛋QWQ",5503870,31278],[900317,"僭语",5487399,31278],[1855543390,"老足球迷胖胖大叔",5543130,31278]],"awards":{"30971":{"award_type":1,"award_name":"这个好诶","award_pic":"https://s1.hdslb.com/bfs/live/9260c680959428c45b3a2742e42ea7ae75e457ef.png","award_big_pic":"https://i0.hdslb.com/bfs/live/fc69ce781aae94ef0629b68b1d650a3a837086be.png","award_price":1000},"31215":{"award_type":1,"award_name":"花式夸夸","award_pic":"https://s1.hdslb.com/bfs/live/28186596880db45a7b843f17d6ebb70feeac06f9.png","award_big_pic":"https://i0.hdslb.com/bfs/live/86927942c0c32f49bce65cc98f37fca90a0b2e67.png","award_price":33000},"31278":{"award_type":1,"award_name":"打call","award_pic":"https://s1.hdslb.com/bfs/live/b1be22bf5843b6d1164683233bf35947714118bb.png","award_big_pic":"https://i0.hdslb.com/bfs/live/d31a129b858c1853f0bc588096d1ed313293c30a.png","award_price":500}},"version":1}}
//                        {"cmd":"POPULARITY_RED_POCKET_WINNER_LIST","data":{"lot_id":10483521,"total_num":8,"winner_info":[[592298660,"200虫",5547731,31212],[555871534,"以己之心待他人",5500791,31212],[26563730,"慈母手中剑游子身上扎",5547732,31214],[2994493,"年年の柴宝",5549205,31214],[87577734,"华樱白猫",5504005,31214],[1233162176,"月影入画vci",5570145,31216],[26631012,"乃琳喵提不起劲_",5554045,31216],[403319092,"ZHJ赵浙",5554046,31216]],"awards":{"31212":{"award_type":1,"award_name":"打call","award_pic":"https://s1.hdslb.com/bfs/live/461be640f60788c1d159ec8d6c5d5cf1ef3d1830.png","award_big_pic":"https://i0.hdslb.com/bfs/live/9e6521c57f24c7149c054d265818d4b82059f2ef.png","award_price":500},"31214":{"award_type":1,"award_name":"牛哇","award_pic":"https://s1.hdslb.com/bfs/live/91ac8e35dd93a7196325f1e2052356e71d135afb.png","award_big_pic":"https://i0.hdslb.com/bfs/live/3b74c117b4f265edcea261bc5608a58d3a7c300a.png","award_price":100},"31216":{"award_type":1,"award_name":"小花花","award_pic":"https://s1.hdslb.com/bfs/live/5126973892625f3a43a8290be6b625b5e54261a5.png","award_big_pic":"https://i0.hdslb.com/bfs/live/cf90eac49ac0df5c26312f457e92edfff266f3f1.png","award_price":100}},"version":1}}
                            break;
                        case "LIKE_INFO_V3_UPDATE":
//                            					LOGGER.info("点赞信息v3推送:::" + message);
                            //{"cmd":"LIKE_INFO_V3_UPDATE","data":{"click_count":371578}}
//                            MainConf.ROOM_LIKE = JSONObject.parseObject(jsonObject.getString("data")).getLong("click_count");
//
//                            log.info("点赞消息LIKE_INFO_V3_UPDATE推送");
//                            log.info(message);

                            // 推送 总点赞次数
//                        {"cmd":"LIKE_INFO_V3_UPDATE","data":{"click_count":13}}

                            MainConf.ROOM_CLICK = JSONObject.parseObject(jsonObject.getString("data")).getLong("click_count");


                            break;
                        case "LIKE_INFO_V3_CLICK":
                            danmuService.LikeFunction(message);
//
////                            log.info("点赞消息LIKE_INFO_V3_CLICK推送");
////                            log.info(message);
//                            String like_info_data = JSONObject.parseObject(message).getString("data");
//                            LikeInfo likeInfo = JSONObject.parseObject(like_info_data, LikeInfo.class);
////
////                            log.info(likeInfo.getUname() + "点赞了");
////                            log.info("勋章级别" + GuardLevelEnum.getCountryValue(likeInfo.getFans_medal().getGuard_level())
////                                    + "勋章牌子名称" + likeInfo.getFans_medal().getMedal_name()
////                                    + " 勋章等级" + likeInfo.getFans_medal().getMedal_level()
////
////                            );
//
//                            String temp_stt = addFull(likeInfo.getFans_medal().getGuard_level(), likeInfo.getFans_medal().getMedal_name(), String.valueOf(likeInfo.getFans_medal().getMedal_level()),
//                                    likeInfo.getUname());
//                            log.info(temp_stt + "点赞了");
//
//                            // 点赞推送
////                            danmuWebsocket.sendMessage(DanmuDto.toJson("emoticon", likeInfo));
//
////                        {"cmd":"LIKE_INFO_V3_CLICK","data":{"show_area":1,"msg_type":6,"like_icon":"https://i0.hdslb.com/bfs/live/23678e3d90402bea6a65251b3e728044c21b1f0f.png","uid":939948,"like_text":"为主播点赞了","uname":"春风十里不如一路有语","uname_color":"","identities":[3,1],"fans_medal":{"target_id":3295,"medal_level":28,"medal_name":"大冰棒","medal_color":398668,"medal_color_start":398668,"medal_color_end":6850801,"medal_color_border":6809855,"is_lighted":1,"guard_level":3,"special":"","icon_id":0,"anchor_roomid":0,"score":50272228},"contribution_info":{"grade":0},"dmscore":20}}
//

                            //					LOGGER.info("点赞信息v3推送:::" + message);
                            break;
                        case "CORE_USER_ATTENTION":
                            //					LOGGER.info("中心用户推送:::" + message);
                            break;
                        case "HOT_RANK_SETTLEMENT":
                            //					LOGGER.info("热榜排名推送:::" + message);
                            break;
                        case "MESSAGEBOX_USER_GAIN_MEDAL":
                            //					LOGGER.info("粉丝勋章消息盒子推送:::" + message);
                            break;
                        case "POPULARITY_RED_POCKET_START":
//                            log.info("礼物推送:::" + message);
//                        {"cmd":"POPULARITY_RED_POCKET_START","data":{"lot_id":11498667,"sender_uid":1407831746,"sender_name":"直播小电视","sender_face":"http://i2.hdslb.com/bfs/face/72c99193ee2c32f14b7b60711ec4c2ce2eced60c.jpg","join_requirement":1,"danmu":"老板大气！点点红包抽礼物！","current_time":1684595079,"start_time":1684595078,"end_time":1684595258,"last_time":180,"remove_time":1684595273,"replace_time":1684595268,"lot_status":1,"h5_url":"https://live.bilibili.com/p/html/live-app-red-envelope/popularity.html?is_live_half_webview=1\u0026hybrid_half_ui=1,5,100p,100p,000000,0,50,0,0,1;2,5,100p,100p,000000,0,50,0,0,1;3,5,100p,100p,000000,0,50,0,0,1;4,5,100p,100p,000000,0,50,0,0,1;5,5,100p,100p,000000,0,50,0,0,1;6,5,100p,100p,000000,0,50,0,0,1;7,5,100p,100p,000000,0,50,0,0,1;8,5,100p,100p,000000,0,50,0,0,1\u0026hybrid_rotate_d=1\u0026hybrid_biz=popularityRedPacket\u0026lotteryId=11498667","user_status":2,"awards":[{"gift_id":30971,"gift_name":"这个好诶","gift_pic":"https://s1.hdslb.com/bfs/live/9260c680959428c45b3a2742e42ea7ae75e457ef.png","num":2},{"gift_id":31278,"gift_name":"打call","gift_pic":"https://s1.hdslb.com/bfs/live/b1be22bf5843b6d1164683233bf35947714118bb.png","num":1},{"gift_id":31225,"gift_name":"牛哇牛哇","gift_pic":"https://s1.hdslb.com/bfs/live/91ac8e35dd93a7196325f1e2052356e71d135afb.png","num":5}],"lot_config_id":-1,"total_price":3000,"wait_num":0}}
                            // todp待抓
                            break;
                        case "LITTLE_MESSAGE_BOX":
                            //					LOGGER.info("小消息box推送:::" + message);
                            break;
                        case "ANCHOR_HELPER_DANMU":
                            //					LOGGER.info("直播小助手信息推送:::" + message);
                            break;
                        case "ENTRY_EFFECT_MUST_RECEIVE":
                            //					LOGGER.info("直播小助手信息推送:::" + message);
                            break;
                        case "GIFT_STAR_PROCESS":
                            //					LOGGER.info("礼物开始进度条信息推送:::" + message);
                            break;
                        case "GUARD_HONOR_THOUSAND":
                            //					LOGGER.info("千舰推送:::" + message);
                            break;
                        case "FULL_SCREEN_SPECIAL_EFFECT":
//                            log.info("FULL_SCREEN_SPECIAL_EFFECT:::" + message);
//                            FULL_SCREEN_SPECIAL_EFFECT:::{"cmd":"FULL_SCREEN_SPECIAL_EFFECT","data":{"type":2,"ids":[1045],"queue":2,"platform_in":[1,2]}}
                            break;
                        case "CARD_MSG":
                            //					LOGGER.info("CARD_MSG:::" + message);
                            break;
                        case "USER_PANEL_RED_ALARM":
                            //					LOGGER.info("USER_PANEL_RED_ALARM:::" + message);
                            break;
                        case "TRADING_SCORE":
                            //					LOGGER.info("TRADING_SCORE:::" + message);
                            break;
                        case "USER_TASK_PROGRESS":
                            //					LOGGER.info("USER_TASK_PROGRESS:::" + message);
                            break;
                        case "POPULAR_RANK_CHANGED":
                            //					LOGGER.info("POPULAR_RANK_CHANGED:::" + message);   // {"cmd":"POPULAR_RANK_CHANGED","data":{"uid":477482558,"rank":98,"countdown":2863,"timestamp":1682377938,"cache_key":"rank_change:3911d21b38134d38970eacaa6d4b5b20"}}
                            break;
                        case "AREA_RANK_CHANGED":
                            // 分区榜单排名地址
//                            log.info("AREA_RANK_CHANGED:::" + message);
                            // todo 只收集

                            //"cmd":"AREA_RANK_CHANGED","data":{"conf_id":23,"rank_name":"手游航海","uid":3295,"rank":22,"icon_url_blue":"https://i0.hdslb.com/bfs/live/18e2990a546d33368200f9058f3d9dbc4038eb5c.png","icon_url_pink":"https://i0.hdslb.com/bfs/live/a6c490c36e88c7b191a04883a5ec15aed187a8f7.png","icon_url_grey":"https://i0.hdslb.com/bfs/live/cb7444b1faf1d785df6265bfdc1fcfc993419b76.png","action_type":1,"timestamp":1680369690,"msg_id":"3451d86e-279f-433f-bc01-6c30451ff557","jump_url_link":"https://live.bilibili.com/p/html/live-app-hotrank/index.html?clientType=3\u0026ruid=3295\u0026conf_id=23\u0026is_live_half_webview=1\u0026hybrid_rotate_d=1\u0026is_cling_player=1\u0026hybrid_half_ui=1,3,100p,70p,f4eefa,0,30,100,0,0;2,2,375,100p,f4eefa,0,30,100,0,0;3,3,100p,70p,f4eefa,0,30,100,0,0;4,2,375,100p,f4eefa,0,30,100,0,0;5,3,100p,70p,f4eefa,0,30,100,0,0;6,3,100p,70p,f4eefa,0,30,100,0,0;7,3,100p,70p,f4eefa,0,30,100,0,0;8,3,100p,70p,f4eefa,0,30,100,0,0#/area-rank","jump_url_pc":"https://live.bilibili.com/p/html/live-app-hotrank/index.html?clientType=4\u0026ruid=3295\u0026conf_id=23\u0026pc_ui=338,465,f4eefa,0#/area-rank","jump_url_pink":"https://live.bilibili.com/p/html/live-app-hotrank/index.html?clientType=1\u0026ruid=3295\u0026conf_id=23\u0026is_live_half_webview=1\u0026hybrid_rotate_d=1\u0026hybrid_half_ui=1,3,100p,70p,ffffff,0,30,100,12,0;2,2,375,100p,ffffff,0,30,100,0,0;3,3,100p,70p,ffffff,0,30,100,12,0;4,2,375,100p,ffffff,0,30,100,0,0;5,3,100p,70p,ffffff,0,30,100,0,0;6,3,100p,70p,ffffff,0,30,100,0,0;7,3,100p,70p,ffffff,0,30,100,0,0;8,3,100p,70p,ffffff,0,30,100,0,0#/area-rank","jump_url_web":"https://live.bilibili.com/p/html/live-app-hotrank/index.html?clientType=2\u0026ruid=3295\u0026conf_id=23#/area-rank"}}
                            break;
                        default:
//                            LOGGER.info("其他未处理消息:" + message);
                            break;
                    }
                    MainConf.resultStrs.remove(0);
                } else {
                    synchronized (MainConf.parseMessageThread) {
                        try {
                            MainConf.parseMessageThread.wait();
                        } catch (InterruptedException e) {
                            // TODO 自动生成的 catch 块
                            //						LOGGER.info("处理弹幕包信息线程关闭:" + e);
                            //						e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    //获取发送礼物code
    public String sendCode(short guardLevel) {
//        String code = CurrencyTools.sendGiftCode(guardLevel);
//        CenterSetConf centerSetConf= CurrencyTools.codeRemove(code);
//        setService.changeSet(centerSetConf, true);
//        return code;
        return null;
    }

//    public boolean parseAutoReplySetting(Barrage barrage) {
//        ListPeopleShieldStatus listPeopleShieldStatus = ParseSetStatusTools.getListPeopleShieldStatus(getCenterSetConf().getReply().getList_people_shield_status());
//        //先人员
//        switch (listPeopleShieldStatus) {
////			case ALL:
////				break;
//            case MEDAL:
//                if (MainConf.MEDALINFOANCHOR != null) {
//                    if (StringUtils.isBlank(MainConf.MEDALINFOANCHOR.getMedal_name())) {
//                        break;
//                    }
//                    //舰长的这里是空的
//                    if (barrage.getMedal_name() == null) {
//                        break;
//                    }
//                    if (!MainConf.MEDALINFOANCHOR.getMedal_name().equals(barrage.getMedal_name())) {
//                    //    LOGGER.info("自动回复姬人员屏蔽[勋章模式]:{}", barrage.getMedal_name());
//                        return false;
//                    }
//                }
//                break;
//            case GUARD:
//                if (barrage.getUguard() <= 0) {
//               //     LOGGER.info("自动回复姬人员屏蔽[舰长模式]:{}", ParseIndentityTools.parseGuard(barrage.getUguard()));
//                    return false;
//                }
//            default:
//                break;
//        }
//        return true;
//    }


//    public void DelayGiftTimeSetting() {
//        synchronized (MainConf.parsethankGiftThread) {
//            if (MainConf.parsethankGiftThread != null) {
//                threadComponent.startParseThankGiftThread(getCenterSetConf().getThank_gift(), getThankGiftRuleSets());
////				if (MainConf.parsethankGiftThread.getState().toString().equals("TERMINATED")
////						|| MainConf.parsethankGiftThread.getState().toString().equals("NEW")) {
////					MainConf.parsethankGiftThread = new ParseThankGiftThread();
////					MainConf.parsethankGiftThread
////							.setDelaytime((long) (1000 * getThankGiftSetConf().getDelaytime()));
////					MainConf.parsethankGiftThread.start();
////					MainConf.parsethankGiftThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankGiftThread.setThankGiftString(getThankGiftSetConf().getThank());
////					MainConf.parsethankGiftThread.setThankGiftStatus(
////							ParseSetStatusTools.getThankGiftStatus(getThankGiftSetConf().getThank_status()));
////					MainConf.parsethankGiftThread
////							.setThankGiftRuleSets(getThankGiftRuleSets());
////					MainConf.parsethankGiftThread.setNum(getThankGiftSetConf().getNum());
////					MainConf.parsethankGiftThread.setIs_num(getThankGiftSetConf().is_num());
////				} else {
////					MainConf.parsethankGiftThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankGiftThread.setThankGiftString(getThankGiftSetConf().getThank());
////					MainConf.parsethankGiftThread.setThankGiftStatus(
////							ParseSetStatusTools.getThankGiftStatus(getThankGiftSetConf().getThank_status()));
////					MainConf.parsethankGiftThread
////							.setThankGiftRuleSets(getThankGiftRuleSets());
////					MainConf.parsethankGiftThread.setNum(getThankGiftSetConf().getNum());
////					MainConf.parsethankGiftThread.setIs_num(getThankGiftSetConf().is_num());
////				}
//            }
//        }
//    }

//
//    public synchronized void parseGiftSetting(Gift gift) throws Exception {
//        Vector<Gift> gifts = null;
//        if (gift != null && !StringUtils.isEmpty(MainConf.USERCOOKIE)) {
//            if (MainConf.sendBarrageThread != null && MainConf.parsethankGiftThread != null) {
//                if (!MainConf.sendBarrageThread.FLAG && !MainConf.parsethankGiftThread.TFLAG) {
//                    if (MainConf.thankGiftConcurrentHashMap.size() > 0) {
//                        gifts = MainConf.thankGiftConcurrentHashMap.get(gift.getUname());
//                        if (gifts != null) {
//                            int flagNum = 0;
//                            for (Gift giftChild : gifts) {
//                                int num1 = giftChild.getNum();
//                                int num2 = gift.getNum();
//                                long total_coin1 = giftChild.getTotal_coin();
//                                long total_coin2 = gift.getTotal_coin();
//                                if (giftChild.getGiftName().equals(gift.getGiftName())) {
//                                    giftChild.setNum(num1 + num2);
//                                    giftChild.setTotal_coin(total_coin1 + total_coin2);
//                                    DelayGiftTimeSetting();
//                                    flagNum++;
//                                }
//                            }
//                            if (flagNum == 0) {
//                                gifts.add(gift);
//                                DelayGiftTimeSetting();
//                            }
//                        } else {
//                            gifts = new Vector<Gift>();
//                            gifts.add(gift);
//                            MainConf.thankGiftConcurrentHashMap.put(gift.getUname(), gifts);
//                            DelayGiftTimeSetting();
//                        }
//                    } else {
//                        gifts = new Vector<Gift>();
//                        gifts.add(gift);
//                        MainConf.thankGiftConcurrentHashMap.put(gift.getUname(), gifts);
//                        DelayGiftTimeSetting();
//                    }
//                }
//            }
//        }
//    }


//
//    public void DelayFollowTimeSetting() {
//        synchronized (MainConf.parsethankFollowThread) {
//            if (MainConf.parsethankFollowThread != null) {
//                threadComponent.startParseThankFollowThread(getCenterSetConf().getFollow());
////				if (MainConf.parsethankFollowThread.getState().toString().equals("TERMINATED")
////						|| MainConf.parsethankFollowThread.getState().toString().equals("NEW")) {
////					MainConf.parsethankFollowThread = new ParseThankFollowThread();
////					MainConf.parsethankFollowThread
////							.setDelaytime((long) (1000 * getThankFollowSetConf().getDelaytime()));
////					MainConf.parsethankFollowThread.start();
////					MainConf.parsethankFollowThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankFollowThread.setThankFollowString(getThankFollowSetConf().getFollows());
////					MainConf.parsethankFollowThread.setNum(getThankFollowSetConf().getNum());
////				} else {
////					MainConf.parsethankFollowThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankFollowThread.setThankFollowString(getThankFollowSetConf().getFollows());
////					MainConf.parsethankFollowThread.setNum(getThankFollowSetConf().getNum());
////				}
//            }
//        }
//    }
//
//    public synchronized void parseFollowSetting(Interact interact) throws Exception {
//        if (interact != null && !StringUtils.isEmpty(MainConf.USERCOOKIE)) {
//            if (MainConf.sendBarrageThread != null && MainConf.parsethankFollowThread != null) {
//                if (!MainConf.sendBarrageThread.FLAG && !MainConf.parsethankFollowThread.FLAG) {
//                    MainConf.interacts.add(interact);
//                    DelayFollowTimeSetting();
//                }
//            }
//        }
//    }
//
//    public void DelayWelcomeTimeSetting() {
//        synchronized (MainConf.parseThankWelcomeThread) {
//            if (MainConf.parseThankWelcomeThread != null) {
//                threadComponent.startParseThankWelcomeThread(getCenterSetConf().getWelcome());
////				if (MainConf.parsethankFollowThread.getState().toString().equals("TERMINATED")
////						|| MainConf.parsethankFollowThread.getState().toString().equals("NEW")) {
////					MainConf.parsethankFollowThread = new ParseThankFollowThread();
////					MainConf.parsethankFollowThread
////							.setDelaytime((long) (1000 * getThankFollowSetConf().getDelaytime()));
////					MainConf.parsethankFollowThread.start();
////					MainConf.parsethankFollowThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankFollowThread.setThankFollowString(getThankFollowSetConf().getFollows());
////					MainConf.parsethankFollowThread.setNum(getThankFollowSetConf().getNum());
////				} else {
////					MainConf.parsethankFollowThread.setTimestamp(System.currentTimeMillis());
////					MainConf.parsethankFollowThread.setThankFollowString(getThankFollowSetConf().getFollows());
////					MainConf.parsethankFollowThread.setNum(getThankFollowSetConf().getNum());
////				}
//            }
//        }
//    }
//
//    public synchronized void parseWelcomeSetting(Interact interact) throws Exception {
//        if (interact != null && !StringUtils.isEmpty(MainConf.USERCOOKIE)) {
//            //屏蔽设定
//            ListPeopleShieldStatus listPeopleShieldStatus = ParseSetStatusTools.getListPeopleShieldStatus(getCenterSetConf().getWelcome().getList_people_shield_status());
//            //先人员
//            switch (listPeopleShieldStatus) {
////			case ALL:
////				break;
//                case MEDAL:
//                    if (MainConf.MEDALINFOANCHOR != null) {
//                        if (StringUtils.isBlank(MainConf.MEDALINFOANCHOR.getMedal_name())) {
//                            break;
//                        }
//                        //舰长的这里是空的
//                        if (interact.getFans_medal() == null) {
//                            break;
//                        }
//                        if (!MainConf.MEDALINFOANCHOR.getMedal_name().equals(interact.getFans_medal().getMedal_name())) {
////                           LOGGER.info("欢迎姬人员屏蔽[勋章模式]:{}", interact.getFans_medal().getMedal_name());
//                            return;
//                        }
//                    }
//                    break;
//                case GUARD:
//                    if (interact.getFans_medal().getGuard_level() <= 0) {
////                        LOGGER.info("欢迎姬人员屏蔽[舰长模式]:{}", ParseIndentityTools.parseGuard(interact.getFans_medal().getGuard_level()));
//                        return;
//                    }
//                default:
//                    break;
//            }
//            if (MainConf.sendBarrageThread != null && MainConf.parseThankWelcomeThread != null) {
//                if (!MainConf.sendBarrageThread.FLAG && !MainConf.parseThankWelcomeThread.FLAG) {
//                    MainConf.interactWelcome.add(interact);
//                    DelayWelcomeTimeSetting();
//                }
//            }
//        }
//    }

    public CenterSetConf getCenterSetConf() {
        if (centerSetConf == null) return MainConf.centerSetConf;
        return centerSetConf;
    }

    public void setCenterSetConf(CenterSetConf centerSetConf) {
        this.centerSetConf = centerSetConf;
    }

//    public HashSet<ThankGiftRuleSet> getThankGiftRuleSets() {
//        return thankGiftRuleSets;
//    }

//    public void setThankGiftRuleSets(HashSet<ThankGiftRuleSet> thankGiftRuleSets) {
//        this.thankGiftRuleSets = thankGiftRuleSets;
//    }

    /**
     * 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     * format = "yyyy-MM-dd HH:mm:ss"
     *
     * @param str_num 时间
     * @param format 格式
     * @return 转换后时间
     */
    public static String timestamp2Date(String str_num, String format) {
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
}
