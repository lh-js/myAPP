package com.example.myapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapp.dao.UserDAO;
import com.example.myapp.dao.daoVO.UserExtDAO;
import com.example.myapp.exception.NoDataFoundException;
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
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        String openId= getOpenId(user.getCode());
        if(openId==null){
            throw new NoDataFoundException("openId获取失败");
        }
        String token= getAccessToken();
        if(token==null){
            throw new NoDataFoundException("token获取失败");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis() ;
        System.out.print("当前时间:");
        System.out.println(dateFormat.format(currentTime));
        currentTime +=110*60*1000;
        Date date=new Date(currentTime);
        System.out.print("2小时后时间:");
        System.out.println(dateFormat.format(date));

        user.setToken(token);
        user.setOpenId(openId);
        user.setCode(user.getCode());
        System.out.println(date);
        user.setExpirationttime(date);

        if(checkIsRegister(openId)==null){
            registerUser(user);
        }

        return user;
    }

    String getOpenId(String code) throws Exception {
        String APPID="wxc509139d5e64d413";
        String SECRET="34ae9c94e749dc6cc930794ea23cb588";
        String JSCODE=code;
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
        // 5.关闭HttpURLConnection连接
        httpConn.disconnect();
        JSONObject jsonObject =  JSONObject.parseObject(resultData);
        return jsonObject.getString("openid");
    }

    String getAccessToken() throws Exception {
        String APPID="wxc509139d5e64d413";
        String SECRET="34ae9c94e749dc6cc930794ea23cb588";
        String TYPE="client_credential";
        String getUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+TYPE+"&appid="+APPID+"&secret="+SECRET;
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
        // 5.关闭HttpURLConnection连接
        httpConn.disconnect();
        JSONObject jsonObject =  JSONObject.parseObject(resultData);
        return jsonObject.getString("access_token");
    }

    User checkIsRegister(String openId){
        return userExtDAO.selectByPrimaryKey((openId));
    }

    int registerUser(User user){
        return userExtDAO.insertUser(user);
    }

}
