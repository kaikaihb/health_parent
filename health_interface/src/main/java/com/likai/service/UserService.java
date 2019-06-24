package com.likai.service;

import com.likai.pojo.User;

public interface UserService {
    User findByUserName(String username);
}
