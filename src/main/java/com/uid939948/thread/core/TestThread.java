package com.uid939948.thread.core;

import com.uid939948.Conf.MainConf;
import com.uid939948.Controller.DanmuWebsocket;

import com.uid939948.Service.TestMessageService;
import com.uid939948.Until.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestThread extends Thread {
    public static final int MILLIS = 1000;
    public volatile boolean HFLAG = false;
    private DanmuWebsocket danmuWebsocket = SpringUtils.getBean(DanmuWebsocket.class);

    private TestMessageService testMessageService = SpringUtils.getBean(TestMessageService.class);



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

                    Thread.sleep(MILLIS);
                    testMessageService.danmuMessage("非舰队用户", null, "普通弹幕", 0);

                    Thread.sleep(MILLIS);
                    testMessageService.danmuMessage("超长舰队用户名超长舰队用户名", null, "超长的舰队用户超长的舰队用户超长的舰队用户超长的舰队用户弹幕", 3);

//                    Thread.sleep(MILLIS);
//                    testMessageService.giftMessage("超长舰队用户名", null, "超长的舰队用户弹幕,超长的舰队用户弹幕,超长的舰队用户弹幕,", 3);
//
//                    Thread.sleep(MILLIS);
//                    testMessageService.toastMessage("新增舰长的用户", null, "超长的舰队用户弹幕,超长的舰队用户弹幕,超长的舰队用户弹幕,", 3);
//

//                    List<String> testDanmuMessageList = MainConf.testDanmuMessageList;
//                    // 打乱顺序
//                    Collections.shuffle(testDanmuMessageList);
//
//                    String message = testDanmuMessageList.stream().findAny().get();
//
//
////                    String result = HttpUtil.doGetWithHeader(url, headers);
////                    JSONObject jsonObject = JSONObject.parseObject(result);
////                    String code = jsonObject.getString("code");
////                    if ("0".equals(code)) {
////                        roomInit = jsonObject.getObject("data", RoomInit.class);
////                    } else {
////                        LOGGER.error("直播房间号不存在，或者未知错误，请尝试更换房间号,原因:" + jsonObject.getString("message"));
////                    }
//
////                    JSONObject jsonObject = JSONObject.parseObject(message);
////                    JSONObject jsonObject =jsonObject.getObject("data", RoomInit.class);
//////                            danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));
//
//
//                    DanMu_MSG_Info danMuMsgInfo = JSONObject.parseObject(message, DanMu_MSG_Info.class);
//
//                    if (danMuMsgInfo.getIsEmoticon()) {
//                        // log.info("表情地址 " + danMuMsgInfo.getEmoticon_url());
//                        // log.info(message);
//                        try {
//                            danmuWebsocket.sendMessage(DanmuDto.toJson("emoticon", danMuMsgInfo));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    } else {
//                        try {
//                            danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }

                } catch (Exception e) {
                }
            }
        }
    }
//
//    private static int getRandomNumberInRange(int min, int max) {
//        if (min >= max) {
//            throw new IllegalArgumentException("max must be greater than min");
//        }
//        Random r = new Random();
//        return r.nextInt((max - min) + 1) + min;
//    }
}
