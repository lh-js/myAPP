package com.example.myapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyAppApplicationTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //操作k-v都是对象对象的
    @Test
    void contextLoads() {
        ValueOperations ops = stringRedisTemplate.opsForValue();    // 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的
        ops.set("address1","zhengzhou");// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）// 表明取的是key，value型的数据
        Object o = ops.get("address1");  // 获取Redis数据库中key为address1对应的value数据
        stringRedisTemplate.delete("address1");
        System.out.println(o);
    }

}
