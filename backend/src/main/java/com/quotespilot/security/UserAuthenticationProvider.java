package com.quotespilot.security;

import com.quotespilot.entity.User;
import com.quotespilot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("*****************inside authenticate");
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.checkCredentials(username, password);
        if(user !=null ){
            //Assigning authorities to a user is must otherwise spring-security will not work
            return new UsernamePasswordAuthenticationToken(username,password,getGrantedAuthorities(user.getRole()));
        }
        throw new BadCredentialsException("Invalid Credentials!!");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        return grantedAuthorities;
    }
}
