package com.uid939948.Service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.*;
import com.uid939948.DO.FansMember.FansMemberInfo;
import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.DO.temp.FacePicture;
import com.uid939948.Enuns.LiveStateEnum;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.Service.ClientService;
import com.uid939948.Service.SetService;
import com.uid939948.Tools.HandleWebsocketPackage;
import com.uid939948.Until.ByteUtils;
import com.uid939948.WebSocketClient.WebSocketProxy;
import com.uid939948.component.ThreadComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private ThreadComponent threadComponent;

    private SetService setService;

    @Override
    public void startConnService(long roomId) throws Exception {

        // 第一步 获取房间号，并且获取房间信息
        // 1、 房间号长号  websocket地址 uid 主播名称
        // 2、 如果已经登录 确认是否是房管， 获取最大输入长度（用于弹幕姬输入）
        // 3、 开始连接websocket 发现送心跳包，并且启动发送心跳包的线程
        // 4、 在websocket 里面收到消息并且回复

        if (ObjectUtil.isEmpty(roomId) || roomId < 1) {
            return;
        }

        if (ObjectUtil.isNotEmpty(MainConf.webSocketProxy) && MainConf.webSocketProxy.isOpen()) {
            MainConf.webSocketProxy.close();
            threadComponent.closeHeartByteThread();
            threadComponent.closeHeartOnlineThread();
            MainConf.ROOM_CLICK = 0L;
            MainConf.ROOM_WATCHER = 0L;
            log.info("关闭webSocketProxy 重新打开");
        }

        // 1、 获取房间基本信息
        RoomInit roomInit = HttpRoomUtil.GetRoomInit(roomId);
        MainConf.UID = roomInit.getUid();

        if (roomInit.getShort_id() > 0) {
            MainConf.SHORTROOMID = roomInit.getShort_id();
            MainConf.ROOMID = roomInit.getRoom_id();
        } else {
            MainConf.ROOMID = roomInit.getRoom_id();
        }
        log.info("房间号" + roomInit.getRoom_id() + " 房间的 uid " + roomInit.getUid() +
                " 开播状态 " + LiveStateEnum.getCountryValue(roomInit.getLive_status()));

        // 2、 获取 websocket地址  使用随机地址
        // todo userCookie 待完成   使用登陆后的toke 会获取更多websocket地址
        WebSocketAddress webSocketAddress = HttpRoomUtil.getDanmuInfo(MainConf.ROOMID, "");
//        log.info("webSocketAddress");
//        log.info(webSocketAddress + "");
//        log.info("webSocketAddress 的数量为 " + webSocketAddress.getHost_list().size());
        webSocketAddress.getHost_list().forEach(mo -> {
//            log.info(mo.getHost());
        });
        HostServer hostServer = webSocketAddress.getHost_list()
                .stream().findAny().orElse(new HostServer("wss://broadcastlv.chat.bilibili.com:2245/sub", 0, 0, 0));
        MainConf.URL = HostServer.getWsUrl(hostServer);
        Room room = HttpRoomUtil.httpGetRoomData(MainConf.ROOMID);
        // 3、拼接开始连接的数据
        FristSecurityData fristSecurityData = null;
        // todo 登录的话 需要传UID
        fristSecurityData = new FristSecurityData(MainConf.ROOMID, webSocketAddress.getToken());

        byte[] byte_1 = HandleWebsocketPackage.BEhandle(BarrageHeadHandle.getBarrageHeadHandle(
                fristSecurityData.toJson().getBytes().length + MainConf.packageHeadLength,
                MainConf.packageHeadLength, MainConf.packageVersion, MainConf.firstPackageType,
                MainConf.packageOther));
        byte[] byte_2 = fristSecurityData.toJson().getBytes();
        byte[] req = ByteUtils.byteMerger(byte_1, byte_2);

        MainConf.webSocketProxy = new WebSocketProxy(MainConf.URL, room);
        MainConf.webSocketProxy.send(req);
        MainConf.webSocketProxy.send(HexUtils.fromHexString(MainConf.heartByte));

        // 4、启动心跳线程
        // 开启心跳线程 定期发送心跳
        threadComponent.startHeartByteThread();
        threadComponent.startHeartOnlineThread();

        // 保存配置 并且 开启读取弹幕的线程
        setService.holdSet(MainConf.centerSetConf);

        // 在这里查询一次用户信息，并且保存到缓存里面
//        MainConf.userInfo = HttpRoomUtil.httpGetUserInfo(roomInit.getUid());
        MainConf.userInfo = httpGetUserInfo_V2(MainConf.ROOMID);


        // 如果重新连 也会出现 不合理
//        DanMu_MSG_Info danMuMsgInfo = new DanMu_MSG_Info();
//        danMuMsgInfo.setMessage("连接" + room.getUname() + " " + room.getRoomId() + "成功");
//        // 未携带头像 名称
//        danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));
//        https://api.live.bilibili.com/xlive/lottery-interface/v1/lottery/getLotteryInfoWeb?roomid=26313262

//        {"code":0,"message":"0","ttl":1,"data":{"pk":null,"guard":null,"gift":null,"storm":null,"silver":null,"activity_box":{"ACTIVITY_ID":0,"ACTIVITY_PIC":""},"danmu":null,"anchor":{"id":4083329,"room_id":26313262,"status":1,"asset_icon":"https://i0.hdslb.com/bfs/live/627ee2d9e71c682810e7dc4400d5ae2713442c02.png","award_name":"情书","award_num":1,"award_image":"","danmu":"开箱来了～","time":509,"current_time":1679709584,"join_type":0,"require_type":1,"require_value":0,"require_text":"关注主播","gift_id":0,"gift_name":"","gift_num":0,"gift_price":0,"cur_gift_num":0,"goaway_time":180,"award_users":null,"show_panel":1,"url":"https://live.bilibili.com/p/html/live-lottery/anchor-join.html?is_live_half_webview=1\u0026hybrid_biz=live-lottery-anchor\u0026hybrid_half_ui=1,5,100p,100p,000000,0,30,0,0,1;2,5,100p,100p,000000,0,30,0,0,1;3,5,100p,100p,000000,0,30,0,0,1;4,5,100p,100p,000000,0,30,0,0,1;5,5,100p,100p,000000,0,30,0,0,1;6,5,100p,100p,000000,0,30,0,0,1;7,5,100p,100p,000000,0,30,0,0,1;8,5,100p,100p,000000,0,30,0,0,1","lot_status":0,"web_url":"https://live.bilibili.com/p/html/live-lottery/anchor-join.html","send_gift_ensure":0,"goods_id":-99998,"award_type":1,"award_price_text":"价值52电池","ruid":3493075773032566,"asset_icon_webp":"https://i0.hdslb.com/bfs/live/b47453a0d42f30673b6d030159a96d07905d677a.webp","danmu_type":0,"danmu_new":[{"danmu":"开箱来了～","danmu_view":"","reject":false}]},"red_pocket":null,"popularity_red_pocket":null,"activity_box_info":null,"magic_wish_info":{"icon":"http://i0.hdslb.com/bfs/live/c339a3569df7351406f29afae77a917aec3073a3.png","is_show":true,"title":"魔法奇遇","type":1}}}

        // 更新礼物列表
//        MainConf.giftList = HttpRoomUtil.getGiftData(MainConf.ROOMID);

//
//        List<GiftConfigData> s1 = MainConf.giftList;
//        if (ObjectUtil.isEmpty(s1)) {
//            // todo 有giftMap时 不需要这个
//            MainConf.giftList = s1 = HttpRoomUtil.getGiftData(MainConf.ROOMID);
//        }

        // 读取礼物列表
        Map<Integer, GiftConfigData> giftMap = MainConf.giftMap;
        if (ObjectUtil.isEmpty(giftMap)) {
            // 连接房间时面部需要重复获取礼物列表，基本不会变动
            List<GiftConfigData> list = HttpRoomUtil.getGiftData(MainConf.ROOMID);
            MainConf.giftMap = list.stream()
                    .collect(Collectors.toMap(GiftConfigData::getId, v -> v, (p1, p2) -> p1));
        }

        // 测试模式弹幕
        List<String> testDanmuMessageList = new ArrayList<>();
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"非舰队发言\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"早上好 \",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"中午好\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"下午好\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"特别长的弹幕 特别长的弹幕 特别长的弹幕 特别长的弹幕 特别长的弹幕\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"嘿 今天在忙什么呢\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":false,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"初次见面\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        testDanmuMessageList.add("{\"bulge_display\":null,\"danmuColor\":\"#00D1F1\",\"emojiList\":[],\"emoticon_url\":null,\"faceUrl\":\"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg\",\"guard_level\":3,\"icon_id\":0,\"isEmoticon\":false,\"isFansMedal\":true,\"isHaveSystemEmoji\":false,\"isManager\":true,\"is_lighted\":0,\"medal_color\":0,\"medal_level\":27,\"medal_name\":\"72O\",\"message\":\"舰队发言\",\"uid\":939948,\"ul_level\":49,\"uname\":\"春风十里不如一路有语\"}");
        MainConf.testDanmuMessageList = testDanmuMessageList;

        // 通过房间号获取粉丝团的表情 加入的已有头像内，

        // 如果用户uid和粉丝团数量相等，则不需要重新获取
        List<FansMemberInfo> fansMemberInfoList = HttpRoomUtil.getFansMembersRank(roomInit.getUid());
        List<FacePicture> facePictureList = new ArrayList<>();

        for (FansMemberInfo fansMember : fansMemberInfoList) {
            FacePicture facePicture = new FacePicture();
            facePicture.setFaceUrl(fansMember.getFace());
            facePicture.setUid(fansMember.getUid());
            facePicture.setCount(1);
            facePicture.setTimestamp(System.currentTimeMillis());

            facePictureList.add(facePicture);

        }

        // 通过勋章获取的头像 uid可能有重合，需要去重 只更新头像 不更新次数

//        if(StringUtils.isEmpty())
        if (CollectionUtils.isEmpty(MainConf.facePictureList)) {

        }

        if (ObjectUtils.isEmpty(MainConf.facePictureList)) {
            MainConf.facePictureList.addAll(facePictureList);
        } else {
            // 这里属于更换房间号 后的场景,需要清空原来的房间头像
            MainConf.facePictureList = facePictureList;
//
//
//            List<FacePicture> FacePictureTemp = MainConf.facePictureList;
//            for (FacePicture facePicture : FacePictureTemp) {
//                FacePicture f1 = facePictureList.stream().filter(mo -> mo.getUid().equals(facePicture.getUid())).findAny().get();
            //
//            }
        }
    }

    /**
     * 获取用户信息
     *
     * @param roomId 房间号
     * @return 用户数据
     */
    private UserInfo httpGetUserInfo_V2(long roomId) {
        if (ObjectUtils.isEmpty(MainConf.userInfo)) {
            // 为null时为 初次登录
            log.info("初次获取UserInfo " + roomId);
            return HttpRoomUtil.httpGetUserInfoV2(roomId);
        } else {
            // 不为null时 说明已经登录过一次了，如果两次相等，则不需要重新获取
            if (MainConf.userInfo.getUid().equals(MainConf.UID)) {
                log.info("uid相同 UserInfo不需要重新获取 " + roomId);

                // todo 这里可能会很频繁
                return HttpRoomUtil.httpGetUserInfoV2(roomId);
            } else {
                log.info("uid不相同 UserInfo需要重新获取 " + roomId);
                return HttpRoomUtil.httpGetUserInfoV2(roomId);
            }
        }
    }


    @Override
    public void closeConnService() {
    }


    @Override
    public void starTestService() {
        threadComponent.startTestThread();
    }

    @Override
    public void closeTestSet() {
        threadComponent.closeTestThread();
    }

    @Autowired
    public void setSetService(SetService setService) {
        this.setService = setService;
    }

    @Autowired
    public void setThreadComponent(ThreadComponent threadComponent) {
        this.threadComponent = threadComponent;
    }
}
