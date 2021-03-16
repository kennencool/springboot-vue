package com.kennen.yebserver.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.kennen.yebserver.mapper.EmployeeecMapper;
import com.kennen.yebserver.pojo.*;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.pojo.resp.RespPageBean;
import com.kennen.yebserver.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
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
@RequestMapping("/employee/basic")
public class EmployeeController {
    @Resource private IEmployeeService employeeService;
    @Resource private IPoliticsstatusService politicsstatusService;
    @Resource private IJoblevelService joblevelService;
    @Resource private IPositionService positionService;
    @Resource private INationService nationService;
    @Resource private IDepartmentService departmentService;
    
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
    
    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsStatus(){
        return politicsstatusService.list();
    }
    
    @ApiOperation(value = "获取所有职称")
    @GetMapping("/jobLevel")
    public List<Joblevel> getAllJobLevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/position")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/department")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean getMaxWorkID(){
        return employeeService.maxWorkID();
    }
    
    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }
    
    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee){
        if(employeeService.updateById(employee)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id){
        if(employeeService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
    
    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> employees = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook sheets = ExcelExportUtil.exportExcel(params, Employee.class, employees);
        ServletOutputStream out = null;
        try {
            //  流形式传输
            response.setHeader("content-type","application/octet-stream");
            //  防止中文乱码
            response.setHeader("content-disposition",
                    "attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
            out = response.getOutputStream();
            sheets.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //  作用是删除标题行
        params.setTitleRows(1);
        List<Employee> employeeLists = null;
        List<Nation> nationList = nationService.list();
        List<Position> positionList = positionService.list();
        List<Politicsstatus> politicsstatusList = politicsstatusService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Department> departmentList = departmentService.list();
        
        try {
            employeeLists = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            employeeLists.forEach(employee -> {
                employee.setNationId(nationList.get(nationList.indexOf(
                        new Nation(employee.getNation().getName())
                )).getId());
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(
                        new Department(employee.getDepartment().getName())
                )).getId());
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(
                        new Joblevel(employee.getJoblevel().getName())
                )).getId());
                employee.setPosId(positionList.get(positionList.indexOf(
                        new Position(employee.getPosition().getName())
                )).getId());
                employee.setPoliticId(politicsstatusList.get(politicsstatusList.indexOf(
                        new Politicsstatus(employee.getPoliticsstatus().getName())
                )).getId());
            });
            if(employeeService.saveBatch(employeeLists)){
                return RespBean.success("导入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败！");
    }
}
