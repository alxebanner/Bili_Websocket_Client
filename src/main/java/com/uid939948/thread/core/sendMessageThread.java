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
 * 发送弹幕线程
 * 默认每隔2秒
 */
@Slf4j
public class sendMessageThread extends Thread {

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

            if (MainConf.webSocketProxy != null && !MainConf.webSocketProxy.isOpen()) {
                return;
            }

            if (MainConf.webSocketProxy.isOpen()) {
                try {


                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }
}
