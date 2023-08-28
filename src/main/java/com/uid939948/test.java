package com.uid939948;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.UserBarrageType;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.HttpUtil.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class test {
    public static void main(String[] args) {

        String str = "{\"cmd\":\"room_admin_entrance\",\"dmscore\":45,\"is_report\":false,\"level\":1,\"msg\":\"系统提示：你已被主播设为房管\",\"msg_id\":\"618799078316032\",\"send_time\":1689734145576,\"uid\":287769888}";



        BlockingQueue queue = new LinkedBlockingQueue();
        ThreadPoolExecutor executor= new ThreadPoolExecutor(10,Integer.MAX_VALUE,10L, TimeUnit.SECONDS,queue);






        String msg = "我的";
//        System.out.println(URLEncoderString(msg, "utf-8") + "");

//        UserBarrageType userBarrageType = httpGetInfoByUser(3961583L);

        UserBarrageType userBarrageType = HttpRoomUtil.httpGetInfoByUser(3961583L);
//        System.out.println("userBarrageType开始");
//        System.out.println(userBarrageType);
//        System.out.println("userBarrageType结束");
        HttpRoomUtil.httpSendBarrage(msg, userBarrageType);
    }


    public static UserBarrageType httpGetInfoByUser(Long roomId) {
        String result = null;
        JSONObject jsonObject = null;
        UserBarrageType userBarrageType = new UserBarrageType();
        Map<String, String> headers = null;
        String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByUser?room_id=" + roomId;

        if (StringUtils.isEmpty(MainConf.USERCOOKIE)) {
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
        }

        if (result == null)
            return userBarrageType;
        jsonObject = JSONObject.parseObject(result);
        short code = jsonObject.getShort("code");
        if (code == 0) {

            System.out.println(result);
            System.out.println("11");
            System.out.println(jsonObject.getJSONObject("data").getJSONObject("property"));

            userBarrageType = jsonObject.getJSONObject("data").getJSONObject("property").toJavaObject(UserBarrageType.class);
        } else {
        }
        return userBarrageType;
    }


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
}
