package com.weijsgo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserServiceImp userServiceImp;



    /**
     *  配置user-detail服务
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

        //1.基于内存验证
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder.encode("admin")).roles("ADMIN").and()
                .withUser("test").password(passwordEncoder.encode("test")).roles("TEST");

        //2.基于数据库验证
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username,password,enabled from user where username = ?")
//                .authoritiesByUsernameQuery("select username,rolename from role where username=?")
//                .passwordEncoder(passwordEncoder);

        //3.基于LDAP验证
        auth.ldapAuthentication().userSearchFilter("{uid=0}").groupSearchFilter("member={0}");

        //4.自定义用户服务
        auth.userDetailsService(userServiceImp).passwordEncoder(passwordEncoder);

    }

    /**
     * 配置Security的Filter链
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 配置如何通过拦截器保护请求
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .authorizeRequests()
                // 所有请求登录后
                .anyRequest().authenticated()
                //basic 验证
                .and().httpBasic()
                // 表单验证
                .and().formLogin().loginPage("/").defaultSuccessUrl("/index")
                .failureForwardUrl("/login/error").permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();



        http.addFilter(null);
    }
}
