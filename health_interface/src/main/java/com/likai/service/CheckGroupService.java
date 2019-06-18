package com.likai.service;

import com.likai.entity.PageResult;
import com.likai.entity.QueryPageBean;
import com.likai.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    CheckGroup findById(Integer id);

    List<CheckGroup> findAll();

    void delete(Integer id);
}
