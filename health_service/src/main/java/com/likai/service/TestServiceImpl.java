package com.likai.service;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String sayHello() {
        return "hello";
    }
}
