package com.miaoshaproject;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//@EnableAutoConfiguration//依赖自动加载

@SpringBootApplication(scanBasePackages = {"com.miaoshaproject"})//主启动类，给spring boot 托管
@RestController
@MapperScan("com.miaoshaproject.dao")
public class App
{
    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
     public String home(){
      UserDO userDO =  userDOMapper.selectByPrimaryKey(1);
    if(userDO == null)
    {
       return "用户对象不存在";
    }else{
        return userDO.getName();
    }

     }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);
    }
}
