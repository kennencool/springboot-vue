package com.kennen.yebserver.controller;


import com.jhlabs.image.ImageMath;
import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.service.IHrService;
import com.kennen.yebserver.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/system/config")
public class MenuController {
    @Resource private IMenuService menuService;
    
    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusById(){
        return menuService.getMenusById();
    }
}
