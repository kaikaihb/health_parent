package com.likai.dao;

import com.likai.pojo.Role;

public interface RoleDao {
    public Role findByUserId(Integer id);
}
