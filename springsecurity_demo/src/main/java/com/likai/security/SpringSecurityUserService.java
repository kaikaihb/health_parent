package com.likai.security;

import com.likai.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class SpringSecurityUserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }

    private User findByUserName(String username) {
        if ("admin".equals(username)) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(encoder.encode("123456"));
            return user;
        }
        return null;
    }
}
