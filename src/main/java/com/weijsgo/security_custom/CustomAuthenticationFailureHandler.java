package com.weijsgo.security_custom;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public CustomAuthenticationFailureHandler(){}

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        //通过检查异常类型实现页面跳转控制
        if (e instanceof UsernameNotFoundException) {
            httpServletResponse.sendRedirect("/login/page?inexistent");
        } else if (e instanceof DisabledException) {
            httpServletResponse.sendRedirect("/login/page?disabled");
        } else if (e instanceof AuthenticationException) {
            httpServletResponse.sendRedirect("/login/page?expired");
        } else if (e instanceof LockedException) {
            httpServletResponse.sendRedirect("/login/page?locked");
        } else {
            httpServletResponse.sendRedirect("/login/page?error");
        }
    }
}
