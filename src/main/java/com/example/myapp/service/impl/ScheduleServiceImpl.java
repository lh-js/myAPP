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
        User user = userService.updateToken();
        System.out.println(user);
        record.setOpenId(user.getOpenId());
        int i=scheduleExtDAO.insert(record);
        if(i==0){
            throw new NoDataFoundException("日程添加失败");
        }
        return i;
    }

    @Override
    public List<Schedule> selectByOpenId() {
        User user = userService.updateToken();
        System.out.println(user);
        List<Schedule> list= scheduleExtDAO.selectByOpenId(user.getOpenId());
        if(list==null){
            throw new NoDataFoundException("日程列表获取失败");
        }
        return list;
    }

    @Override
    public Schedule getScheduleById(Integer id){
        User user = userService.updateToken();
        System.out.println(user);
        Schedule schedule=scheduleExtDAO.selectByPrimaryKey(id);
        if(schedule==null){
            throw new NoDataFoundException("日程详情获取失败");
        }
        return schedule;
    }

    @Override
    public int updateSchedule(Schedule schedule){
        User user = userService.updateToken();
        System.out.println(user);
        schedule.setOpenId(user.getOpenId());
        int i= scheduleExtDAO.updateByPrimaryKey(schedule);
        if(i==0){
            throw new NoDataFoundException("日程更新失败");
        }
        return i;
    }

    JSONObject send(String remindThing,String thingAddress, String startTime,String endTime,String remark) throws Exception {
        User user= userService.updateToken();
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
