package com.scm.majProj.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.majProj.services.implementation.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailsService; // Ensure this is not null

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home","/about","/register").permitAll() 
            //          ; // Require authentication for other requests
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll(); // Allow all other requests without authentication 
        });
        
        httpSecurity.formLogin(Customizer.withDefaults());//with help of this access denied wont come
        // rather the please login page will come

        return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService); // Set userDetailsService correctly
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



