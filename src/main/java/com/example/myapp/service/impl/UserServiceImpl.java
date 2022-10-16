package com.example.myapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.myapp.dao.UserDAO;
import com.example.myapp.dao.daoVO.UserExtDAO;
import com.example.myapp.exception.NoDataFoundException;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.UserService;
import com.example.myapp.util.AESUtils;
import com.example.myapp.util.HttpUtils;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

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
        checkHeaderName();
        String openId= getOpenId(user.getCode());
        if(openId==null  || openId==""){
            throw new NoDataFoundException("openId获取失败");
        }
//        String token= getAccessToken();
//        if(token==null  || token==""){
//            throw new NoDataFoundException("token获取失败");
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis() ;
        System.out.print("当前时间:");
        System.out.println(dateFormat.format(currentTime));
        currentTime +=110*60*1000;
        Date date=new Date(currentTime);
        System.out.print("2小时后时间:");
        System.out.println(dateFormat.format(date));

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setOpenId(openId);
        user.setCode(user.getCode());
        user.setExpirationttime(date);

        if(checkIsRegister(openId)==null){
            registerUser(user);
        }else {
            updateExpirationtTime(user);
        }

        return user;
    }

    @Override
    public User getUserInfo(){
        String token= checkHeaderName();
        if(token==null || token==""){
            throw new NoDataFoundException("token获取失败");
        }
        User user= checkLogin(token);
        if(user==null){
            throw new NoDataFoundException("token过期，请重新登录！");
        }

        return user;
    }

    String getOpenId(String code) throws Exception {
        String APPID="wxc509139d5e64d413";
        String SECRET="34ae9c94e749dc6cc930794ea23cb588";
        String JSCODE=code;
        String TYPE="authorization_code";
        String getUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+JSCODE+"&grant_type="+TYPE;
        JSONObject jsonObject= HttpUtils.getHttp(getUrl);
        return jsonObject.getString("openid");
    }

    String getAccessToken() throws Exception {
        String APPID="wxc509139d5e64d413";
        String SECRET="34ae9c94e749dc6cc930794ea23cb588";
        String TYPE="client_credential";
        String getUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+TYPE+"&appid="+APPID+"&secret="+SECRET;
        JSONObject jsonObject = HttpUtils.getHttp(getUrl);
        return jsonObject.getString("access_token");
    }

    /**
     * 获取判断请求头的参数
     * @return
     */
    String checkHeaderName(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(ra)){
            System.out.println("服务里RequestAttributes对象为空");
            return null;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        String token = request.getHeader("authorization");
        System.out.println("accesstoken:"+token);
        return token;
    }

    User checkIsRegister(String openId){
        return userExtDAO.selectByPrimaryKey((openId));
    }

    int registerUser(User user){
        return userExtDAO.insertUser(user);
    }

    int updateExpirationtTime(User user){
        return userExtDAO.updateByPrimaryKey(user);
    }

     User checkLogin(String token){
        User userInfo= userExtDAO.selectByToken(token);
        if(userInfo==null) {
            throw new NoDataFoundException("用户不存在，请重新登录");
        }
        Date now=new Date();
        if(now.compareTo(userInfo.getExpirationttime())<0){
            return userInfo;
        }

        return null;
    }

}
