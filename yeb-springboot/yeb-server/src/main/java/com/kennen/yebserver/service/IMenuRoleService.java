package com.kennen.yebserver.service;

import com.kennen.yebserver.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kennen.yebserver.pojo.resp.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色的菜单列表
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
