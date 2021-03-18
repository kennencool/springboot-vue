package com.kennen.yebserver.config.security;

import com.kennen.yebserver.config.entrypoint.RestAuthorizationEntryPoint;
import com.kennen.yebserver.config.filter.CustomFilter;
import com.kennen.yebserver.config.filter.CustomUrlDecisionManager;
import com.kennen.yebserver.config.filter.JwtAuthenticationTokenFilter;
import com.kennen.yebserver.config.handler.RestfulAccessDeniedHandler;
import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.service.IHrService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.annotation.Resource;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 11:37
 * @Description: Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource private IHrService hrService;
    @Resource private RestAuthorizationEntryPoint entryPoint;
    @Resource private RestfulAccessDeniedHandler accessDeniedHandler;
    @Resource private CustomFilter customFilter;
    @Resource private CustomUrlDecisionManager urlDecisionManager;

    /**
     * 配置密码验证以及UserDetailsService
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()) // 2
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 放行一些路径，不走拦截路径
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( // 4
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/login",
                "/logout",
                "/captcha"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  使用JWT不需要csrf
        http.csrf().disable()// 3
                //  基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //  除了上面的路径，其他都需要验证
                .anyRequest()
                .authenticated()
                //  动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(urlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
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
        return username -> {// 1
            Hr hr = hrService.getHrByUserName(username); 
            if(hr!=null){
                hr.setRoles(hrService.getRoles(hr.getId()));
                return hr;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }
    
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}