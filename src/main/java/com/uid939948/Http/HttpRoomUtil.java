package com.uid939948.Http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.uid939948.DO.Emoji.EmojiInfo;
import com.uid939948.DO.Room;
import com.uid939948.DO.RoomInit;
import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.WebSocketAddress;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.Enuns.LiveStateEnum;
import com.uid939948.HttpUtil.HttpUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRoomUtil {
    private static Logger LOGGER = LogManager.getLogger(HttpRoomUtil.class);

    /**
     * 获取房间信息
     *
     * @param roomId 房间号
     * @return 房间信息 （uid 长号短号 开播状态）
     */
    public static RoomInit GetRoomInit(long roomId) {
        RoomInit roomInit = null;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        String url = "https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomId;
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if ("0".equals(code)) {
            roomInit = jsonObject.getObject("data", RoomInit.class);
        } else {
            LOGGER.error("直播房间号不存在，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
        }
        return roomInit;
    }

    /**
     * 获取弹幕websocket地址
     *
     * @param roomId     长号
     * @param userCookie 用户cookie (可为空)
     */
    public static WebSocketAddress getDanmuInfo(long roomId, String userCookie) {
        WebSocketAddress webSocketAddress = null;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        if (!StringUtils.isBlank(userCookie)) {
            headers.put("cookie", userCookie);
        }
        headers.put("referer", "https://live.bilibili.com/" + roomId);

        String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo?id=" + roomId + "&type=0";
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if ("0".equals(code)) {
            webSocketAddress = jsonObject.getObject("data", WebSocketAddress.class);
        } else {
            LOGGER.error("获取弹幕websocket地址，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
        }
        return webSocketAddress;
    }

    /**
     * 获取房间信息
     *
     * @param roomId 房间ID
     * @return 房间新
     */
    public static Room httpGetRoomData(long roomId) {
        JSONObject jsonObject = null;
        Room room = null;
        short code = -1;
        Map<String, String> headers = null;
        headers = new HashMap<>(3);
        headers.put("referer", "https://live.bilibili.com/" + roomId);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        String url = "https://api.live.bilibili.com/room_ex/v1/RoomNews/get?roomid=" + roomId;
        String result = HttpUtil.doGetWithHeader(url, headers);
        jsonObject = JSONObject.parseObject(result);
        code = jsonObject.getShort("code");
        if (code == 0) {
            room = jsonObject.getObject("data", Room.class);
        } else {
            LOGGER.error("直播房间号不存在，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
        }
        return room;
    }

    /**
     * 通过UID获取头像
     *
     * @param uid 用户头像
     * @return 头像地址
     */
    public static String httpGetFaceUrl(long uid) {

//        String url = "https://tenapi.cn/bilibili/?uid=" + uid;
//        String result = HttpUtil.doGet(url);
//
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        String data = jsonObject.getString("data");
//       String faceUrl = JSONObject.parseObject(data).getString("avatar");
//        return faceUrl;

        Map<String, String> headers = new HashMap<>(3);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        headers.put("referer", "https://space.bilibili.com/" + uid);
//        headers.put("referer", "https://live.bilibili.com/" + CurrencyTools.parseRoomId());

//        headers.put("cookie", "buvid3=08D352DC-DC35-6D0B-D7A6-F4580C59984D09585infoc; i-wanna-go-back=-1; _uuid=7CAC75C3-27A2-ADD7-CB54-F10B5242310A3609137infoc; FEED_LIVE_VERSION=V8; SESSDATA=a8aa63df%2C1696936030%2C488e4%2A42; bili_jct=d776d147a5bf13b53bb79c13dd62520e; DedeUserID=939948; DedeUserID__ckMd5=9518835468e33460; sid=8a5pl4lu; b_ut=5; header_theme_version=CLOSE; CURRENT_PID=23923600-d9eb-11ed-be23-97bcf7bbe66b; rpdid=|(um|JR))J~k0J'uY)uJ)R|R~; LIVE_BUVID=AUTO8716813855968636; nostalgia_conf=-1; buvid_fp_plain=undefined; hit-new-style-dyn=1; hit-dyn-v2=1; share_source_origin=QQ; bsource=search_baidu; fingerprint=1866c61b561bad20f46ded607d8975aa; bp_article_offset_939948=786134939893497900; PEA_AU=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJiaWQiOjkzOTk0OCwicGlkIjoxMDg5OTMsImV4cCI6MTcxMzQzNzY5MiwiaXNzIjoidGVzdCJ9.4iPiYPKpM62sHBYEBfnaMmD5V-i7_AdLCwAzscRaP84; home_feed_column=5; CURRENT_FNVAL=4048; b_nut=1681907299; innersign=0; browser_resolution=1536-722; b_lsid=1039CD975_1879DCEFD97; buvid_fp=44b6fa13d9548059aabf9a59543f7da9; bp_video_offset_939948=786596442720436400; _dfcaptcha=427a842a0ea68c70cab214df1990f0ec; PVID=36; buvid4=7F23CB9E-1207-096C-3093-293FE8C9ED4111559-023041319-0mOPZbinCsPmFgrzIcnxpw%3D%3D");

//        headers.put("cookie", "DedeUserID__ckMd5=9518835468e33460;SESSDATA=2be2c157%2C1695794768%2Cad75e*31;bili_jct=762e71db19f43462a0c7ad53d1f44fc9;sid=67c3bea3;DedeUserID=939948");

//        https://api.bilibili.com/x/space/wbi/acc/info?mid=327126417&token=&platform=web&web_location=1550101&w_rid=97cabccb69750659ed72c5244b46d134&wts=1682957910

        String url = "https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&token=&platform=web";
        String result = HttpUtil.doGetWithHeader(url, headers);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (JSONException e) {
            String[] s111 = result.split("}");
            String s11111 = s111[0] + "}";
//            System.out.println("请求过于频繁，请稍后再试");
            String newResult = result.replace(s11111, "");
            jsonObject = JSONObject.parseObject(newResult);
            String data = jsonObject.getString("data");
            String faceUrl = JSONObject.parseObject(data).getString("face");
            return faceUrl;
        }

        if (ObjectUtils.isEmpty(jsonObject)) {
            System.out.println("请求过于频繁，已关闭显示");
            System.out.println(result);
            return "";
        }

        String data = jsonObject.getString("data");
        return JSONObject.parseObject(data).getString("face");

    }

    public static String getOneFace(Long uid) {

        String url = "https://tenapi.cn/bilibili/?uid=" + uid;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

//        headers.put("cookie", token);
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);


        String s1 = jsonObject.getJSONObject("data").getString("avatar");
//        System.out.println(s1);
        return s1;
    }


    public static String getPicture(Long uid, String faceUrl) throws IOException {
        String url = faceUrl;
        String path = System.getProperty("user.dir");

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                InputStream inStream = entity.getContent();
//                C:\Users\Administrator\Desktop\交付

                path = path + File.separator + "src" + File.separator + "main" + File.separator
                        + "resources" + File.separator + "static" + File.separator + "face" + File.separator;
                System.out.println(" 文件路径 " + path);
                path = path + uid + ".jpg";
                FileOutputStream fw = new FileOutputStream(path, false);
                int b = inStream.read();
                while (b != -1) {
                    fw.write(b);
                    b = inStream.read();
                }
                fw.close();
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return "";
    }

    /**
     * 获取礼物列表
     *
     * @param uid 用户uid
     * @return
     */
    public static List<GiftConfigData> getGiftData(long uid) {
        String url = "https://api.live.bilibili.com/xlive/web-room/v1/giftPanel/giftConfig?platform=pc&room_id=" + uid;
        String result = HttpUtil.doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");

        JSONArray j1 = JSONObject.parseObject(data).getJSONArray("list");
        List<GiftConfigData> list = JSONObject.parseArray(j1.toJSONString(), GiftConfigData.class);
        return list;

//
//        Map<String, String> headers = new HashMap<>(3);
//        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
//        headers.put("referer", "https://space.bilibili.com/" + uid);
//
//
//
//        String url = "https://api.bilibili.com/x/space/acc/info?mid=" + uid;
//        String result = HttpUtil.doGetWithHeader(url,headers);
//
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        String data = jsonObject.getString("data");
//        String faceUrl = JSONObject.parseObject(data).getString("face");
//
//        return faceUrl;

    }

    /**
     * 通过uid获取 用户基本信息
     *
     * @param uid 用户uid
     * @return 用户基本信息
     */
    public static UserInfo httpGetUserInfo(long uid) {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        headers.put("referer", "https://space.bilibili.com/" + uid);

        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token&platform=web";
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


    /**
     * 通过房间号和token获取信息
     *
     * @param roomId 房间号
     * @param token  token
     * @return
     */
    public static List<EmojiInfo> getEmojiInfoByToken(Long roomId, String token) {

        String url = "https://api.live.bilibili.com/xlive/web-ucenter/v2/emoticon/GetEmoticons?platform=pc&room_id=" + roomId;
//        String url = "https://api.live.bilibili.com/xlive/web-ucenter/v2/emoticon/GetEmoticons?platform=pc&room_id=24673446";
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        if (StringUtils.isEmpty(token)) {
            token = "buvid3=08D352DC-DC35-6D0B-D7A6-F4580C59984D09585infoc; i-wanna-go-back=-1; _uuid=7CAC75C3-27A2-ADD7-CB54-F10B5242310A3609137infoc; FEED_LIVE_VERSION=V8; SESSDATA=a8aa63df%2C1696936030%2C488e4%2A42; bili_jct=d776d147a5bf13b53bb79c13dd62520e; DedeUserID=939948; DedeUserID__ckMd5=9518835468e33460; sid=8a5pl4lu; b_ut=5; header_theme_version=CLOSE; CURRENT_FNVAL=4048; CURRENT_PID=23923600-d9eb-11ed-be23-97bcf7bbe66b; rpdid=|(um|JR))J~k0J'uY)uJ)R|R~; LIVE_BUVID=AUTO8716813855968636; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1681425952; nostalgia_conf=-1; bsource=search_baidu; buvid_fp_plain=undefined; b_nut=1681448088; home_feed_column=5; hit-new-style-dyn=1; hit-dyn-v2=1; fingerprint=1866c61b561bad20f46ded607d8975aa; _dfcaptcha=6e8e8c9b9b8001ae78749167e55b1dac; bp_video_offset_939948=784782373085511700; bp_article_offset_939948=784737280223871000; buvid_fp=44b6fa13d9548059aabf9a59543f7da9; b_lsid=7EFA41610_18784EAA15C; innersign=0; buvid4=F30C274A-BA93-645C-4AB3-CC0E267F768B71380-023041521-WEPh3cauru/S2ihNzsVNARaXWfUg45pw4+AvxkahliBqCuL2gzeF7A%3D%3D; Hm_lpvt_8a6e55dbd2870f0f5bc9194cddf32a02=1681565951; PVID=94";

        }

        headers.put("cookie", token);
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0).getJSONArray("emoticons");

        List<EmojiInfo> list = JSONObject.parseArray(jsonArray.toJSONString(), EmojiInfo.class);
        return list;
    }
}