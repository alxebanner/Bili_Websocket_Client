package com.uid939948.Controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 反向推送弹幕集合
 */
@Controller
@ServerEndpoint("/danmu/sub")
public class DanmuWebsocket {
    private final Logger LOGGER = LogManager.getLogger(DanmuWebsocket.class);
    private static CopyOnWriteArraySet<DanmuWebsocket> webSocketServers = new CopyOnWriteArraySet<>();
    private Session session;

    //连接超时
    public static final long MAX_TIME_OUT = 24 * 60 * 1000;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketServers.add(this);
        // * 设置session 过期时间， 防止session由于有默认时间导致会自动过期
        session.setMaxIdleTimeout(MAX_TIME_OUT);

//        // 发送成功连接 改为从前端获取
//        for (DanmuWebsocket danmuWebsocket : webSocketServers) {
//            try {
//                Long roomId = MainConf.ROOMID;
//                DanMu_MSG_Info danMuMsgInfo = new DanMu_MSG_Info();
//
//                if (ObjectUtil.isNotEmpty(MainConf.UID)) {
//                    UserInfo userInfo = HttpRoomUtil.httpGetUserInfo(MainConf.UID);
//                    danMuMsgInfo.setFaceUrl(userInfo.getFace());
//                    danMuMsgInfo.setUname(userInfo.getName());
//                }
//                danMuMsgInfo.setMessage("连接房间 " + roomId + " 成功,请稍后");
//                danmuWebsocket.sendMessage(DanmuDto.toJson("danmu", danMuMsgInfo));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

    }

    @OnClose
    public void onClose() {
        webSocketServers.remove(this);
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        // 反向发送
        for (DanmuWebsocket danmuWebsocket : webSocketServers) {
            danmuWebsocket.session.getBasicRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error(error);
    }

    public void sendMessage(String message) throws IOException {
        for (DanmuWebsocket danmuWebsocket : webSocketServers) {
            synchronized (danmuWebsocket.session) {
                danmuWebsocket.session.getBasicRemote().sendText(message);
            }

        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
