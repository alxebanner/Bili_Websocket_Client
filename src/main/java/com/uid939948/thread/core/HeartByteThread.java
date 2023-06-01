package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;

@Slf4j
public class HeartByteThread extends Thread {
    public volatile boolean HFLAG = false;

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
                    Thread.sleep(30000);
                    MainConf.webSocketProxy.send(HexUtils.fromHexString(MainConf.heartByte));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

}
