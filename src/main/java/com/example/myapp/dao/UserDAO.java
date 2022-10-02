package com.example.myapp.dao;

import com.example.myapp.model.User;

public interface UserDAO {
    int deleteByPrimaryKey(String openId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String openId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}