package com.kennen.yebserver.controller;


import com.kennen.yebserver.pojo.Department;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Resource private IDepartmentService departmentService;
    
    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }
    
    @ApiOperation(value = "添加部门")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }
    
    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{id}")
    public RespBean deleteDepartment(@PathVariable Integer id){
        return departmentService.deleteDepartment(id);
    }
}
