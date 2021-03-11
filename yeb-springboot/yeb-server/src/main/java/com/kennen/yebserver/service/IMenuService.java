package com.kennen.yebserver.service;

import com.kennen.yebserver.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kennen.yebserver.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据全局对象中存储的用户id查询菜单
     * @return
     */
    List<Menu> getMenusById();

    /**
     * 根据角色获取菜单
     * @return
     */
    List<Menu> getMenusByRole();
}
