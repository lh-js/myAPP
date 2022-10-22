package com.example.myapp.controller;

import com.example.myapp.model.Schedule;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.ScheduleVO;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.impl.ScheduleServiceImpl;
import com.example.myapp.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Resource
    private ScheduleServiceImpl scheduleService;

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/insert")
    public int insertSchedule(@RequestBody Schedule schedule) throws Exception {
        User user= userService.updateToken();
        return scheduleService.insertSchedule(schedule,user);
    }

    @PostMapping("selectByOpenId")
    public List<Schedule> selectByOpenId(){
        User user= userService.updateToken();
        return scheduleService.selectByOpenId(user);
    }

    @PostMapping("/updateSchedule")
    public int updateSchedule(@RequestBody Schedule schedule){
        User user= userService.updateToken();
        return scheduleService.updateSchedule(schedule,user);
    }

    @PostMapping("/selectById")
    public Schedule selectById(@RequestBody Schedule schedule){
        userService.updateToken();
        return scheduleService.getScheduleById(schedule.getId());
    }

//    @PostMapping("/send")
//    public String send() throws Exception {
//        return scheduleService.send();
//    }
}
