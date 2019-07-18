package com.weijsgo.security_custom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationFailureHandler authenticationFailureHandler,
                                      AuthenticationSuccessHandler authenticationSuccessHandler){
        super(new AntPathRequestMatcher("/login","POST"));
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("认证方法不支持："+request.getMethod());
        }

        /*
        // 添加验证码校验功能
        String captcha = request.getParameter("captcha");
        if (!checkCaptcha(captcha)) {
            throw new AuthenticationException("Invalid captcha!");
        }
        */

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        username = Objects.isNull(username)?"":username;
        password= Objects.isNull(password)?"":password;

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
