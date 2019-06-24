package com.likai.dao;

import com.likai.pojo.User;

public interface UserDao {
    User findByUserName(String username);
}
