package com.miaoshaproject.service.impl;


import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorIml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务层，接口的方法
 * 用于Controller层的调用
 * @Author 86178
 * @create 2019/6/29 18:42
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorIml validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdomapper获取到对应用户dataobject
        UserDO userDO =userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //通过用户id获取对应用户加密密码信息
        UserPasswordDO userPasswordDO =userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFormDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"caocaocoa");
        }
//        if(StringUtils.isEmpty(userModel.getName())
//                || userModel.getGender() ==null
//                || userModel.getAge()== null
//                ||StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??111sadas");
//        }
        ValidationResult result =validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //实现model->dataobject
        UserDO userDO =convertFormModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }

        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO =convertPasswordFormModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户手机手机获取用户信息
        UserDO userDO =userDOMapper.selectByTelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO =userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFormDataObject(userDO,userPasswordDO);
        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if( !StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }else{
            return userModel;
        }

    }

    private UserPasswordDO convertPasswordFormModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return  userPasswordDO;
    }
    private UserDO convertFormModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }


    /*从数据库直接映射层转换为用户核心类Model*/
    private UserModel convertFormDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel =new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
      return userModel;
    }
}
