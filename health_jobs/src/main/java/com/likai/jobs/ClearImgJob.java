package com.likai.jobs;

import com.likai.constant.RedisConstant;
import com.likai.utils.QCloudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String pic = iterator.next();
            QCloudUtils.deleteObject(pic);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
        }
    }
}
