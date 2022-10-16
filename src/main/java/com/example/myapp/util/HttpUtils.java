package com.example.myapp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    public static JSONObject  getHttp(String url) throws Exception {
        URL urlObj = new URL(url);
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
        return jsonObject;
    }

    public static JSONObject postHttp(String url, JSONObject param) throws Exception {

        // 2.请求参数JSON格式
        System.out.println(param);
        String json = JSON.toJSONString(param);
        // 3.创建连接与设置连接参数
        URL urlObj = new URL(url);
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
        JSONObject jsonObject =  JSONObject.parseObject(resultData);
        return jsonObject;
    }
}
