package com.kennen.yebserver.service;

import com.kennen.yebserver.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kennen.yebserver.pojo.resp.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartments();

    /**
     * 添加部门
     * @param department
     * @return
     */
    RespBean addDepartment(Department department);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RespBean deleteDepartment(Integer id);
}
