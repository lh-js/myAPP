package com.example.myapp.model.modelVO;

import com.example.myapp.model.Schedule;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ScheduleVO extends Schedule {

    private String token;
}
