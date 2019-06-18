package com.likai.dao;

import com.github.pagehelper.Page;
import com.likai.pojo.CheckItem;

import java.util.List;

// @Repository
public interface CheckItemDao {

    /*@Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention)\n" +
            "                      values\n" +
            "        (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")*/
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    Integer[] findCheckItemIdsByCheckGroupId(Integer id);
}
