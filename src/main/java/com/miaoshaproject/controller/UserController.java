package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * @Author 86178
 * @create 2019/6/29 18:37
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders ="*")//DEFAULT_ALLOWED_HEADEERS 允许跨域传输所有header参数
// ，将用于使用token放如header域做session共享的跨域请求
public class UserController extends  BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value="/login",method ={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,
                                  @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel =userService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value="/register",method ={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="password")String password,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="name") String name) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号对应的otocode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }

        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setTelphone(telphone);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));
//        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));

        userService.register(userModel);

        return CommonReturnType.create(null);

    }


    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方式
        MessageDigest md5 =MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder =new BASE64Encoder();
        //加密字符串
        String newstr =base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    @RequestMapping(value="/getotp",method ={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt =random.nextInt(99999);
        randomInt +=10000;
        String otpCode =String.valueOf(randomInt);


        //将OTP验证码同对应用户的手机号关联,使用httpsession的方式绑定他的手机号与OTPCODE
     httpServletRequest.getSession().setAttribute(telphone,otpCode);



        //将OTP验证码通过短信通道发送给用户,省略
        System.out.println("telphone ="+telphone+" & optCode ="+otpCode);
        return CommonReturnType.create(null);
    }


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


    //可供UI使用的veiwobject
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO =new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }


}
