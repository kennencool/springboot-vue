package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Role;
import com.kennen.yebserver.mapper.RoleMapper;
import com.kennen.yebserver.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
