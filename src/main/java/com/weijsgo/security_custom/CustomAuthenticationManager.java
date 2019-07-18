package com.weijsgo.security_custom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final AuthenticationProvider authenticationProvider;

    public CustomAuthenticationManager(AuthenticationProvider authenticationProvider){
        this.authenticationProvider = authenticationProvider;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication reulst = authenticationProvider.authenticate(authentication);
        if (Objects.nonNull(reulst)){
            return  reulst;
        }
        throw new ProviderNotFoundException("not fund");

    }
}
