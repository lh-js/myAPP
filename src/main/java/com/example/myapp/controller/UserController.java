package com.example.myapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.impl.UserServiceImpl;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

}
