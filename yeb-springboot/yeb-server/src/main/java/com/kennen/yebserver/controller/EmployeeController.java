package com.kennen.yebserver.controller;


import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.pojo.resp.RespPageBean;
import com.kennen.yebserver.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {
    @Resource private IEmployeeService employeeService;
    
    @ApiOperation(value = "分页查询所有员工")
    @GetMapping("/")
    public RespPageBean getEmployee(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size,
            Employee employee,
            Date[] beginDateScope
    ){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);
    }
}
