package com.example.cetcxl.cetcssoserver.common.config;

import com.alibaba.fastjson.JSON;
import com.example.cetcxl.cetcssoserver.service.CETCUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.io.PrintWriter;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CETCUserDetailService userDetailService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .requestMatchers().antMatchers("/login").antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                /*.successHandler(
                        (req, res, auth) -> {
                            Object principal = auth.getPrincipal();
                            res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            PrintWriter out = res.getWriter();
                            out.write(JSON.toJSONString(principal));
                            out.flush();
                            out.close();
                        }
                )*/
                .failureHandler(
                        (req, res, e) -> {
                            res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            PrintWriter out = res.getWriter();
                            out.write(e.getMessage());
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                /*.exceptionHandling()
                .authenticationEntryPoint(
                        (req, res, e) -> {
                            res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            PrintWriter out = res.getWriter();
                            out.write("尚未登录，请先登录");
                            out.flush();
                            out.close();
                        }
                )
                .and()*/
        ;
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .and()
        ;
    }
}
