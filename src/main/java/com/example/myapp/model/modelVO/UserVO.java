package com.example.myapp.model.modelVO;

import com.example.myapp.model.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserVO extends User {

    private String code;

    private String token;

}
