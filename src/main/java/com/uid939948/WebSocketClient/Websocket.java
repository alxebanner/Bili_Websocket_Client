package com.uid939948.WebSocketClient;

import com.uid939948.Conf.MainConf;
import com.uid939948.DO.Room;
import com.uid939948.Tools.HandleWebsocketPackage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Websocket客户端
 * Websocket连接bilibili服务器
 */
public class Websocket extends WebSocketClient {
    private static Logger LOGGER = LogManager.getLogger(Websocket.class);

    public Websocket(String url, Room room) throws URISyntaxException {
        super(new URI(url));
        LOGGER.info("已尝试连接至服务器地址:" + url + ";真实房间号为:" + room.getRoomId() + ";主播名字为:" + room.getUname());
        // TODO 自动生成的构造函数存根
    }


    //建立连接成功调用
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // TODO 自动生成的方法存根
        LOGGER.info("websocket connect open(连接窗口打开)");
    }

    //收到客户端信息
    @Override
    public void onMessage(ByteBuffer message) {
        // 这里是收到了二进制内容请求，需要进一步解析
        if (MainConf.parseMessageThread != null && !MainConf.parseMessageThread.FLAG) {
            try {
                HandleWebsocketPackage.handle_Message(message);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                LOGGER.info("解析错误日志生成，请将log底下文件发给管理员,或github开issue发送错误" + e);
            }
        }
        // 实现原理 通过房间号获取弹幕连接地址  有多个，可以随机获取一个
        // 如果存在登录， 则要获取用户基本信息  包括 能发送的弹幕最大数量
        // 然后 发送心跳包 就能收获弹幕燃火启动线程
    }

    //连接关闭
    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.info("websocket connect close(连接已经断开)，纠错码:" + code);

        // todo 重连
    }

    @Override
    public void onError(Exception ex) {
		LOGGER.error("[错误信息，请将log文件下的日志发送给管理员]websocket connect error,message:" + ex.getMessage());
		LOGGER.info("尝试重新链接");
    }

    @Override
    public void onMessage(String message) {
    }
}
