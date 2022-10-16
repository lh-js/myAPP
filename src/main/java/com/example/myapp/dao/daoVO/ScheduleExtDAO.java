package com.example.myapp.dao.daoVO;

import com.example.myapp.dao.ScheduleDAO;
import com.example.myapp.model.Schedule;

import java.util.List;

public interface ScheduleExtDAO extends ScheduleDAO {
    List<Schedule> selectByOpenId(String openId);
}
