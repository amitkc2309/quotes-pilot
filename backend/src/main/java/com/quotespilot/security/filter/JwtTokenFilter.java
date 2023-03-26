package com.quotespilot.security.filter;

import com.quotespilot.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    
    // Spring Security will call method during filter chain execution
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("******************inside doFilterInternal of JwtTokenFilter");
        // fetch Authorization header from request
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")){
            // if Authorization header does not exist, then skip this filter and continue to execute next filter class
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final String token = authorizationHeader.split(" ")[1].trim();
        if (!jwtUtil.validate(token)) {
            // if token is not valid, then skip this filter
            // and continue to execute next filter class.
            // This means authentication is not successful since token is invalid.
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if(SecurityContextHolder.getContext()!=null){
            if(SecurityContextHolder.getContext().getAuthentication()!=null)
                logger.info("***********username from SecurityContextHolder="+SecurityContextHolder.getContext().getAuthentication().getName());
        }
        
        // Authorization header exists, token is valid. So, we can authenticate.
        String username = jwtUtil.getUsername(token);
        String role= jwtUtil.getRole(token);
        logger.info("***********username from token jwtFilter="+username);
        logger.info("***********Role from token jwtFilter="+role);
        UsernamePasswordAuthenticationToken upassToken = 
                new UsernamePasswordAuthenticationToken(username, null, getGrantedAuthorities(role));
        upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        // finally, give the authentication token to Spring Security Context
        SecurityContextHolder.getContext().setAuthentication(upassToken);

        // end of the method, so go for next filter class
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        return grantedAuthorities;
    }
}
