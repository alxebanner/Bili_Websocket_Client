package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import com.uid939948.Controller.DanmuWebsocket;
import com.uid939948.DO.danmu.DanmuVO.DanmuDto;
import com.uid939948.Until.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class HeartOnlineThread extends Thread {

    public volatile boolean HFLAG = false;

    private DanmuWebsocket danmuWebsocket = SpringUtils.getBean(DanmuWebsocket.class);

//    @Resource
//    DanmuWebsocket danmuWebsocket;

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
                    // 5分钟
                    Thread.sleep(300000);

//                    MainConf.webSocketProxy.send(HexUtils.fromHexString(MainConf.heartByte));

                    try {
                        danmuWebsocket.sendMessage(DanmuDto.toJson("HeartOnline", "HeartOnline"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    //

//                    log.info("发送心跳 次数为 " + count1);
//                    count1++;
                } catch (Exception e) {
                    // TODO: handle exception
//					LOGGER.info("心跳线程关闭:"+e);
//					e.printStackTrace();
                }
            }
        }
    }

}
