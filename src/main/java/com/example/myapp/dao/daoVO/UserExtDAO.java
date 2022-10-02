package com.example.myapp.dao.daoVO;

import com.example.myapp.dao.UserDAO;
import com.example.myapp.model.User;
import com.example.myapp.util.QueryRequest;
import com.example.myapp.util.QueryResult;

import java.util.List;

public interface UserExtDAO extends UserDAO {
    List<User> selectAll(User user);

}
