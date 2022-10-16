package com.example.myapp.controller;

import com.example.myapp.model.Schedule;
import com.example.myapp.model.modelVO.ScheduleVO;
import com.example.myapp.model.modelVO.UserVO;
import com.example.myapp.service.impl.ScheduleServiceImpl;
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

    @PostMapping("/insert")
    public int insertSchedule(@RequestBody Schedule schedule) {
        return scheduleService.insertSchedule(schedule);
    }

    @PostMapping("selectByOpenId")
    public List<Schedule> selectByOpenId(){
        return scheduleService.selectByOpenId();
    }

    @PostMapping("/updateSchedule")
    public int updateSchedule(@RequestBody Schedule schedule){
        return scheduleService.updateSchedule(schedule);
    }

    @PostMapping("/selectById")
    public Schedule selectById(@RequestBody Schedule schedule){
        return scheduleService.getScheduleById(schedule.getId());
    }

//    @PostMapping("/send")
//    public String send() throws Exception {
//        return scheduleService.send();
//    }
}
