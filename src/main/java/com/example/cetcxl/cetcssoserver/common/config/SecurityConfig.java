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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;

import java.io.PrintWriter;

@EnableWebSecurity
@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CETCUserDetailService userDetailService;

    @Autowired
    SessionRegistry sessionRegistry;

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
                .requestMatchers().antMatchers("/login").antMatchers("/logout").antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(
                        (req, res, auth) -> {
                            Object principal = auth.getPrincipal();
                            res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            PrintWriter out = res.getWriter();
                            out.write(JSON.toJSONString(principal));
                            out.flush();
                            out.close();
                        }
                )
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
                .logout()
                .invalidateHttpSession(true).clearAuthentication(true)
                .logoutSuccessHandler(
                        (request, response, authentication) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            PrintWriter out = response.getWriter();
                            out.write("ok");
                            out.flush();
                            out.close();
                        }
                )
                .deleteCookies()
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
                .sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry).maxSessionsPreventsLogin(true)
                .and()
                .and()
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
