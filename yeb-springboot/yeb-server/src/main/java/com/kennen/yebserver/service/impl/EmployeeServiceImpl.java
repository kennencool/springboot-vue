package com.kennen.yebserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.mapper.EmployeeMapper;
import com.kennen.yebserver.pojo.resp.RespPageBean;
import com.kennen.yebserver.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource private EmployeeMapper employeeMapper;
    
    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, Date[] beginDateScope) {
        //  开启分页
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeByPage(page,employee,beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(),employeeIPage.getRecords());
        return respPageBean;
    }
}
