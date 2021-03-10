package com.kennen.yebserver.config.security;

import com.kennen.yebserver.config.entrypoint.RestAuthorizationEntryPoint;
import com.kennen.yebserver.config.filter.JwtAuthenticationTokenFilter;
import com.kennen.yebserver.config.handler.RestfulAccessDeniedHandler;
import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.service.IHrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 11:37
 * @Description: Security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource private IHrService hrService;
    @Resource private RestAuthorizationEntryPoint entryPoint;
    @Resource private RestfulAccessDeniedHandler accessDeniedHandler;

    /**
     * 配置密码验证以及UserDetailsService
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  使用JWT不需要csrf
        http.csrf().disable()
                //  基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //  允许登录
                .authorizeRequests()
                .antMatchers("/login","logout")
                .permitAll()
                //  除了上面的路径，其他都需要验证
                .anyRequest()
                .authenticated()
                .and()
                //  禁用缓存
                .headers()
                .cacheControl();
        //  添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //  添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint);
    }

    /**
     * 重写密码机制
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 重写 UserDetailsService方法
     * @return
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Hr hr = hrService.getHrByUserName(username);
            return hr;
        };
    }
    
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}