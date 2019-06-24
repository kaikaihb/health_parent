package com.likai.dao;

import com.likai.pojo.Permission;

public interface PermissionDao {
    public Permission findByRoleId(Integer id);
}
