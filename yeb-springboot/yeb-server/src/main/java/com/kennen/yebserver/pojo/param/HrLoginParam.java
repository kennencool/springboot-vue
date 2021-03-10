package com.kennen.yebserver.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 10:59
 * @Description: Hr登录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "登录信息实体", description = "")
public class HrLoginParam {
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;
}
