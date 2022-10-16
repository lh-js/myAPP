package com.example.myapp.service;

import com.example.myapp.model.Schedule;
import com.example.myapp.model.modelVO.ScheduleVO;

import java.util.List;

public interface ScheduleService {

    int insertSchedule(Schedule record);

    List<Schedule> selectByOpenId();

    Schedule getScheduleById(Integer id);

    int updateSchedule(Schedule schedule);
}
