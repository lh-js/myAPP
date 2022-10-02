package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

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

    private static final long serialVersionUID = 1L;
}