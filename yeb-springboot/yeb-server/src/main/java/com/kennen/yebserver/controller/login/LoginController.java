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
    public RespBean login(HrLoginParam hrLoginParam, HttpServletRequest request){
        return hrService.login(hrLoginParam.getUsername(), hrLoginParam.getPassword(), request);
    }

    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/hr/info")
    public Hr getHrInfo(Principal principal){
        if(null == principal){
            return null;
        }
        String username = principal.getName();
        Hr hr = hrService.getHrByUserName(username);
        hr.setPassword(null);
        return hr;
    }
    
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }
}
