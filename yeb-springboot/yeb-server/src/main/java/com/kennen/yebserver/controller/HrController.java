package com.kennen.yebserver.controller;


import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.service.IHrService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/hr")
public class HrController {
    @Autowired private IHrService hrService;
    
    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/info")
    public Hr getHrInfo(Principal principal){
        if(null == principal){
            return null;
        }
        String username = principal.getName();
        Hr hr = hrService.getHrByUserName(username);
        hr.setPassword(null);
        hr.setRoles(hrService.getRoles(hr.getId()));
        return hr;
    }
    
    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Hr> getAllHrs(String keyword){
        return hrService.getAllHrs(keyword);
    }
    
}
