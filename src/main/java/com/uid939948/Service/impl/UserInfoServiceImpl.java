package com.uid939948.Service.impl;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.Face.RoomInfo;
import com.uid939948.DO.Room;
import com.uid939948.DO.RoomInit;
import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.Enuns.LiveStateEnum;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.HttpUtil.HttpUtil;
import com.uid939948.Service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public UserInfo getUserInfo(String roomId) {
        // 通过房间号获取uid 再反查用户信息
//        if (ObjectUtils.isEmpty(MainConf.userInfo)) {
//            log.info("通过房间号获取用户信息" + roomId);
//            long l1 = Long.parseLong(roomId);
//            UserInfo userInfo = new UserInfo();
//
//            Room room = HttpRoomUtil.httpGetRoomData(MainConf.ROOMID);
//            userInfo.setUid(Long.valueOf(room.getUid()));
//            userInfo.setName(room.getUname());
//            userInfo.setRoomId(Long.valueOf(room.getRoomId()));
//
//            userInfo.setFace(HttpRoomUtil.httpGetFaceUrl_V2(Long.parseLong(room.getUid())));
//
//
//            RoomInfo roomInfo = HttpRoomUtil.httpGetRoomBaseInfo(MainConf.ROOMID);
//            userInfo.setCover(roomInfo.getCover());
//            userInfo.setTitle(roomInfo.getTitle());
//            userInfo.setLiveStatus(LiveStateEnum.getCountryValue(roomInfo.getLive_status()));
//            MainConf.userInfo = userInfo;
//            return userInfo;
//        } else {
//            if (roomId.equals(MainConf.userInfo.getRoomId() + "")) {
//                log.info(roomId + "房间号相同 不需要重复获取");
//                return MainConf.userInfo;
//            }
//            log.info(roomId + "房间号不同 重复获取");
//            long l1 = Long.parseLong(roomId);
//            RoomInit roomInit = HttpRoomUtil.GetRoomInit(l1);
//
//            UserInfo userInfo = HttpRoomUtil.httpGetUserInfo(roomInit.getUid());
//            MainConf.userInfo = userInfo;
//            return userInfo;
//        }

        return MainConf.userInfo;
    }

    @Override
    public List<GiftConfigData> getGiftData(long uid) {
        return HttpRoomUtil.getGiftData(uid);
    }

    @Override
    public List<String> getSystemEmojiList() {
        String newPath = System.getProperty("user.dir") + File.separator + "SystemEmoji";

        File f = new File(newPath);
        File[] f1 = f.listFiles();
        List<String> list = new ArrayList<>();
        for (File file : f1) {
            String name = file.getName();
            list.add(name);

        }
        System.out.println(newPath);
        System.out.println(list);

        return list;
    }


    private UserInfo httpGetUserInfo(long uid) {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
//        headers.put("referer", "https://space.bilibili.com/" + uid);
//        headers.put("cookie", "buvid3=08D352DC-DC35-6D0B-D7A6-F4580C59984D09585infoc; i-wanna-go-back=-1; _uuid=7CAC75C3-27A2-ADD7-CB54-F10B5242310A3609137infoc; FEED_LIVE_VERSION=V8; SESSDATA=a8aa63df%2C1696936030%2C488e4%2A42; bili_jct=d776d147a5bf13b53bb79c13dd62520e; DedeUserID=939948; DedeUserID__ckMd5=9518835468e33460; sid=8a5pl4lu; b_ut=5; header_theme_version=CLOSE; CURRENT_PID=23923600-d9eb-11ed-be23-97bcf7bbe66b; rpdid=|(um|JR))J~k0J'uY)uJ)R|R~; LIVE_BUVID=AUTO8716813855968636; nostalgia_conf=-1; buvid_fp_plain=undefined; hit-new-style-dyn=1; hit-dyn-v2=1; share_source_origin=QQ; bsource=search_baidu; fingerprint=1866c61b561bad20f46ded607d8975aa; bp_article_offset_939948=786134939893497900; PEA_AU=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJiaWQiOjkzOTk0OCwicGlkIjoxMDg5OTMsImV4cCI6MTcxMzQzNzY5MiwiaXNzIjoidGVzdCJ9.4iPiYPKpM62sHBYEBfnaMmD5V-i7_AdLCwAzscRaP84; home_feed_column=5; CURRENT_FNVAL=4048; b_nut=1681907299; innersign=0; browser_resolution=1536-722; b_lsid=1039CD975_1879DCEFD97; buvid_fp=44b6fa13d9548059aabf9a59543f7da9; bp_video_offset_939948=786596442720436400; _dfcaptcha=427a842a0ea68c70cab214df1990f0ec; PVID=36; buvid4=7F23CB9E-1207-096C-3093-293FE8C9ED4111559-023041319-0mOPZbinCsPmFgrzIcnxpw%3D%3D");

        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token&platform=web";

//        String url = "https://api.bilibili.com/x/space/acc/info?mid=" + uid;
        String result = HttpUtil.doGetWithHeader(url, headers);

        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");

        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        userInfo.setFace(JSONObject.parseObject(data).getString("face"));
        userInfo.setName(JSONObject.parseObject(data).getString("name"));
        userInfo.setSign(JSONObject.parseObject(data).getString("sign"));

        JSONObject liveRoom = JSONObject.parseObject(jsonObject.getString("data")).getJSONObject("live_room");

        userInfo.setTitle(liveRoom.getString("title"));
        userInfo.setCover(liveRoom.getString("cover"));
        userInfo.setLiveStatus(LiveStateEnum.getCountryValue(Integer.valueOf(liveRoom.getString("liveStatus"))));
        userInfo.setRoomId(liveRoom.getLong("roomid"));
//
//        JSONObject.parseObject(jsonObject.getString("data")).getJSONObject("live_room") = {JSONObject@3840}  size = 9
//        "cover" -> "http://i0.hdslb.com/bfs/live/new_room_cover/e2735e8e4634c8353212820fd7c45a7aac98dcbf.jpg"
//        "watched_show" -> {JSONObject@3857}  size = 7
//        "roomStatus" -> {Integer@3274} 1
//        "roundStatus" -> {Integer@3268} 0
//        "title" -> "日常敲代码"
//        "liveStatus" -> {Integer@3268} 0
//        "url" -> "https://live.bilibili.com/3961583?broadcast_type=0&is_room_feed=1"
//        "roomid" -> {Integer@3860} 3961583
//        "broadcast_type" -> {Integer@3268} 0


        return userInfo;

    }


}
