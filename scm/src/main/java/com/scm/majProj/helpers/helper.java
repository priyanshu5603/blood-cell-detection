package com.scm.majProj.helpers;

import java.security.Principal;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public class helper {
    public static String getEmailOfLoggedInUser(Principal principal) {

        if(principal instanceof OAuth2AuthenticatedPrincipal){
            
        }
        else{
            return principal.getName(); // Assuming the principal is a String with the email
        }
        return "";
    }
}
