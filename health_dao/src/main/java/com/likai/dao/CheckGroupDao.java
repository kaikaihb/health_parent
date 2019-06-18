package com.likai.dao;

import com.github.pagehelper.Page;
import com.likai.pojo.CheckGroup;
import com.likai.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);

    Page<CheckGroup> selectByCondition(String queryString);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    CheckGroup findById(Integer id);

    List<CheckGroup> findAll();

    void delete(Integer id);
}
