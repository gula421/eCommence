package com.example.demo;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static List<User> createUsers(int numUser){
        List<User> list = new ArrayList<>();
        for (int i=1; i<numUser; i++){
            User user = new User();
            user.setUsername("username"+i);
            user.setPassword("password"+i);
            list.add(user);
        }
        return list;
    }

    public static CreateUserRequest createOneUserRequest(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setPasswordConfirm("password");
        return request;
    }

    public static User createOneUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("oneUserName");
        user.setPassword("oneUserPassword");
        return user;
    }
}
