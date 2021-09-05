package com.jarvis.ai.jarvisai.dao;

import com.jarvis.ai.jarvisai.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface  UserDao {

    @Insert("insert into user_tb (user_name,phone,icon_url,role) values(#{userName},#{phone},#{iconUrl},#{role})")
    int addUser(User user);


    @Select("select * from user_tb where user_name=#{userName}")
    List<User> getUsersByName(@Param(value = "userName")String name);

    @Update("update user_tb set user_name=#{userName} where phone=#{phone}")
    int updateUserNameByPhone(@Param(value = "phone")String phone,@Param(value = "userName")String userName);

    @Delete("delete from user_tb where phone=#{phone}")
    int deleteUserByPhone(@Param(value = "phone")String phon);



}
