package com.example.myapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user
 * @author 
 */
@ApiModel(value="com.example.myapp.model.User")
@Data
public class User implements Serializable {
    private String openId;

    private String username;

    private String avatar;

    private Date expirationttime;

    private static final long serialVersionUID = 1L;
}