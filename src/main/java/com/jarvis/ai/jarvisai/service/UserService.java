package com.jarvis.ai.jarvisai.service;

import com.jarvis.ai.jarvisai.dao.UserDao;
import com.jarvis.ai.jarvisai.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    /**
     * 用户注册
     * @param user 要注册的user
     * @return 此次操作数据库插入的user个数
     */
    public int userRegister(User user){
        return userDao.addUser(user);
    }

    /**
     * 通过名字查找用户
     * @param name 名字
     * @return 用户列表
     */
    public List<User> getUsersByName(String name){
        return userDao.getUsersByName(name);
    }

    /**
     * 通过电话号码更新用户名
     * @param phone
     * @param newName 新的用户名
     * @return 此次操作数据库更新的条数
     */
    public int updateUserNameByPhone(String phone,String newName){
        return userDao.updateUserNameByPhone(phone,newName);
    }

    /**
     * 通过电话号码删除用户信息
     * @param phone
     * @return
     */
    public int deleteUserByPhone(String phone){
        return userDao.deleteUserByPhone(phone);
    }




}
