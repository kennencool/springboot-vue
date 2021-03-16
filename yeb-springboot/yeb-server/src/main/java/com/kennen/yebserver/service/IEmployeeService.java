package com.kennen.yebserver.service;

import com.kennen.yebserver.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.pojo.resp.RespPageBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页获取员工信息
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, Date[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkID();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmp(Employee employee);

    /**
     * 查询所有员工
     * @param id
     */
    List<Employee> getEmployee(Integer id);
}
