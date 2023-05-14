package com.uid939948.config;

import com.uid939948.Service.SetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterExcelConfig {
    /**
     * 初始化相互
     */
    @Autowired
    private SetService setService;

    @Bean
    public void init() {
        setService.init();
    }
}
