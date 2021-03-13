package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Department;
import com.kennen.yebserver.mapper.DepartmentMapper;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Resource DepartmentMapper departmentMapper;
    
    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public RespBean addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
        if(1 == department.getResult()){
            return RespBean.success("添加成功！",department);
        }
        return RespBean.error("添加失败！");
    }

    @Override
    public RespBean deleteDepartment(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
        switch (department.getResult()){
            case 1:
                return RespBean.success("删除成功！");
            case -2:
                return RespBean.error("该部门下还有子部门，删除失败！");
            case -1:
                return RespBean.error("该部门下还有员工，删除失败！");
            default:
                break;
        }
        return RespBean.error("删除失败！");
    }
}
