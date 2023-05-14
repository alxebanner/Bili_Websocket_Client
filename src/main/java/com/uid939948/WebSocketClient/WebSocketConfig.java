package com.uid939948.WebSocketClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

// 开启WebSocket的支持，并把该类注入到spring容器中, 如果不写 就会无法注入

/**
 * 注入开启WebSocket的支持
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
