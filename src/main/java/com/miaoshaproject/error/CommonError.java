package com.miaoshaproject.error;

/**
 * 定义错误类型
 * @Author 86178
 * @create 2019/6/29 20:21
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
