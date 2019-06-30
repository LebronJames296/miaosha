package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author 86178
 * @create 2019/6/30 20:41
 */

@Component
public class ValidatorIml implements InitializingBean {

   private Validator validator;//javax接口实现的validator工具

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        //返回bean中参数违背validate注解规则的值
     Set<ConstraintViolation<Object>> constraintViolationSet =validator.validate(bean);
     if(constraintViolationSet.size()>0){
         //有错误
         result.setHasErrors(true);
         //遍历错误
         constraintViolationSet.forEach(constraintViolation->{
            String errMsg =constraintViolation.getMessage();
            String propertyName =constraintViolation.getPropertyPath().toString();
            result.getErrorMsgMap().put(propertyName,errMsg);
         });
     }
    return result;
    }

    //此方法在SpringBean初始化完成后进行回调
    @Override
    public void afterPropertiesSet() throws Exception {
    //将hibernate validator 通过工厂的初始化方式使其初始化
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
