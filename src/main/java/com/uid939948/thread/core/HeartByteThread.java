package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;

@Slf4j
public class HeartByteThread extends Thread {
    //	private Logger LOGGER = LogManager.getLogger(HeartByteThread.class);
//	Websocket client;
//	String heartByte;
    public volatile boolean HFLAG = false;

//    public volatile int count1 = 0;

//	public HeartByteThread(Websocket client, String heartByte) {
//		super();
//		this.client = client;
//		this.heartByte = heartByte;
//	}

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
