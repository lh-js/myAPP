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
        String token= getAccessToken();
        if(token==null  || token==""){
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

    public String send() throws Exception {
        String token= checkHeaderName();
        if(token==null || token==""){
            throw new NoDataFoundException("token获取失败");
        }
        User user= checkLogin(token);
        if(user==null){
            throw new NoDataFoundException("token过期，请重新登录！");
        }
        // 1.请求URL
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+token;
        // 2.请求参数JSON格式
        JSONObject map = new JSONObject();
        JSONObject map0 = new JSONObject();
        JSONObject map1 = new JSONObject();
        JSONObject map2 = new JSONObject();
        JSONObject map3 = new JSONObject();
        JSONObject map4 = new JSONObject();
        JSONObject map5 = new JSONObject();
        map1.put("value","1");
        map2.put("value","2");
        map3.put("value","3");
        map4.put("value","2019-12-11 18:36");
        map5.put("value","2022-12-11 18:36");
        map0.put("thing3",map1);
        map0.put("thing4",map2);
        map0.put("thing9",map3);
        map0.put("time13",map4);
        map0.put("time14",map5);
        map.put("template_id", "N0liGKV50bJRH_sMx4qnNzf-DeZxreWDgcmwry_WeYM");
        map.put("page", "pages/index/index");
        map.put("touser", user.getOpenId());
        map.put("miniprogram_state", "developer");
        map.put("lang", "zh_CN");
        JSONObject mapData= JSONObject.parseObject(JSON.toJSONString(map0, SerializerFeature.DisableCircularReferenceDetect));
        map.put("data", mapData);
        System.out.println(map);
        String json = JSON.toJSONString(map);
        // 3.创建连接与设置连接参数
        URL urlObj = new URL(postUrl);
        HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Charset", "UTF-8");
        // POST请求且JSON数据,必须设置
        httpConn.setRequestProperty("Content-Type", "application/json");
        // 打开输出流,默认是false
        httpConn.setDoOutput(true);
        // 打开输入流,默认是true,可省略
        httpConn.setDoInput(true);
        // 4.从HttpURLConnection获取输出流和写数据
        OutputStream oStream = httpConn.getOutputStream();
        oStream.write(json.getBytes());
        oStream.flush();
        // 5.发起http调用(getInputStream触发http请求)
        if (httpConn.getResponseCode() != 200) {
            throw new Exception("调用服务端异常.");
        }
        // 6.从HttpURLConnection获取输入流和读数据
        BufferedReader br = new BufferedReader(
                new InputStreamReader(httpConn.getInputStream()));
        String resultData = br.readLine();
        System.out.println("从服务端返回结果: " + resultData);
        // 7.关闭HttpURLConnection连接
        httpConn.disconnect();
        return resultData;
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
        Date now=new Date();
        if(now.compareTo(userInfo.getExpirationttime())<0){
            return userInfo;
        }

        return null;
    }

}
