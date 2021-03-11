package com.kennen.yebserver.service;

import com.kennen.yebserver.pojo.Hr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.pojo.Role;
import com.kennen.yebserver.pojo.resp.RespBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface IHrService extends IService<Hr> {

    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 查询当前登录用户信息
     * @param username
     * @return
     */
    Hr getHrByUserName(String username);

    /**
     * 根据id获取角色
     * @param userId
     * @return
     */
    List<Role> getRoles(int userId);
}
