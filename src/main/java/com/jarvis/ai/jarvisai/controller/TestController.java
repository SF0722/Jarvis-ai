package com.jarvis.ai.jarvisai.controller;

import com.jarvis.ai.jarvisai.model.Dialogue;
import com.jarvis.ai.jarvisai.model.User;
import com.jarvis.ai.jarvisai.service.SayHelloService;
import com.jarvis.ai.jarvisai.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    SayHelloService sayHelloService;

    @Autowired
    UserService userService;



    @RequestMapping(value = "/say_hello_to_somebody")
    public Dialogue sayhello(@RequestParam(value = "userId")Long userId, @RequestParam(value = "content") String content){
        String str = sayHelloService.sayhello(userId,content);
       // String str ="hello";
        return new Dialogue(0,str,new Date().toString());

    }


    @RequestMapping(value = "/add_user")
    public int addUser2table(@RequestParam( value = "userName")String userName,@RequestParam(value = "phone")String phone,@RequestParam(value = "iconUrl")String iconUrl,@RequestParam(value = "role")String role){
        User user=new User();
        user.setUserName(userName);
        user.setIconUrl(iconUrl);
        user.setPhone(phone);
        user.setRole(role);
        return userService.userRegister(user);
    }


    @RequestMapping(value = "/search_users_by_name")
    public List<User> getUsersByName(@RequestParam(value = "userName")String userName){
        List<User> users =new ArrayList<>();
        users.addAll(userService.getUsersByName(userName));
        return users;
    }


    @RequestMapping(value = "/update_username_by_phone")
    public int updateUserNameByPhone(@RequestParam(value = "newName")String newName, @Param(value = "phone")String phone){
        return userService.updateUserNameByPhone(phone,newName);
    }

    @RequestMapping(value = "/delete_user_by_phone")
    public int deleteUserByPhone(@Param(value = "phone")String phone){
        return userService.deleteUserByPhone(phone);
    }



}
