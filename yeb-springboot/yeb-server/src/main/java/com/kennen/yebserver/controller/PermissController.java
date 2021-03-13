package com.kennen.yebserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.pojo.MenuRole;
import com.kennen.yebserver.pojo.Role;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IMenuRoleService;
import com.kennen.yebserver.service.IMenuService;
import com.kennen.yebserver.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/12 16:46
 * @Description: 权限组操作接口
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Resource private IRoleService roleService;
    @Resource private IMenuService menuService;
    @Resource private IMenuRoleService menuRoleService;
    
    @ApiOperation(value = "获取所有角色")
    @GetMapping("/role")
    public List<Role> getAllRoles(){
        return roleService.list();
    }
    
    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(
            @RequestBody Role role
    ){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_" + role.getName());
        }
        if(roleService.save(role)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(
            @PathVariable Integer rid
    ){
        //  注意，首先要将该角色在 menu_role中的数据删除，防止外键干扰
        if(menuRoleService.remove(new QueryWrapper<MenuRole>().eq("rid",rid))){
            if(roleService.removeById(rid)){
                return RespBean.success("删除成功！");
            }
        }
        return RespBean.error("删除失败！");
    }
    
    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menu")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }
    
    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/menu/{rid}")
    public List<Integer> getMidByRid(
            @PathVariable Integer rid
    ){
        return menuRoleService.list(new QueryWrapper<MenuRole>()
                .eq("rid",rid))
                .stream()
                .map(MenuRole::getMid) 
                .collect(Collectors.toList());
    }
    
    @ApiOperation(value = "更新角色的菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids){
        return menuRoleService.updateMenuRole(rid,mids);
    }
}
