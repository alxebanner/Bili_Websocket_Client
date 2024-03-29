package com.uid939948.Http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.Emoji.EmojiInfo;
import com.uid939948.DO.Face.Card;
import com.uid939948.DO.Face.RoomInfo;
import com.uid939948.DO.FansMember.FansMemberInfo;
import com.uid939948.DO.Login.LoginUrl;
import com.uid939948.DO.Room;
import com.uid939948.DO.RoomInit;
import com.uid939948.DO.UserBarrageType;
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

import java.io.*;
import java.util.*;

public class HttpRoomUtil {
    private static Logger LOGGER = LogManager.getLogger(HttpRoomUtil.class);

    private static String NO_FACE_URL = "https://s1.ax1x.com/2023/05/20/p95e0Ig.png";

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
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
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
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        if (StringUtils.isNotEmpty(userCookie)) {
            headers.put("cookie", userCookie);
            LOGGER.info("获取地址使用cookie" + userCookie);
        } else {
            LOGGER.info("获取地址没有使用cookie");
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
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
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
     * 另外一个接口获取头像
     * 官方接口
     *
     * @param uid 用戶uid
     * @return 消息
     */
    public static String httpGetFaceUrl_V2(long uid) {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        headers.put("referer", "https://space.bilibili.com/" + uid);

        String url = "https://api.bilibili.com/x/web-interface/card?mid=" + uid;
//        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token&platform=web";
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (ObjectUtils.isEmpty(result) || ObjectUtils.isEmpty(jsonObject) || StringUtils.isEmpty(jsonObject.getString("code"))) {
            LOGGER.warn("httpGetFaceUrl_V2 获取头像失败" + result);
            return NO_FACE_URL;
        }
        if ("0".equals(jsonObject.getString("code"))) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//        roomInit = jsonObject.getObject("data", RoomInit.class);

            Card card = jsonObject1.getObject("card", Card.class);
//        jsonObject.getJSONObject("data").getJSONObject("card").getJSONObject("face");
            return card.getFace();
        } else {
            LOGGER.error("httpGetFaceUrl_V2 获取头像失败" + result);
            return NO_FACE_URL;
        }
    }

    /**
     * 根据房间号获取 房间信息 (包含直播封面)
     *
     * @param roomId 房间号
     * @return 房间信息
     */
    public static RoomInfo httpGetRoomBaseInfo(long roomId) {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        String url = " https://api.live.bilibili.com/xlive/web-room/v1/index/getRoomBaseInfo?room_ids=" + roomId + "&req_biz=video";
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data").getJSONObject("by_room_ids");
        RoomInfo roomInfo = jsonObject1.getObject(roomId + "", RoomInfo.class);
        return roomInfo;
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

//        String url = "https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&token=&platform=web";


        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token&platform=web";
        String result = HttpUtil.doGetWithHeader(url, headers);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (JSONException e) {
            String[] s111 = result.split("}");
            String s11111 = s111[0] + "}";
            System.out.println("请求过于频繁，请稍后再试");

            System.out.println(result);

            String newResult = result.replace(s11111, "");
            jsonObject = JSONObject.parseObject(newResult);
            String data = jsonObject.getString("data");
            String faceUrl = JSONObject.parseObject(data).getString("face");
            return faceUrl;
        }

        if (ObjectUtils.isEmpty(jsonObject)) {
            System.out.println("请求过于频繁，已关闭显示");
            System.out.println(result);
            return NO_FACE_URL;
        }

        if (!jsonObject.getString("code").equals("0")) {

            System.out.println("code不为0");
            System.out.println(result);

            return NO_FACE_URL;
        }
        String data = jsonObject.getString("data");
        return JSONObject.parseObject(data).getString("face");
    }

    /**
     * tenap 根据uid获取头像
     *
     * @param uid 用户uid
     * @return 头像地址
     */
    public static String getOneFace(Long uid) {
        String url = "https://tenapi.cn/bilibili/?uid=" + uid;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);

        String s1 = jsonObject.getJSONObject("data").getString("avatar");

        System.out.println("s1");
        System.out.println(s1);
        return s1;
    }

    /**
     * 获取图片
     *
     * @param uid     用户uid
     * @param faceUrl 头像地址
     * @return 地址
     * @throws IOException 异常
     */
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
                path = path + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "face" + File.separator;
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
     * @param roomId 房间号
     * @return 礼物列表
     */
    public static List<GiftConfigData> getGiftData(long roomId) {
        String url = "https://api.live.bilibili.com/xlive/web-room/v1/giftPanel/giftConfig?platform=pc&room_id=" + roomId;
        String result = HttpUtil.doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");

        JSONArray j1 = JSONObject.parseObject(data).getJSONArray("list");
        List<GiftConfigData> list = JSONObject.parseArray(j1.toJSONString(), GiftConfigData.class);
        return list;
    }

    /**
     * 通过uid获取 用户基本信息第二种方式
     *
     * @param roomId 房间号
     * @return 用户基本信息
     */
    public static UserInfo httpGetUserInfoV2(long roomId) {
        Room room = HttpRoomUtil.httpGetRoomData(roomId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(Long.valueOf(room.getUid()));
        userInfo.setName(room.getUname());
        userInfo.setRoomId(Long.valueOf(room.getRoomId()));
        userInfo.setFace(HttpRoomUtil.httpGetFaceUrl_V2(Long.parseLong(room.getUid())));
        RoomInfo roomInfo = HttpRoomUtil.httpGetRoomBaseInfo(MainConf.ROOMID);
        userInfo.setCover(roomInfo.getCover());
        userInfo.setTitle(roomInfo.getTitle());
        userInfo.setLiveStatus(LiveStateEnum.getCountryValue(roomInfo.getLive_status()));
        return userInfo;
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

        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token=&platform=web";


//        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid;
//        String url = "https://api.bilibili.com/x/space/wbi/acc/info?mid=" + uid + "&token&platform=web";
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);

        try {
            jsonObject = JSONObject.parseObject(result);

            String code = jsonObject.getString("code");
            if (!"0".equals(code)) {
                System.out.println("httpGetUserInfo :" + result);
                return null;
            }

        } catch (JSONException e) {
            String[] s111 = result.split("}");
            String s11111 = s111[0] + "}";
            String newResult = result.replace(s11111, "");
            jsonObject = JSONObject.parseObject(newResult);
            String data1 = jsonObject.getString("data");
            String faceUrl = JSONObject.parseObject(data1).getString("face");
            System.out.println("请求过于频繁，已关闭显示");
            System.out.println(result);
            return null;
        }

        if (ObjectUtils.isEmpty(jsonObject)) {
            System.out.println("请求过于频繁，已关闭显示");
            System.out.println(result);
            return null;
        }

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
     * @return 表情列表
     */
    public static List<EmojiInfo> getEmojiInfoByToken(Long roomId, String token) {
        String url = "https://api.live.bilibili.com/xlive/web-ucenter/v2/emoticon/GetEmoticons?platform=pc&room_id=" + roomId;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
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

    /**
     * 通过房间号获取粉色勋章
     *
     * @param uid 房间号
     * @return 勋章实体
     */
    public static List<FansMemberInfo> getFansMembersRank(long uid) {
        String url = "https://api.live.bilibili.com/xlive/general-interface/v1/rank/getFansMembersRank?ruid=" + uid + "&page=1&page_size=30";
        String result = HttpUtil.doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        List<FansMemberInfo> memberInfoArrayList = new ArrayList<>();
        int num = data.getInteger("num");
        int count = 1;
        if (ObjectUtils.isNotEmpty(num)) {
            count = num / 30 + 1;
            // 页数为count
            for (int i = 1; i <= count; i++) {
                String url1 = "https://api.live.bilibili.com/xlive/general-interface/v1/rank/getFansMembersRank?ruid=" + uid +
                        "&page=" + i +
                        "&page_size=30";
                String resultTemp = HttpUtil.doGet(url1);
                JSONObject jsonObject1 = JSONObject.parseObject(resultTemp);
                JSONArray jsonArray = jsonObject1.getJSONObject("data").getJSONArray("item");
                List<FansMemberInfo> list = JSONObject.parseArray(jsonArray.toJSONString(), FansMemberInfo.class);
                memberInfoArrayList.addAll(list);
            }
        }
        return memberInfoArrayList;
    }

    /**
     * 通过uid获取 用户头像（ 第三方接口）
     *
     * @param uid 用户uid
     * @return 头像
     */
    public static String httpGetFaceV2(long uid) {
        String url = "https://api.obfs.dev/api/bilibili/v3/user_info?uid=" + uid + "&size=1";
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (ObjectUtils.isEmpty(result) || ObjectUtils.isEmpty(jsonObject) || ObjectUtils.isEmpty(jsonObject.getString("code"))) {
            LOGGER.warn("httpGetFaceV2 获取头像失败,被拉黑 " + result);
            return HttpRoomUtil.httpGetFaceUrl_V2(uid);
        }
        if ("0".equals(jsonObject.getString("code"))) {
            String face = jsonObject.getJSONObject("data").getJSONObject("card").getString("face");
            return face;
        } else {
            LOGGER.error("httpGetFaceV2 获取头像失败 " + result);
            return HttpRoomUtil.httpGetFaceUrl_V2(uid);
        }
    }

    /**
     * 通过uid获取 用户头像（第三方接口）
     * 查询不到就返回空
     *
     * @param uid 用户uid
     * @return 头像
     */
    public static String httpGetFaceV3(long uid) {
        String url = "https://api.obfs.dev/api/bilibili/v3/user_info?uid=" + uid + "&size=1";
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (ObjectUtils.isEmpty(result) || ObjectUtils.isEmpty(jsonObject) || ObjectUtils.isEmpty(jsonObject.getString("code"))) {
            LOGGER.warn("httpGetFaceV3 获取头像失败,被拉黑 " + result);
            return NO_FACE_URL;
        }
        if ("0".equals(jsonObject.getString("code"))) {
            String face = jsonObject.getJSONObject("data").getJSONObject("card").getString("face");
            return face;
        } else {
            LOGGER.error("httpGetFaceV3 获取头像失败 " + result);
            return NO_FACE_URL;
        }
    }

    /**
     * 获取二维码地址
     *
     * @return 二维码扫码地址
     */
    public static LoginUrl httpGetQrcode() {
        String result = null;
        JSONObject jsonObject = null;
        LoginUrl loginUrl = null;
        Map<String, String> headers = null;
        String url = "https://passport.bilibili.com/qrcode/getLoginUrl";

        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        try {
            result = HttpUtil.doGetWithHeader(url, headers);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
            result = null;
        }
        if (result == null)
            return loginUrl;
        jsonObject = JSONObject.parseObject(result);
        short code = jsonObject.getShort("code");
        if (code == 0) {
            loginUrl = JSONObject.parseObject(jsonObject.getString("data"), LoginUrl.class);
            jsonObject.getString("data");
        } else {
            LOGGER.error("获取二维码出错:" + jsonObject);
        }
        return loginUrl;
    }

    /**
     * 获取二维码登录状态
     *
     * @param oauthKey 秘钥
     * @return
     */
    public static String httpGetQrcodeStates(String oauthKey) {
        if (!StringUtils.isEmpty(MainConf.USERCOOKIE)) {
            return "";
        }
        String data = null;
        Map<String, String> headers = null;
        Map<String, String> params = null;
        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        headers.put("referer", "https://passport.bilibili.com/login");
        params = new HashMap<>(3);
        params.put("oauthKey", oauthKey);
        params.put("gourl", "https://www.bilibili.com/");
//        public static String sendPost(String url, Map<String, String> headerMap, Map<String, String> bodyMap) {
        String url = "https://passport.bilibili.com/qrcode/getLoginInfo";
        String result;
        try {
            result = HttpUtil.sendPost(url, headers, params);
            JSONObject.parseObject(data);
        } catch (Exception e1) {
            // TODO 自动生成的 catch 块
            LOGGER.error("扫码登录失败抛出异常:" + e1);
        }
        return data;
    }

    /**
     * 发送弹幕  已登录
     *
     * @param msg 弹幕
     * @return 是否发送成功
     */
    public static Boolean httpSendBarrage(String msg, UserBarrageType userBarrageType) {
//        if (!StringUtils.isEmpty(MainConf.USERCOOKIE)) {
//            return false;
//        }
        String data = null;
        Map<String, String> headers = null;
        Map<String, String> params = null;
        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        headers.put("referer", "https://passport.bilibili.com/login");

        headers.put("cookie", MainConf.USERCOOKIE);

        params = new HashMap<>(10);
        params.put("color", userBarrageType.getDanmu().getColor().toString());
        params.put("fontsize", "25");
        params.put("mode", userBarrageType.getDanmu().getMode().toString());
        params.put("msg", msg);
        params.put("rnd", String.valueOf(System.currentTimeMillis()).substring(0, 10));
        params.put("roomid", String.valueOf(MainConf.ROOMID));
        params.put("bubble", userBarrageType.getBubble_id().toString());
        params.put("csrf_token", MainConf.COOKIE.getBili_jct());
        params.put("csrf", MainConf.COOKIE.getBili_jct());
        String url = "https://api.live.bilibili.com/msg/send";
        String result;
        try {
            result = HttpUtil.sendPost(url, headers, params);
            return true;
        } catch (Exception e1) {
            // TODO 自动生成的 catch 块
            LOGGER.error("发送失败抛出异常:" + e1);
        }
        return false;
    }

    /**
     * 发送弹幕  已登录
     *
     * @param msg 弹幕
     * @return 是否发送成功
     */
    public static Boolean httpSendBarrage(String msg) {
//        if (!StringUtils.isEmpty(MainConf.USERCOOKIE)) {
//            return false;
//        }
        String data = null;
        Map<String, String> headers = null;
        Map<String, String> params = null;
        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        headers.put("referer", "https://passport.bilibili.com/login");

        headers.put("cookie", MainConf.USERCOOKIE);

        params = new HashMap<>(10);
        params.put("color", MainConf.userBarrageType.getDanmu().getColor().toString());
        params.put("fontsize", "25");
        params.put("mode", MainConf.userBarrageType.getDanmu().getMode().toString());
        params.put("msg", msg);
        params.put("rnd", String.valueOf(System.currentTimeMillis()).substring(0, 10));
        params.put("roomid", String.valueOf(MainConf.ROOMID));
        params.put("bubble", MainConf.userBarrageType.getBubble_id().toString());
        params.put("csrf_token", MainConf.COOKIE.getBili_jct());
        params.put("csrf", MainConf.COOKIE.getBili_jct());
        String url = "https://api.live.bilibili.com/msg/send";
        String result;
        try {
            result = HttpUtil.sendPost(url, headers, params);
            return true;
        } catch (Exception e1) {
            // TODO 自动生成的 catch 块
            LOGGER.error("发送失败抛出异常:" + e1);
        }
        return false;
    }


    public static UserBarrageType httpGetInfoByUser(Long roomId) {
        String result = null;
        JSONObject jsonObject = null;
        UserBarrageType userBarrageType = new UserBarrageType();
        Map<String, String> headers = null;
        String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByUser?room_id=" + roomId;

        if (StringUtils.isEmpty(MainConf.USERCOOKIE)) {
            LOGGER.info("未登录，无法获取弹幕长度信息");
        }

        headers = new HashMap<>(3);
        headers.put("referer", "https://live.bilibili.com/" + roomId);
        headers.put("cookie", MainConf.USERCOOKIE);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        try {
            result = HttpUtil.doGetWithHeader(url, headers);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
        }
        if (result == null)
            return userBarrageType;
        jsonObject = JSONObject.parseObject(result);
        short code = jsonObject.getShort("code");
        if (code == 0) {
            LOGGER.debug("获取 房间号" + roomId + "可发送弹幕长度");
            userBarrageType = jsonObject.getJSONObject("data").getJSONObject("property").toJavaObject(UserBarrageType.class);
        } else {
            LOGGER.error("获取可发送弹幕长度失败:" + jsonObject);
        }
        return userBarrageType;
    }

    /**
     * 文字编码
     *
     * @param str     字符串
     * @param charset 格式
     * @return 编码后格式
     */
    public static String URLEncoderString(String str, String charset) {
        String result = "";
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        try {
            result = java.net.URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 文字解码
     *
     * @param str     字符串
     * @param charset 格式
     * @return 解码后格式
     */
    public static String URLDecoderString(String str, String charset) {
        String result = "";
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        try {
            result = java.net.URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取二维码地址
     *
     * @return 获取二维码key
     */
    public static String httpGetQR_Code_Key() {
        String result = null;
        JSONObject jsonObject = null;
        String key = "";
        Map<String, String> headers = null;
        String url = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate?source=main-fe-header";
        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        try {
            result = HttpUtil.doGetWithHeader(url, headers);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
        }
        if (result == null)
            return "";
        jsonObject = JSONObject.parseObject(result);
        short code = jsonObject.getShort("code");
        if (code == 0) {
            key = jsonObject.getJSONObject("data").getString("qrcode_key");

        } else {
            LOGGER.error("获取二维码失败:" + jsonObject);
        }
        return key;
    }

    /**
     * 二维码轮询
     *
     * @param qrcode_key 二维码key
     * @return 二维码key 的轮询结果
     */
    public static String httpGetQR_Code_Check(String qrcode_key) {
        String result = null;
        JSONObject jsonObject = null;
        String key = "";
        Map<String, String> headers = null;
        String url = " https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key=" + qrcode_key + "&source=main-fe-headerr";

        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        try {
            result = HttpUtil.doGetWithHeader(url, headers);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
        }
        if (result == null)
            return "";
        jsonObject = JSONObject.parseObject(result);

        JSONObject j1 = jsonObject.getJSONObject("data");
        String code = j1.getString("code");
        String data_code = jsonObject.getString("code");
        if ("0".equals(data_code)) {
            if ("86090".equals(code)) {
                LOGGER.info("二维码已扫码未确认:" + jsonObject);
            } else if ("86101".equals(code)) {
                LOGGER.info("未扫码 请等待继续扫码:" + jsonObject);
            } else if ("86038".equals(code)) {
                LOGGER.info("二维码已经失效,请刷新重试:" + jsonObject);
            } else if ("0".equals(code)) {
                LOGGER.info("二维码已登录:");
                LOGGER.debug("二维码已登录:" + jsonObject);
                String s1 = jsonObject.getJSONObject("data").getString("url");
                String s2 = s1.replace("https://passport.biligame.com/x/passport-login/web/crossDomain?", "");
                String s3 = s2.replace("https%3A%2F%2Fwww.bilibili.com", "https://www.bilibili.com");

                List<String> list = Arrays.asList(s3.split("&"));

                String DedeUserID = list.stream().filter(mo -> mo.startsWith("DedeUserID=")).findAny().orElse("");
                Long uid = Long.valueOf(DedeUserID.replace("DedeUserID=", ""));

                String result1 = String.join(";", list);

                MainConf.USERCOOKIE = result1;
                MainConf.USERCOOKIE_UID = uid;
                LOGGER.debug("输出qrcode_key:" + result1);

            } else {
                LOGGER.error("验证二维码出错:" + jsonObject);
            }
        }

        return code;
    }


    public static String HttpGetLoginInfo(String oauthKey) {
        String result = null;
        JSONObject jsonObject = null;
        String key = "";

        Map<String, String> headers = null;
        Map<String, String> params = null;


        String url = " https://passport.bilibili.com/qrcode/getLoginInfo";

        headers = new HashMap<>(3);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        headers.put("referer", "https://passport.bilibili.com/login");

        params = new HashMap<>(3);

        params.put("oauthKey", oauthKey);
        params.put("gourl", "https://www.bilibili.com/");

        try {
            result = HttpUtil.sendPost(url, headers, params);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
        }
        if (result == null)
            return "";
        jsonObject = JSONObject.parseObject(result);

//        JSONObject j1 = jsonObject.getJSONObject("data");
//        String code = j1.getString("code");
//        String data_code = jsonObject.getString("code");
//        if ("0".equals(data_code)) {
//            if ("86090".equals(code)) {
//                LOGGER.info("二维码已扫码未确认:" + jsonObject);
//            } else if ("86101".equals(code)) {
//                LOGGER.info("未扫码 请等待继续扫码:" + jsonObject);
//            } else if ("86038".equals(code)) {
//                LOGGER.info("二维码已经失效,请刷新重试:" + jsonObject);
//            } else if ("0".equals(code)) {
//                LOGGER.info("二维码已登录:" + jsonObject);
//
//                // 旧的  DedeUserID__ckMd5=9518835468e33460;SESSDATA=63651ffc%2C1704103286%2C8c171*71;bili_jct=726a82f532d3551d4dee029d88a48e68;sid=nm8lednh;DedeUserID=939948
//
//                String key1 = jsonObject.getJSONObject("data").getString("url");
//
//                LOGGER.info("key1:" + key1);
//                MainConf.USERCOOKIE = key1;
////                {"code":0,"data":{"refresh_token":"aa1c81c20152d2a106f59dbe01589081","code":0,"message":"","url":"https://passport.biligame.com/x/passport-login/web/crossDomain?DedeUserID=939948&DedeUserID__ckMd5=9518835468e33460&Expires=1708693173&SESSDATA=4ace4b62,1708693173,2a913*81fkN7bL3uYg0xsLIkMy2WkS1KPH5Pqh3WHu1KJ4wV_bqLaPcimgAUMMXZBOU-nnNYly_kXgAAMAA&bili_jct=2babeac9be9e71ec562c6674fc7aee88&gourl=https%3A%2F%2Fwww.bilibili.com","timestamp":1693141173673},"message":"0","ttl":1}
//
//
//                //"https://passport.biligame.com/x/passport-login/web/crossDomain?DedeUserID=939948&DedeUserID__ckMd5=9518835468e33460&Expires=1708693173&SESSDATA=4ace4b62,1708693173,2a913*81fkN7bL3uYg0xsLIkMy2WkS1KPH5Pqh3WHu1KJ4wV_bqLaPcimgAUMMXZBOU-nnNYly_kXgAAMAA&bili_jct=2babeac9be9e71ec562c6674fc7aee88&gourl=https%3A%2F%2Fwww.bilibili.com
//            } else {
//                LOGGER.error("验证二维码出错:" + jsonObject);
//            }
//        }

        return "1";
    }
    //111   DedeUserID__ckMd5=9518835468e33460DedeUserID__ckMd5=9518835468e33460;SESSDATA=16da32e7,1708694069,a2288*81Nu5er3Vw-ml1yF6nn2ldZzDuE_cd7mGJ4nRnhaor9DrPMe_fl6TvMDWimamiZRzvDth5WwAAMAA;bili_jct=ddf5e33c1157b88b8d38040e3b328445;DedeUserID=939948

}
//
//url: "https://passport.biligame.com/x/passport-login/web/crossDomain?DedeUserID=939948&DedeUserID__ckMd5=9518835468e33460&Expires=1708694069&SESSDATA=16da32e7,1708694069,a2288*81Nu5er3Vw-ml1yF6nn2ldZzDuE_cd7mGJ4nRnhaor9DrPMe_fl6TvMDWimamiZRzvDth5WwAAMAA&bili_jct=ddf5e33c1157b88b8d38040e3b328445&gourl=https%3A%2F%2Fwww.bilibili.com"
//
//buvid3=24FC6C9C-3A1A-D50D-9D28-0FC47C39692703774infoc; b_nut=1693139103; i-wanna-go-back=-1; b_ut=7; b_lsid=75D812F7_18A36F1FB4C; _uuid=7108B5C97-B475-277C-5935-57B3282B9B4942808infoc; buvid_fp=438e7caaac2291a8b7338441a9847a96; home_feed_column=5; buvid4=B3E03C73-B7C6-C696-EC4B-F8291FE6F8B305324-023082720-0mOPZbinCsOGH0YulYR3xg%3D%3D; header_theme_version=CLOSE; CURRENT_FNVAL=4048; innersign=0; browser_resolution=1536-258; SESSDATA=16da32e7%2C1708694069%2Ca2288%2A81Nu5er3Vw-ml1yF6nn2ldZzDuE_cd7mGJ4nRnhaor9DrPMe_fl6TvMDWimamiZRzvDth5WwAAMAA; bili_jct=ddf5e33c1157b88b8d38040e3b328445; DedeUserID=939948; DedeUserID__ckMd5=9518835468e33460; sid=qcvet8gb