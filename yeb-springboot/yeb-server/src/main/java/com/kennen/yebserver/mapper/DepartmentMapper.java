package com.kennen.yebserver.mapper;

import com.kennen.yebserver.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(Integer parentId);

    void addDepartment(Department department);

    void deleteDepartment(Department department);
}
