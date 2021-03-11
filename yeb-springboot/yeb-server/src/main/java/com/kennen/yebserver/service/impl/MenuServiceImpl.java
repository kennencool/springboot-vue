package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.mapper.MenuMapper;
import com.kennen.yebserver.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kennen.yebserver.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Resource private MenuMapper menuMapper;
    @Resource private RedisUtil redisUtil;
    
    @Override
    public List<Menu> getMenusById() {
        int userId = ((Hr)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
        //  从redis中获取菜单数据
        List<Menu> menus = (List<Menu>) redisUtil.get("menu_" + userId);
        if(CollectionUtils.isEmpty(menus)){
            //  从数据库拉取，然后再存储
            menus = menuMapper.getMenusById(userId);
            redisUtil.set("menu_" + userId, menus);
        }
        return menus;
    }

    @Override
    public List<Menu> getMenusByRole() {
        return menuMapper.getMenusByRole();
    }
}
