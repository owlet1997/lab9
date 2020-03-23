package com.ncedu.user_manager.config;

import com.ncedu.user_manager.service.impl.auth.JWTAuthenticationEntryPoint;
import com.ncedu.user_manager.filter.JWTRequestFilter;
import com.ncedu.user_manager.service.impl.auth.JWTAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private String[] allowedURIs = new String[] {
            "/",
            "/webjars/**",
            "/csrf",
            "/error",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html*",
            "/api/user-manager/v1/login"
    };

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(allowedURIs).permitAll()
                .anyRequest().authenticated()
        .and().exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
                .authenticationProvider(jwtAuthenticationProvider);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
