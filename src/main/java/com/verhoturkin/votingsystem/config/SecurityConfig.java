package com.verhoturkin.votingsystem.config;


import com.verhoturkin.votingsystem.web.RestAuthenticationEntryPoint;
import com.verhoturkin.votingsystem.web.RestResponseExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService detailsService;

    @Autowired
    public SecurityConfig(UserDetailsService detailsService) {
        this.detailsService = detailsService;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/register").anonymous()
                .antMatchers("/**/profile", "/**/menu", "/**/votes","/**/current").hasRole("USER")
                .antMatchers("/**/users/**", "/**/restaurants/**", "/**/byDate", "/**/byRestaurant", "/api-docs").hasRole("ADMIN")
                .and().exceptionHandling().accessDeniedHandler(new RestResponseExceptionHandler())
                .and().httpBasic().authenticationEntryPoint(restAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
