package com.example.myapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapp.dao.UserDAO;
import com.example.myapp.dao.daoVO.UserExtDAO;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.UserService;
import com.example.myapp.util.AESUtils;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserExtDAO userExtDAO;

    @Override
    public QueryResult<User> selectAllUser(QueryRequest<User> queryRequest){
        Page page= PageHelper.startPage(queryRequest.getPageCondition().getPageNo(),queryRequest.getPageCondition().getPageSize());
        userExtDAO.selectAll(queryRequest.getCondition());
        return new QueryResult<>(page);
    }

    @Override
    public UserVO userLogin(UserVO user) throws Exception {
        String APPID="wxc509139d5e64d413";
        String SECRET="34ae9c94e749dc6cc930794ea23cb588";
        String JSCODE=user.getCode();
        String TYPE="authorization_code";
        String getUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+JSCODE+"&grant_type="+TYPE;
        URL urlObj = new URL(getUrl);
        HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("Charset", "UTF-8");
        // 3.发起http调用(getInputStream触发http请求)
        if (httpConn.getResponseCode() != 200) {
            throw new Exception("调用服务端异常.");
        }
        // 4.从HttpURLConnection获取输入流和读数据
        BufferedReader br = new BufferedReader(
                new InputStreamReader(httpConn.getInputStream()));
        String resultData = br.readLine();
        System.out.println("从服务端返回结果: " +resultData);
        JSONObject jsonObject =  JSONObject.parseObject(resultData);
        AESUtils aesUtils=new AESUtils();
        // 5.关闭HttpURLConnection连接
        httpConn.disconnect();
        user.setToken("Bearer"+aesUtils.encode(jsonObject.getString("session_key")));
        user.setOpenId(aesUtils.encode(jsonObject.getString("openid")));
        user.setCode(aesUtils.encode(user.getCode()));

        if(checkIsRegister(aesUtils.encode(jsonObject.getString("openid")))==null){
            registerUser(user);
        }

        return user;
    }

    User checkIsRegister(String openId){
        return userExtDAO.selectByPrimaryKey((openId));
    }

    int registerUser(User user){
        return userExtDAO.insertUser(user);
    }

}
