package com.uid939948.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostServer implements Serializable {
    private String host;
    private Integer port;
    private Integer ws_port;
    private Integer wss_port;

	/**
	 * 获取 Websocket的url拼接地址
	 *
	 * @param hostServer Websocke地址对象
	 * @return url拼接地址
	 */
    public static String getWsUrl(HostServer hostServer) {
        return "wss://" + hostServer.getHost() + ":" + hostServer.getWss_port() + "/sub";
    }

}
