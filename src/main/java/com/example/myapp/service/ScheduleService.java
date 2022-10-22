package com.example.myapp.service;

import com.example.myapp.model.Schedule;
import com.example.myapp.model.User;
import com.example.myapp.model.modelVO.ScheduleVO;

import java.util.List;

public interface ScheduleService {

    int insertSchedule(Schedule record, User user) throws Exception;

    List<Schedule> selectByOpenId(User user);

    Schedule getScheduleById(Integer id);

    int updateSchedule(Schedule schedule,User user);
}
