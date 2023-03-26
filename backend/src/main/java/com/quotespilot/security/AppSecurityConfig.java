package com.quotespilot.security;

import com.quotespilot.security.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Value("${front.end.app.origin}")
    private String frontEndAppOrigin;

    /**
     *  CORS configuration. 
     *  It will return ACCESS_CONTROL_ALLOW_ORIGIN from spring's response
     *  SO that we can call application from our react-js front end running on different origin otherwise we will 
     *  get error saying -
     *  Access to XMLHttpRequest at 'http://localhost:8080/quote/search?query=joy' from origin 'http://localhost:3000' 
     *  has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
     */
   
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").
                        allowedMethods("*").
                        allowedHeaders("*").
                        allowedOrigins(frontEndAppOrigin);
            }
        };
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http POST messages will return 403 if csrf is not disabled for POST Operations
        //so disabling CSRF
        http.csrf().disable()
                //Enabling authorization
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                //.antMatchers("/h2-console/**").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/logout/**").permitAll()
                //react-js frontend will give CORS error if we don't add this (My precious 3 hours)
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                //.antMatchers("/quote/random/**").permitAll()
                //.antMatchers("/quote/search/**").permitAll()
                .antMatchers("/quote/delete-quote/**").access("hasAuthority('ROLE_ADMIN')")
                .anyRequest().authenticated()
                
                /*Use below in case of username/password based authentication*/
                //.and().logout().invalidateHttpSession(true).permitAll()
                //.and().httpBasic();
                
                /*Use Below in case of jwt based authentication */
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
    
    @Bean
    public AuthenticationManager defaultAuthenticationMenager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
