package com.kennen.yebserver.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/15 10:10
 * @Description: 日期转换
 */
@Component
public class DateConverter implements Converter<String, Date> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public Date convert(String s) {
        try{
            return sdf.parse(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
