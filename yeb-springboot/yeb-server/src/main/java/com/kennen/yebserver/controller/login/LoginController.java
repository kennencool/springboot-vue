package com.kennen.yebserver.controller.login;

import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.pojo.param.HrLoginParam;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IHrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 11:02
 * @Description:
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {
    
    @Autowired private IHrService hrService;
    
    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody HrLoginParam hrLoginParam, HttpServletRequest request){
        return hrService.login(hrLoginParam.getUsername(), hrLoginParam.getPassword(),
                hrLoginParam.getCode(), request);
    }
    
    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }
}
