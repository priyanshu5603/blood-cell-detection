package com.scm.majProj.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.majProj.services.implementation.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private OAuthAuthenticationSuccessHandler handler; // Ensure this is not null
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
        
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard"); // Redirect to dashboard on successful login
            formLogin.failureUrl("/login?error=true"); // Redirect to login page with error on failure

            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password"); // Set the parameter names for username and password
            
        });//with help of this access denied wont come
        // rather the please login page will come
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(
            logoutForm -> {
                logoutForm.logoutUrl("/logout"); // URL to trigger logout
                logoutForm.logoutSuccessUrl("/login?logout=true"); // Redirect to login page after logout
        });


        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login"); // Custom login page for OAuth2
            oauth.successHandler(handler); // Handle successful login
        }); // Enable OAuth2 login with default settings


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



