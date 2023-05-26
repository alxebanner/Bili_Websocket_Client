package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import com.uid939948.DO.temp.FacePicture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * 定期获取头像
 */
@Slf4j
public class FaceThread extends Thread {
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
                    Thread.sleep(10000);
//                    MainConf.webSocketProxy.send(HexUtils.fromHexString(MainConf.heartByte));
                    List<FacePicture> list = MainConf.noFaceUidList;

                    if (ObjectUtils.isNotEmpty(list) && list.size() != 0) {

                    }

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
