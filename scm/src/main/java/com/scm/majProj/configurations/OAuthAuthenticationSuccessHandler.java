package com.scm.majProj.configurations;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.majProj.entities.Providers;
import com.scm.majProj.entities.User;
import com.scm.majProj.helpers.AppConstants;
import com.scm.majProj.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    
    
    
    @Autowired
    private UserRepo userRepo; // Ensure this is not null

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            logger.info("OAuth2 authentication successful for user: {}", authentication.getName());
            // Redirect to the desired URL after successful authentication

            logger.info("Redirecting to /home after successful authentication");
               
            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            logger.info("Authorized Client Registration ID: {}", authorizedClientRegistrationId);

            var oauthUser=(DefaultOAuth2User) authentication.getPrincipal();

            oauthUser.getAttributes().forEach((key, value) -> {
                logger.info( key + ":" + value);
            });

            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstants.DEFAULT_USER_ROLE));
            user.setEmailVerified(true);
            user.setEnabled(true);


           
           
            if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {
                
                user.setEmail(oauthUser.getAttribute("email").toString());
                
                logger.info("User authenticated with Google");
            } 
            else if (authorizedClientRegistrationId.equals("github")) {
                String email = oauthUser.getAttribute("email")!= null ? oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString() + "@github.com";
                String name = oauthUser.getAttribute("login").toString();

                user.setEmail(email);
                String[] parts = name.split("\\s+", 2);
                String firstName = parts[0];
                String lastName = parts.length > 1 ? parts[1] : "";

                user.setFirstName(firstName);
                user.setLastName(lastName);
                

            } else {
                logger.warn("Unknown OAuth2 provider: {}", authorizedClientRegistrationId);
            }
            //google

            

            //github 

            // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

            // // logger.info(user.getName());

            // // user.getAttributes().forEach((key, value) -> {
            // //     logger.info("{} => {}", key, value);
            // // });

            // // logger.info(user.getAuthorities().toString());

            // String email = user.getAttribute("email").toString();
            // String name = user.getAttribute("name").toString();
            // String[] parts = name.split("\\s+", 2);

            // String firstName = parts[0];
            // String lastName  = parts.length > 1 ? parts[1] : "";
            // User user1 = new User();
            // user1.setEmail(email);
            // user1.setFirstName(firstName);
            // user1.setLastName(lastName);
            // user1.setUserId(UUID.randomUUID().toString());
            // user1.setProvider(Providers.GOOGLE);
            // user1.setProviderId(user.getName());
            // user1.setRoleList(List.of(AppConstants.DEFAULT_USER_ROLE));

            // // Check if the user already exists in the database
            if (userRepo.findByEmail(user.getEmail()).isEmpty()) {
                // Save the new user to the database
                userRepo.save(user);
                logger.info("New user created: {}", user.getEmail());
            } else {
                logger.info("User already exists: {}", user.getEmail());
            }

            new DefaultRedirectStrategy().sendRedirect(request, response, "/home");
        }

}
