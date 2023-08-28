package com.uid939948;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.UserBarrageType;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.HttpUtil.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test1 {
    public static void main(String[] args) {
//        String USERCOOKIE = "DedeUserID__ckMd5=9518835468e33460;SESSDATA=63651ffc%2C1704103286%2C8c171*71;bili_jct=726a82f532d3551d4dee029d88a48e68;sid=nm8lednh;DedeUserID=939948";
//
////        List<String> list = USERCOOKIE.split(",");
//
//        List<String> result = Arrays.asList(USERCOOKIE.split(";"));
//        result.stream().filter(mo -> mo.startsWith("bili_jct=")).findAny().ifPresent(mo1 -> {
//            MainConf.COOKIE.setBili_jct(mo1.substring(9, mo1.length()));
//        });
//        String msg = "测试2";
//        MainConf.ROOMID = 3961583L;
//
//        UserBarrageType userBarrageType = HttpRoomUtil.httpGetInfoByUser(MainConf.ROOMID);
//        HttpRoomUtil.httpSendBarrage(msg, userBarrageType);



//
//        String s1 = HttpRoomUtil.httpGetQR_Code_Key();
//        System.out.println(s1);



        String s1 = "https://passport.biligame.com/x/passport-login/web/crossDomain?DedeUserID=939948&DedeUserID__ckMd5=9518835468e33460&Expires=1708693173&SESSDATA=4ace4b62,1708693173,2a913*81fkN7bL3uYg0xsLIkMy2WkS1KPH5Pqh3WHu1KJ4wV_bqLaPcimgAUMMXZBOU-nnNYly_kXgAAMAA&bili_jct=2babeac9be9e71ec562c6674fc7aee88&gourl=https%3A%2F%2Fwww.bilibili.com";
        String s2=  s1.replace("https://passport.biligame.com/x/passport-login/web/crossDomain?", "");
        List<String> list = Arrays.asList(s2.split("&"));

//        HttpRoomUtil.HttpGetLoginInfo("00 cb09d07ba37efd50645211285420dc");
        String result = String.join(";", list);
        System.out.println("s1");
        System.out.println(result);


        String result1= bytesToHex("https%3A%2F%2Fwww.bilibili.com".getBytes());
        System.out.println(result1);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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


//    bubble: 0
//    msg: [dog][鼓掌]
//    color: 16777215
//    mode: 1
//    room_type: 0
//    jumpfrom: 86002
//    fontsize: 25
//    rnd: 1690885088
//    roomid: 3961583
//    csrf: d776d147a5bf13b53bb79c13dd62520e
//    csrf_token: d776d147a5bf13b53bb79c13dd62520e

//    bubble: 0
//    msg: 1
//    color: 16777215
//    mode: 1
//    room_type: 0
//    jumpfrom: 0
//    fontsize: 25
//    rnd: 1690886248
//    roomid: 3961583
//    csrf: d776d147a5bf13b53bb79c13dd62520e
//    csrf_token: d776d147a5bf13b53bb79c13dd62520e


    //bubble: 0
    //msg: 1
    //color: 16777215
    //mode: 1
    //room_type: 0
    //jumpfrom: 0
    //fontsize: 25
    //rnd: 1690886248
    //roomid: 3961583
    //csrf: d776d147a5bf13b53bb79c13dd62520e
    //csrf_token: d776d147a5bf13b53bb79c13dd62520e

}
