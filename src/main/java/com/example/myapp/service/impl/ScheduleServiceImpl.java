package com.example.myapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.myapp.dao.daoVO.ScheduleExtDAO;
import com.example.myapp.exception.NoDataFoundException;
import com.example.myapp.model.Schedule;
import com.example.myapp.model.User;
import com.example.myapp.service.ScheduleService;
import com.example.myapp.util.HttpUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleExtDAO scheduleExtDAO;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int insertSchedule(Schedule record ,User user) throws Exception {
        record.setOpenId(user.getOpenId());
        int i=scheduleExtDAO.insert(record);
        if(i==0){
            throw new NoDataFoundException("日程添加失败");
        }
        send(record,user);
        return i;
    }

    @Override
    public List<Schedule> selectByOpenId(User user) {
        List<Schedule> list= scheduleExtDAO.selectByOpenId(user.getOpenId());
        if(list==null){
            throw new NoDataFoundException("日程列表获取失败");
        }
        return list;
    }

    @Override
    public Schedule getScheduleById(Integer id){
        Schedule schedule=scheduleExtDAO.selectByPrimaryKey(id);
        if(schedule==null){
            throw new NoDataFoundException("日程详情获取失败");
        }
        return schedule;
    }

    @Override
    public int updateSchedule(Schedule schedule,User user){
        schedule.setOpenId(user.getOpenId());
        int i= scheduleExtDAO.updateByPrimaryKey(schedule);
        if(i==0){
            throw new NoDataFoundException("日程更新失败");
        }
        return i;
    }

    String getAccessTokenFromWX() throws Exception {
        String APPID = "wxc509139d5e64d413";
        String SECRET = "34ae9c94e749dc6cc930794ea23cb588";
        String TYPE = "client_credential";
        String getUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + TYPE + "&appid=" + APPID + "&secret=" + SECRET;
        JSONObject jsonObject = HttpUtils.getHttp(getUrl);
        String accessToken = jsonObject.getString("access_token");
        if (accessToken == null || accessToken == "") {
            throw new NoDataFoundException("access_token获取失败");
        }
        return accessToken;
    }

    public String getAccessToken() throws Exception {
        ValueOperations ops = stringRedisTemplate.opsForValue();
        Object accessTokenItem = ops.get("accessTokenItem");// 获取Redis数据库中key为address1对应的value数据// 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的
        JSONObject accessTokenJson=JSON.parseObject(accessTokenItem.toString());
        Date now = new Date();
        if(accessTokenItem==null || now.compareTo(accessTokenJson.getDate("expirationtTime"))>0){
            JSONObject map = new JSONObject();
            String accessToken=getAccessTokenFromWX();
            map.put("accessToken",accessToken);
            map.put("expirationtTime",userService.getTwoHoursLater());
            String mapString= JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
            ops.set("accessTokenItem",mapString);// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）// 表明取的是key，value型的数据
            System.out.println("保存accessToken");
            return accessToken;
        }

        System.out.println("直接拿accessToken");

        return accessTokenJson.getString("accessToken");
    }

    JSONObject send(Schedule schedule,User user) throws Exception {
        String accessToken= getAccessToken();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        // 1.请求URL
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+accessToken;
        // 2.请求参数JSON格式
        JSONObject map = new JSONObject();
        JSONObject map0 = new JSONObject();
        JSONObject map1 = new JSONObject();
        JSONObject map2 = new JSONObject();
        JSONObject map3 = new JSONObject();
        JSONObject map4 = new JSONObject();
        JSONObject map5 = new JSONObject();
        map1.put("value",schedule.getRemindThing());
        map2.put("value",schedule.getThingAddress());
        map3.put("value",schedule.getRemark());
        map4.put("value",dateFormat.format(schedule.getStartTime()));
        map5.put("value",dateFormat.format(schedule.getEndTime()));
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
