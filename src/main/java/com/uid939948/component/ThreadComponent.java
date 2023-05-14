package com.uid939948.component;


import com.uid939948.Conf.CenterSetConf;

public interface ThreadComponent {

    // 开启处理弹幕包线程 core
    boolean startParseMessageThread(
            CenterSetConf centerSetConf);

    // 开启心跳线程 core
    boolean startHeartByteThread();

    boolean startHeartOnlineThread();

    boolean startTestThread();


    /**
     *  关闭处理弹幕包线程 core
     */
    void closeParseMessageThread();

    // 关闭心跳线程 core
    void closeHeartByteThread();

    void closeHeartOnlineThread();

    void closeTestThread();
}
