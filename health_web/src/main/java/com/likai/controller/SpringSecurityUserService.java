package com.likai.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.likai.pojo.Permission;
import com.likai.pojo.Role;
import com.likai.pojo.User;
import com.likai.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("SpringSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        if (user == null) {
            return null;
        }
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorityList);
        return userDetails;
    }
}
