package com.kennen.yebserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kennen.yebserver.config.security.JwtTokenUtil;
import com.kennen.yebserver.mapper.RoleMapper;
import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.mapper.HrMapper;
import com.kennen.yebserver.pojo.Menu;
import com.kennen.yebserver.pojo.Role;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IHrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class HrServiceImpl extends ServiceImpl<HrMapper, Hr> implements IHrService {
    
    @Resource private UserDetailsService userDetailsService;
    @Resource private PasswordEncoder passwordEncoder;
    @Resource private JwtTokenUtil jwtTokenUtil;
    @Resource private HrMapper hrMapper;
    @Resource private RoleMapper roleMapper;
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        //  从session中获取生成的验证码
        String captcha = (String)request.getSession().getAttribute("captcha");
        if(!code.equalsIgnoreCase(captcha)){
            return RespBean.error("验证码输入错误！");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())){
            return RespBean.error("用户名或密码错误！");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用，请联系管理员！");
        }
        //  把登录对象放到security全局对象中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println("login:  "+SecurityContextHolder.getContext());
        
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功！",tokenMap);
    }

    @Override
    public Hr getHrByUserName(String username) {
        return hrMapper.selectOne(new QueryWrapper<Hr>()
                .eq("username",username)
                .eq("enabled",true)
        );
    }

    @Override
    public List<Role> getRoles(int userId) {
        return roleMapper.getRoles(userId);
    }

    @Override
    public List<Hr> getAllHrs(String keyword) {
        return hrMapper.getAllHrs(((Hr)(SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .getId(),keyword);
    }
}
