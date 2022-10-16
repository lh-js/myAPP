package com.example.myapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.myapp.dao.daoVO.ScheduleExtDAO;
import com.example.myapp.dao.daoVO.UserExtDAO;
import com.example.myapp.exception.NoDataFoundException;
import com.example.myapp.model.Schedule;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.ScheduleVO;
import com.example.myapp.service.ScheduleService;
import com.example.myapp.util.HttpUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleExtDAO scheduleExtDAO;

    @Resource
    private UserServiceImpl userService;

    @Override
    public int insertSchedule(Schedule record) {
        User user = userService.getUserInfo();
        System.out.println(user);
        if(user==null){
            throw new NoDataFoundException("用户信息获取失败");
        }
        record.setOpenId(user.getOpenId());
        return scheduleExtDAO.insert(record);
    }

    @Override
    public List<Schedule> selectByOpenId() {
        User user = userService.getUserInfo();
        System.out.println(user);
        if(user==null){
            throw new NoDataFoundException("用户信息获取失败");
        }
        return scheduleExtDAO.selectByOpenId(user.getOpenId());
    }

    @Override
    public Schedule getScheduleById(Integer id){
        User user = userService.getUserInfo();
        System.out.println(user);
        if(user==null){
            throw new NoDataFoundException("用户信息获取失败");
        }
        return scheduleExtDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateSchedule(Schedule schedule){
        User user = userService.getUserInfo();
        System.out.println(user);
        if(user==null){
            throw new NoDataFoundException("用户信息获取失败");
        }
        schedule.setOpenId(user.getOpenId());
        return scheduleExtDAO.updateByPrimaryKey(schedule);
    }

    JSONObject send(String remindThing,String thingAddress, String startTime,String endTime,String remark) throws Exception {
        User user= userService.getUserInfo();
        // 1.请求URL
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+user.getToken();
        // 2.请求参数JSON格式
        JSONObject map = new JSONObject();
        JSONObject map0 = new JSONObject();
        JSONObject map1 = new JSONObject();
        JSONObject map2 = new JSONObject();
        JSONObject map3 = new JSONObject();
        JSONObject map4 = new JSONObject();
        JSONObject map5 = new JSONObject();
        map1.put("value",remindThing);
        map2.put("value",thingAddress);
        map3.put("value",remark);
        map4.put("value",startTime);
        map5.put("value",endTime);
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
        return HttpUtils.postHttp(postUrl,map);
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                //do Something
//                send()
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
