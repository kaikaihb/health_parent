package com.likai.service;

import com.likai.entity.Result;

import java.util.Map;

public interface OrderService {
    Result submit(Map map) throws Exception;

    Map findById(Integer id);
}
