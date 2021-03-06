package com.example.cetcxl.cetcssoserver.common.config;

import com.example.cetcxl.cetcssoserver.service.CETCUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    CETCUserDetailService userDetailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenStore redisTokenStore;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("cetcxl-client1")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://localhost:8082/clent1/login")
                .accessTokenValiditySeconds(7200)
                .and()
                .withClient("cetcxl-client2")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://localhost:8083/clent2/login")
                .accessTokenValiditySeconds(7200)
                .and()
                .withClient("cetcxl-client3")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://localhost:8084/clent3/login/oauth2/code/cetcxl")
                .accessTokenValiditySeconds(7200)
                .and()
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(redisTokenStore)
                .userDetailsService(userDetailService)
        ;
    }

}
