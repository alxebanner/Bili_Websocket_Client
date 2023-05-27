package com.uid939948;

import com.uid939948.Http.HttpRoomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {

//        long l1 = System.currentTimeMillis();
//        long l2 = System.currentTimeMillis() + 1000 * 60 * 5;
//
//        String s1 = timestamp2Date(String.valueOf(l1), "yyyy-MM-dd HH:mm:ss");
//        String s2 = timestamp2Date(String.valueOf(l2), "yyyy-MM-dd HH:mm:ss");
//        System.out.println(s1);
//        System.out.println(s2);

        String s22 = HttpRoomUtil.httpGetFaceV2(939948);
        System.out.println(s22);
    }

    public static String timestamp2Date(String str_num, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
//            log.debug("timestamp2Date" + "将13位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
//            log.debug("timestamp2Date" + "将10位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        }
    }
}
