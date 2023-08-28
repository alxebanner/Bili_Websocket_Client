package com.uid939948.Service.impl;

import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.WebsocketConfig.WebsocketConfigInfo;
import com.uid939948.Service.ConfigService;
import com.uid939948.Until.BASE64Encoder;
import com.uid939948.Until.ProFileTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Hashtable;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Override
    public Boolean saveConfigUid() {
        return null;
    }

    @Override
    public Boolean saveConfigRoomId(String roomId) {
        CenterSetConf centerSetConf = MainConf.centerSetConf;
        long room = 0L;
        try {
            room = Long.parseLong(roomId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.info("roomId 保存失败" + roomId);
        }
        centerSetConf.setRoomid(room);
        synchronized (centerSetConf) {
            Hashtable<String, String> hashtable = new Hashtable<String, String>();
            BASE64Encoder base64Encoder = new BASE64Encoder();
            hashtable.put("set", base64Encoder.encode(centerSetConf.toJson().getBytes()));
            ProFileTools.write(hashtable, "DanmujiProfile");
            log.info("保存配置文件成功");
            base64Encoder = null;
            hashtable.clear();
        }
        return true;
    }

    @Override
    public WebsocketConfigInfo getConfig() {
        return null;
    }

    private void test() {


    }


}
