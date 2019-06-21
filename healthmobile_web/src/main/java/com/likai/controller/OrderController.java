package com.likai.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.likai.constant.MessageConstant;
import com.likai.constant.RedisMessageConstant;
import com.likai.entity.Result;
import com.likai.pojo.MessageEnum;
import com.likai.service.OrderService;
import com.likai.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;


    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
             Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        try {
            String validateCode = (String) map.get("validateCode");
            String redisValidateCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            if (redisValidateCode == null || !redisValidateCode.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Result result = orderService.submit(map);
            try {
                jedisPool.getResource().del(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            } catch (Exception e) {
                System.out.println("验证码到期已经自动删除了");
                e.printStackTrace();
            }

            if (result.isFlag()){
                String orderDate = (String) map.get("orderDate");
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FULL);
        }
    }
}
