package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * @Author 86178
 * @create 2019/6/29 18:40
 */
public interface UserService {
    //通过用户ID获取用户对象
    UserModel getUserById(Integer id);
    //用户注册
    void register(UserModel userModel) throws BusinessException;
    //登录校验
    /*
    * telphone：用户注册手机
    * password：用户加密后的密码
    * */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
