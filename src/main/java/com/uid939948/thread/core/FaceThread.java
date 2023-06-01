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
                    Thread.sleep(10000);
                    List<FacePicture> list = MainConf.noFaceUidList;

                    if (ObjectUtils.isNotEmpty(list) && list.size() != 0) {

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }
}
