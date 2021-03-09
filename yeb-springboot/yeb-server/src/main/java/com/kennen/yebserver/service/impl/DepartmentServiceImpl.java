package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Department;
import com.kennen.yebserver.mapper.DepartmentMapper;
import com.kennen.yebserver.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
