package com.likai.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.likai.constant.MessageConstant;
import com.likai.constant.RedisConstant;
import com.likai.entity.PageResult;
import com.likai.entity.QueryPageBean;
import com.likai.entity.Result;
import com.likai.pojo.Setmeal;
import com.likai.service.SetmealService;
import com.likai.utils.QCloudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setmealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imagFile) {
        try {
            String originalFilename = imagFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(lastIndexOf - 1);
            String key = UUID.randomUUID().toString() + suffix;
            QCloudUtils.upload(key, imagFile.getBytes());

            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, key);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
