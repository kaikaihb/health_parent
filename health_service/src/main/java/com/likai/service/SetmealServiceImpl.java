package com.likai.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.likai.constant.RedisConstant;
import com.likai.dao.SetmealDao;
import com.likai.entity.PageResult;
import com.likai.entity.QueryPageBean;
import com.likai.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.add(setmeal);
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            setSetmealAndCheckGroup(setmeal.getId(), checkGroupIds);
        }
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    private void setSetmealAndCheckGroup(Integer id, Integer[] checkGroupIds) {
        for (Integer checkGroupId : checkGroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setMealId", id);
            map.put("checkGroupId", checkGroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
