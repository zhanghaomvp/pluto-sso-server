package com.example.cetcxl.cetcssoserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CETCUserDetailService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (!"cetcxl".equals(s)) {
            throw new UsernameNotFoundException(s);
        }

        return new User(
                "cetcxl",
                passwordEncoder.encode("123"),
                AuthorityUtils.createAuthorityList("ADMIN")
        );
    }
}
