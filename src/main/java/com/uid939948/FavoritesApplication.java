package com.uid939948;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
//@MapperScan("com.uid939948.mapper")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class FavoritesApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FavoritesApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FavoritesApplication.class, args);
    }

}