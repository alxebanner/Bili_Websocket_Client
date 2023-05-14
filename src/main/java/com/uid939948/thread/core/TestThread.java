package com.uid939948.thread.core;

import com.alibaba.fastjson.JSONObject;
import com.uid939948.Conf.MainConf;
import com.uid939948.Controller.DanmuWebsocket;
import com.uid939948.DO.danmu.DanMu_MSG.DanMu_MSG_Info;
import com.uid939948.DO.danmu.DanmuVO.DanmuDto;
import com.uid939948.Until.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TestThread extends Thread {
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
                    Thread.sleep(1000);
                    List<String> testDanmuMessageList = MainConf.testDanmuMessageList;

                    // 打乱顺序
                    Collections.shuffle(testDanmuMessageList);

                    String message = testDanmuMessageList.stream().findAny().get();


//                    String result = HttpUtil.doGetWithHeader(url, headers);
//                    JSONObject jsonObject = JSONObject.parseObject(result);
//                    String code = jsonObject.getString("code");
//                    if ("0".equals(code)) {
//                        roomInit = jsonObject.getObject("data", RoomInit.class);
//                    } else {
//                        LOGGER.error("直播房间号不存在，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
//                    }

//                    JSONObject jsonObject = JSONObject.parseObject(message);
//                    JSONObject jsonObject =jsonObject.getObject("data", RoomInit.class);
////                            danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));


                    DanMu_MSG_Info danMuMsgInfo = JSONObject.parseObject(message, DanMu_MSG_Info.class);

                    if (danMuMsgInfo.getIsEmoticon()) {
                        // log.info("表情地址 " + danMuMsgInfo.getEmoticon_url());
                        // log.info(message);
                        try {
                            danmuWebsocket.sendMessage(DanmuDto.toJson("emoticon", danMuMsgInfo));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


//                    MainConf.webSocketProxy.send(HexUtils.fromHexString(MainConf.heartByte));

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
