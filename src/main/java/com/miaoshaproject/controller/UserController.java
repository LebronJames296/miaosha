package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



/**
 * @Author 86178
 * @create 2019/6/29 18:37
 */
@Controller("user")
@RequestMapping("/user")
public class UserController extends  BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

      //若获取的对应用户信息不存在
        if(userModel == null){
//        userModel.setEncrptPassword("112");
            throw  new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
       //将核心领域模型用户对象(Model)转化为可供UI使用的veiwobject
        UserVO userVO = convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO =new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }


}
