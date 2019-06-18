package com.likai.service;

import com.likai.entity.PageResult;
import com.likai.entity.QueryPageBean;
import com.likai.pojo.Setmeal;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(QueryPageBean queryPageBean);
}
