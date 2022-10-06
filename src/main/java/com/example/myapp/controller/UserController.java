package com.example.myapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.impl.UserServiceImpl;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceImpl userServiceImpl;

    @PostMapping("/selectAll")
    public QueryResult<User> selectAll(@RequestBody QueryRequest<User> queryRequest) {
        return userServiceImpl.selectAllUser(queryRequest);
    }

    @PostMapping("/login")
    public UserVO login(@RequestBody UserVO user) throws Exception {
        return userServiceImpl.userLogin(user);
    }

    @PostMapping("/getUserInfo")
    public User getUserInfo(){
        return userServiceImpl.getUserInfo();
    }

    @PostMapping("/send")
    public String send() throws Exception {
       return userServiceImpl.send();
    }

}
