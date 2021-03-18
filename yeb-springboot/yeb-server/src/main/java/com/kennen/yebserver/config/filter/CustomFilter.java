package com.kennen.yebserver.config.filter;

import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.pojo.Role;
import com.kennen.yebserver.service.IMenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/11 18:36
 * @Description: 根据请求url分析请求所需的角色，用于权限控制
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {
    @Resource private IMenuService menuService;
    
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation)o).getRequestUrl();  // 6
        List<Menu> menus =  menuService.getMenusByRole();
        for(Menu menu: menus){
            //  判断请求url所匹配的菜单所需要的角色
            if(new AntPathMatcher().match(menu.getUrl(),requestUrl)){
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        //  未被匹配的url默认登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
