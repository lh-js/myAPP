package com.example.myapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * schedule
 * @author 
 */
@ApiModel(value="com.example.myapp.model.Schedule")
@Data
public class Schedule implements Serializable {
    private Integer id;

    private String remindThing;

    private String thingAddress;

    private Date startTime;

    private Date endTime;

    private Date remindTime;

    private String remark;

    private String openId;

    private static final long serialVersionUID = 1L;
}