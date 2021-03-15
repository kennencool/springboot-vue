package com.kennen.yebserver.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/15 10:08
 * @Description: 分页公共返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {
    //  总条目
    private Long total;
    //  数据
    private List<?> data;
}
