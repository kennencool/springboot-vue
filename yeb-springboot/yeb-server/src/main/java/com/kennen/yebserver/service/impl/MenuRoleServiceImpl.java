package com.kennen.yebserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kennen.yebserver.pojo.MenuRole;
import com.kennen.yebserver.mapper.MenuRoleMapper;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Resource private MenuRoleMapper menuRoleMapper;
    
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //  删除角色所有菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //  给角色添加菜单
        if(null == mids || 0 == mids.length){
            return RespBean.success("更新成功！");
        }
        //  批量更新
        Integer result = menuRoleMapper.insertRecord(rid,mids);
        if(result == mids.length){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}
