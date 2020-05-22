package com.example.cetcxl.cetcssoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
public class CetcSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CetcSsoServerApplication.class, args);
    }

}
