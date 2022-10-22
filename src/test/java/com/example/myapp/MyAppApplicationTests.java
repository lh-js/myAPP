package com.example.myapp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Date;
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
        ValueOperations ops = stringRedisTemplate.opsForValue();
        JSONObject map = new JSONObject();
//        String accessToken=getAccessTokenFromWX();
//        map.put("accessToken",accessToken);
//        Date expirationtTime=userService.getTwoHoursLater();
        Date now=new Date();
        map.put("expirationtTime",now);
        String mapString= JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
        ops.set("accessTokenItem",mapString);// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）// 表明取的是key，value型的数据
    }

}
