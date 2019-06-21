package com.likai.controller;


import com.likai.constant.MessageConstant;
import com.likai.constant.RedisConstant;
import com.likai.constant.RedisMessageConstant;
import com.likai.entity.Result;
import com.likai.utils.SMSUtils;
import com.likai.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        try {
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            System.out.println(telephone + "的验证码是:" + code);
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone, 60 * 5, code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
