package com.likai.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.likai.constant.MessageConstant;
import com.likai.constant.RedisMessageConstant;
import com.likai.entity.Result;
import com.likai.pojo.Member;
import com.likai.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletRequest request){
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String redisValidateCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone);

            if (validateCode == null || !redisValidateCode.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            // 3. 如果校验通过
            // - 判断是否是会员,
            Member member  = memberService.findByTelephone(telephone);
            if(member == null){
                //不是会员, 自动注册为会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.register(member);
            }
            // - 保存用户的登录状态( CAS或者自己手动签发token )
            request.getSession().setAttribute("member",member);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
