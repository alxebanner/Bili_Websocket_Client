package com.uid939948.config;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.*;
import com.uid939948.Service.impl.SetServiceImpl;
import com.uid939948.Until.BASE64Encoder;
import com.uid939948.Until.ProFileTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Hashtable;

@Configuration
@Slf4j
public class DanmujiInitConfig {
    private final String cookies = "1";
    private SetServiceImpl checkService;

    public void init() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        String cookieString = null;
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            hashtable.putAll(ProFileTools.read("DanmujiProfile"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        // 读取本地cookie
        try {
            cookieString = !StringUtils.isEmpty(hashtable.get(cookies))
                    ? new String(base64Encoder.decode(hashtable.get(cookies)))
                    : null;
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            log.error("获取本地cookie失败,请重新登录" + e);
            cookieString = null;
        }
        if (!StringUtils.isEmpty(cookieString)) {
            if (StringUtils.isEmpty(MainConf.USERCOOKIE)) {
                MainConf.USERCOOKIE = cookieString;
            }
        }

        // 读取本地set
        try {
            MainConf.centerSetConf = !StringUtils.isEmpty(hashtable.get("set")) ? JSONObject
                    .parseObject(new String(base64Encoder.decode(hashtable.get("set"))), CenterSetConf.class) : null;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("读取配置文件失败,尝试重新读取" + e);
            MainConf.centerSetConf = null;
        }
        //初始化配置文件开始
        if (MainConf.centerSetConf == null) {
            MainConf.centerSetConf = new CenterSetConf();
        } else {
            if (MainConf.centerSetConf.getRoomid() != null && MainConf.centerSetConf.getRoomid() > 0)
                MainConf.ROOMID_SAFE = MainConf.centerSetConf.getRoomid();
            if (MainConf.ROOMID_SAFE != null && MainConf.ROOMID_SAFE > 0)
                MainConf.centerSetConf.setRoomid(MainConf.ROOMID_SAFE);
        }
        //初始化配置文件结束
        hashtable.put("set", base64Encoder.encode(MainConf.centerSetConf.toJson().getBytes()));
        ProFileTools.write(hashtable, "DanmujiProfile");
        try {
            MainConf.centerSetConf = JSONObject
                    .parseObject(new String(base64Encoder.decode(hashtable.get("set"))), CenterSetConf.class);
            log.info("读取配置文件成功");
        } catch (Exception e) {
            // TODO: handle exception
            log.error("读取配置文件失败" + e);
        }
        // 分离cookie  改
        if (!StringUtils.isEmpty(MainConf.USERCOOKIE) && MainConf.COOKIE == null) {
            String key = null;
            String value = null;
            int controlNum = 0;
            String cookie = MainConf.USERCOOKIE;
            MainConf.COOKIE = new UserCookie();
            cookie = cookie.trim();
            String[] a = cookie.split(";");
            for (String string : a) {
                if (string.contains("=")) {
                    String[] maps = string.split("=");
                    key = maps[0];
                    value = maps.length >= 2 ? maps[1] : "";
                    if (key.equals("DedeUserID")) {
                        MainConf.COOKIE.setDedeUserID(value);
                        controlNum++;
                    } else if (key.equals("bili_jct")) {
                        MainConf.COOKIE.setBili_jct(value);
                        controlNum++;
                    } else if (key.equals("DedeUserID__ckMd5")) {
                        MainConf.COOKIE.setDedeUserID__ckMd5(value);
                        controlNum++;
                    } else if (key.equals("sid")) {
                        MainConf.COOKIE.setSid(value);
                        controlNum++;
                    } else if (key.equals("SESSDATA")) {
                        MainConf.COOKIE.setSESSDATA(value);
                        controlNum++;
                    } else {
//						log.info("获取cookie失败，字段为" + key);
                    }
                }
            }
            if (controlNum >= 2) {
                log.info("用户cookie装载成功");
                controlNum = 0;
            } else {
                log.info("用户cookie装载失败");
                MainConf.COOKIE = null;
            }
            checkService.holdSet(MainConf.centerSetConf);
        }
        base64Encoder = null;
        hashtable.clear();
    }

    @Autowired
    public void setCheckService(SetServiceImpl checkService) {
        this.checkService = checkService;
    }
}
