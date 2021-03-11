package com.kennen.yebserver.mapper;

import com.kennen.yebserver.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据id获取角色
     * @param userId
     * @return
     */
    List<Role> getRoles(int userId);
}
