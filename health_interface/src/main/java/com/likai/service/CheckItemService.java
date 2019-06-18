package com.likai.service;



import com.likai.entity.PageResult;
import com.likai.entity.QueryPageBean;
import com.likai.entity.Result;
import com.likai.pojo.CheckItem;

import java.util.List;

//检查项服务接口
public interface CheckItemService {
    public void add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void delete(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    Integer[] findCheckItemIdsByCheckGroupId(Integer id);
}
