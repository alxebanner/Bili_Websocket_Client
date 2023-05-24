package com.uid939948.Http;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.DO.RoomInit;
import com.uid939948.HttpUtil.HttpUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpMusicUtil {
    private static Logger LOGGER = LogManager.getLogger(HttpMusicUtil.class);


    public static String getMusicIdByName(String musicName) {
        RoomInit roomInit = null;
        Map<String, String> headers = null;
        headers = new HashMap<>(2);
        headers.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
//        String url = "https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomId;

        String url = "http://music.163.com/api/search/pc?s=" + musicName    +"  &type=1";

        String result = HttpUtil.doGetWithHeader(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if ("0".equals(code)) {
            roomInit = jsonObject.getObject("data", RoomInit.class);
        } else {
            LOGGER.error("直播房间号不存在，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
        }
        return "";
    }
}
