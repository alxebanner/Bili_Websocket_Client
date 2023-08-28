package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import com.uid939948.Controller.DanmuWebsocket;
import com.uid939948.DO.danmu.DanmuVO.DanmuDto;
import com.uid939948.DO.temp.FacePicture;
import com.uid939948.Until.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 弹幕机器人
 */
@Slf4j
public class RoboterThread extends Thread {

    public volatile boolean HFLAG = false;

    private DanmuWebsocket danmuWebsocket = SpringUtils.getBean(DanmuWebsocket.class);

    @Override
    public void run() {
        super.run();
        // TODO 自动生成的方法存根
        while (!HFLAG) {
            if (HFLAG) {
                return;
            }
            if (MainConf.webSocketProxy.isOpen()) {
                try {
                    Thread.sleep(10000);
                    List<FacePicture> list = MainConf.noFaceUidList;

                    Object object = null;
                    String type = null;

                    danmuWebsocket.sendMessage(DanmuDto.toJson(type, object));

                    if (CollectionUtils.isNotEmpty(list) && list.size() != 0) {

                    }


                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }
}
