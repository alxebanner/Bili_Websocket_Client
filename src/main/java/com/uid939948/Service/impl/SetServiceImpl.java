package com.uid939948.Service.impl;

import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import com.uid939948.Service.ClientService;
import com.uid939948.Service.SetService;
import com.uid939948.Until.BASE64Encoder;
import com.uid939948.Until.ProFileTools;
import com.uid939948.component.ThreadComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Hashtable;

@Service
@Slf4j
public class SetServiceImpl implements SetService {
    @Resource
    private Environment environment;


    private ThreadComponent threadComponent;


    @Override
    public void init() {
        // 第一步  加载配置 获取端口号
        String port = environment.getProperty("server.port");
        log.info("openBrowser and port is " + port);
        openBrowser(port);
    }

    @Override
    public void holdSet(CenterSetConf centerSetConf) {
        synchronized (centerSetConf) {
            changeSet(MainConf.centerSetConf);
//            SchedulingRunnableUtil task = new SchedulingRunnableUtil("dosignTask", "dosign");
//            SchedulingRunnableUtil dakatask = new SchedulingRunnableUtil("dosignTask", "clockin");
//            SchedulingRunnableUtil autoSendGiftTask = new SchedulingRunnableUtil("dosignTask", "autosendgift");
            //每日签到
//            if (MainConf.centerSetConf.is_dosign()) {
//                //判断签到
//                boolean flag = CurrencyTools.signNow();
//                if (flag) {
//                    changeSet(MainConf.centerSetConf);
//                }
////                if (MainConf.is_sign) {
////                    HttpUserData.httpGetDoSign();
////                    MainConf.is_sign = true;
////                }
//                if (!taskRegisterComponent.hasTask(task)) {
//                    taskRegisterComponent.addTask(task, CurrencyTools.dateStringToCron(centerSetConf.getSign_time()));
//                }
//            } else {
//                try {
//                    taskRegisterComponent.removeTask(task);
//                } catch (Exception e) {
//                    // TODO 自动生成的 catch 块
//                    LOGGER.error("清理定时任务错误：" + e);
//                }
//            }
//            //每日打卡
//            if (centerSetConf.getClock_in().is_open()) {
//                //移除
//                //这里开启一个匿名线程用于打卡
////                new Thread(() -> {
////                    List<UserMedal> userMedals = CurrencyTools.getAllUserMedals();
////                    int max = CurrencyTools.clockIn(userMedals);
////                    if (max == userMedals.size()) {
////                        HttpOtherData.httpPOSTSetClockInRecord();
////                    }
////                }).start();
//                if (!taskRegisterComponent.hasTask(dakatask)) {
//                    taskRegisterComponent.addTask(dakatask, CurrencyTools.dateStringToCron(centerSetConf.getClock_in().getTime()));
//                }
//            } else {
//                try {
//                    taskRegisterComponent.removeTask(dakatask);
//                } catch (Exception e) {
//                    // TODO 自动生成的 catch 块
//                    LOGGER.error("清理定时任务错误：" + e);
//                }
//            }
//            //每日定时自动送礼
//            if (centerSetConf.getAuto_gift().is_open()) {
//                if (!taskRegisterComponent.hasTask(autoSendGiftTask)) {
//                    taskRegisterComponent.addTask(autoSendGiftTask, CurrencyTools.dateStringToCron(centerSetConf.getAuto_gift().getTime()));
//                }
//            } else {
//                try {
//                    taskRegisterComponent.removeTask(autoSendGiftTask);
//                } catch (Exception e) {
//                    // TODO 自动生成的 catch 块
//                    LOGGER.error("清理定时任务错误：" + e);
//                }
//            }

            //need roomid set
            if (MainConf.ROOMID == null || MainConf.ROOMID <= 0) {
                return;
            }
            if (MainConf.webSocketProxy == null) {
                return;
            }

            // 如果已经启动 则返回
            if (MainConf.webSocketProxy != null && !MainConf.webSocketProxy.isOpen()) return;

            // parsemessagethread start
            threadComponent.startParseMessageThread(
                    centerSetConf);
            // logthread

//            if (centerSetConf.is_log()) {
//                threadComponent.startLogThread();
//            } else {
//                threadComponent.closeLogThread();
//            }
//
            // need login
            if (!StringUtils.isEmpty(MainConf.USERCOOKIE)) {
                // advertthread
//                centerSetConf.getAdvert().start(threadComponent);

//            if (centerSetConf.getAdvert().is_live_open()) {
//                if (MainConf.lIVE_STATUS != 1) {
//                    threadComponent.closeAdvertThread();
//                } else {
//                    if (centerSetConf.getAdvert().is_open()) {
//                        threadComponent.startAdvertThread(centerSetConf);
//                    } else {
//                        threadComponent.setAdvertThread(centerSetConf);
//                        threadComponent.closeAdvertThread();
//                    }
//                }
//            } else {
//                if (centerSetConf.getAdvert().is_open()) {
//                    threadComponent.startAdvertThread(centerSetConf);
//                } else {
//                    threadComponent.setAdvertThread(centerSetConf);
//                    threadComponent.closeAdvertThread();
//                }
//            }
                // autoreplythread
//                centerSetConf.getReply().start(threadComponent);

//            if (centerSetConf.getReply().is_live_open()) {
//                if (MainConf.lIVE_STATUS != 1) {
//                    threadComponent.closeAutoReplyThread();
//                } else {
//                    if (centerSetConf.getReply().is_open()) {
//                        threadComponent.startAutoReplyThread(centerSetConf);
//                    } else {
//                        threadComponent.setAutoReplyThread(centerSetConf);
//                        threadComponent.closeAutoReplyThread();
//                    }
//                }
//            } else {
//                if (centerSetConf.getReply().is_open()) {
//                    threadComponent.startAutoReplyThread(centerSetConf);
//                } else {
//                    threadComponent.setAutoReplyThread(centerSetConf);
//                    threadComponent.closeAutoReplyThread();
//                }
//            }
                // useronlinethread && smallHeartThread
//                if (centerSetConf.is_online()) {
//                    threadComponent.startUserOnlineThread();
//                    if (centerSetConf.is_sh() && MainConf.lIVE_STATUS == 1) {
//                        threadComponent.startSmallHeartThread();
//                    } else {
//                        threadComponent.closeSmallHeartThread();
//                    }
//                } else {
//                    threadComponent.closeSmallHeartThread();
//                    threadComponent.closeUserOnlineThread();
//                }
//                // sendbarragethread
//                if (MainConf.advertThread == null
//                        && !MainConf.centerSetConf.getFollow().is_followThank()
//                        && !MainConf.centerSetConf.getWelcome().is_welcomeThank()
//                        && !MainConf.centerSetConf.getThank_gift().is_giftThank()
//                        && MainConf.autoReplyThread == null) {
//                    threadComponent.closeSendBarrageThread();
//                    MainConf.init_send();
//                } else {
//                    threadComponent.startSendBarrageThread();
//                }
            } else {
                //没有登录
//                MainConf.init_user();
//                threadComponent.closeUser(false);
            }
//            if (MainConf.webSocketProxy != null && !MainConf.webSocketProxy.isOpen()) {
//                threadComponent.closeAll();
//                MainConf.init_all();
//            }
        }
    }

    public void changeSet(CenterSetConf centerSetConf) {
        synchronized (centerSetConf) {
            Hashtable<String, String> hashtable = new Hashtable<String, String>();
            BASE64Encoder base64Encoder = new BASE64Encoder();

            hashtable.put("set", base64Encoder.encode(centerSetConf.toJson().getBytes()));
            ProFileTools.write(hashtable, "DanmujiProfile");
            log.info("保存配置文件成功");
            base64Encoder = null;
            hashtable.clear();
        }
    }

    private void openBrowser(String port) {




        try {
            // 打开浏览器
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:" + port);
        } catch (IOException e) {



            log.info("自动打开浏览器错误:当前系统缺少rundll32 url.dll组件或者不是window系统，无法自动启动默认浏览器打开配置页面，请手动打开浏览器地址栏输入http://127.0.0.1:" + port + "进行配置");
        }
    }


    @Autowired
    public void setThreadComponent(ThreadComponent threadComponent) {
        this.threadComponent = threadComponent;
    }
}
