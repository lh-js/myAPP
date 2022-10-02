package com.example.myapp.service;

import com.alibaba.fastjson.JSONObject;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;

import java.io.IOException;
import java.net.MalformedURLException;

public interface UserService {

    QueryResult<User> selectAllUser(QueryRequest<User> queryRequest);

    UserVO userLogin(UserVO user) throws Exception;
}
