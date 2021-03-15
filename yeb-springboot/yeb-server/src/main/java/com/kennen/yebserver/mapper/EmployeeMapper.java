package com.kennen.yebserver.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kennen.yebserver.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    
    IPage<Employee> getEmployeeByPage(Page<Employee> page,
                                      @Param("employee") Employee employee, 
                                      @Param("beginDateScope") Date[] beginDateScope);
}
