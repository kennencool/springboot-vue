package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.mapper.EmployeeMapper;
import com.kennen.yebserver.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjy
 * @since 2021-03-10
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
