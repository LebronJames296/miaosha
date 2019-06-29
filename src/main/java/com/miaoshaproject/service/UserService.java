package com.miaoshaproject.service;

import com.miaoshaproject.service.model.UserModel;

/**
 * @Author 86178
 * @create 2019/6/29 18:40
 */
public interface UserService {
    //通过用户ID获取用户对象
    UserModel getUserById(Integer id);
}
