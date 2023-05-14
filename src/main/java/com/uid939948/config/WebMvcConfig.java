package com.uid939948.config;

import com.uid939948.Conf.MainConf;
import com.uid939948.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private DanmujiInitConfig danmujiInitConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        danmujiInitConfig.init();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver()
                .addVersionStrategy(new ContentVersionStrategy(), "/**");

        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(2592000)
                .resourceChain(true).addResolver(versionResourceResolver);
    }

    @Autowired
    public void setDanmujiConfig(DanmujiInitConfig danmujiInitConfig) {
        this.danmujiInitConfig = danmujiInitConfig;
    }
}

