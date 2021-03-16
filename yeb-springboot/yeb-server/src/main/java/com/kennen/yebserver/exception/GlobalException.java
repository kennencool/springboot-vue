package com.kennen.yebserver.exception;

import com.kennen.yebserver.pojo.resp.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/12 16:20
 * @Description: 全局异常处理类
 */
@RestControllerAdvice
public class GlobalException {

    /**
     * 捕获全局出现的sql异常
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据还存在关联数据，操作失败！");
        }else if(e instanceof SQLFeatureNotSupportedException){
            return RespBean.error("数据映射方面出了问题，如果是datetime转string可能是com.alibaba依赖版本太低导致！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }
}
