package com.uid939948.component.impl;

import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import com.uid939948.component.ThreadComponent;
import com.uid939948.thread.core.HeartByteThread;
import com.uid939948.thread.core.HeartOnlineThread;
import com.uid939948.thread.core.ParseMessageThread;
import com.uid939948.thread.core.TestThread;
import org.springframework.stereotype.Component;

@Component
public class ThreadComponentImpl implements ThreadComponent {
    @Override
    public boolean startParseMessageThread(CenterSetConf centerSetConf) {

        if (MainConf.parseMessageThread != null && !MainConf.parseMessageThread.getState().toString().equals("TERMINATED")) {
            MainConf.parseMessageThread.setCenterSetConf(centerSetConf);
            return false;
        }
        MainConf.parseMessageThread = new ParseMessageThread();
        MainConf.parseMessageThread.FLAG = false;
        MainConf.parseMessageThread.start();
        MainConf.parseMessageThread.setCenterSetConf(centerSetConf);
        if (MainConf.parseMessageThread != null
                && !MainConf.parseMessageThread.getState().toString().equals("TERMINATED")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean startHeartByteThread() {
        // TODO 自动生成的方法存根
        if (MainConf.heartByteThread != null) {
            return false;
        }
        MainConf.heartByteThread = new HeartByteThread();
        MainConf.heartByteThread.HFLAG = false;
        MainConf.heartByteThread.start();
        if (MainConf.heartByteThread != null
                && !MainConf.heartByteThread.getState().toString().equals("TERMINATED")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean startHeartOnlineThread() {
        // TODO 自动生成的方法存根
        if (MainConf.heartOnlineThread != null) {
            return false;
        }
        MainConf.heartOnlineThread = new HeartOnlineThread();
        MainConf.heartOnlineThread.HFLAG = false;
        MainConf.heartOnlineThread.start();
        if (MainConf.heartOnlineThread != null
                && !MainConf.heartOnlineThread.getState().toString().equals("TERMINATED")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean startTestThread() {
        // TODO 自动生成的方法存根
        if (MainConf.testThread != null) {
            return false;
        }
        MainConf.testThread = new TestThread();
        MainConf.testThread.HFLAG = false;
        MainConf.testThread.start();
        if (MainConf.testThread != null
                && !MainConf.testThread.getState().toString().equals("TERMINATED")) {
            return true;
        }
        return false;
    }

    @Override
    public void closeHeartByteThread() {
        // TODO 自动生成的方法存根
        if (MainConf.heartByteThread != null) {
            MainConf.heartByteThread.HFLAG = true;
            MainConf.heartByteThread.interrupt();
            MainConf.heartByteThread = null;
        }
    }

    @Override
    public void closeHeartOnlineThread() {
        // TODO 自动生成的方法存根
        if (MainConf.heartOnlineThread != null) {
            MainConf.heartOnlineThread.HFLAG = true;
            MainConf.heartOnlineThread.interrupt();
            MainConf.heartOnlineThread = null;
        }
    }

    @Override
    public void closeTestThread() {
        if (MainConf.testThread != null) {
            MainConf.testThread.HFLAG = true;
            MainConf.testThread.interrupt();
            MainConf.testThread = null;
        }
    }

    @Override
    public void closeParseMessageThread() {
        // TODO 自动生成的方法存根
        if (MainConf.parseMessageThread != null) {
            MainConf.parseMessageThread.FLAG = true;
            MainConf.parseMessageThread.interrupt();
            MainConf.parseMessageThread = null;
        }
    }
}
