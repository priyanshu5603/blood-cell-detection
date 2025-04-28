package com.scm.majProj.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.majProj.helpers.helper;


@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("dashboard")
    public String userDashboard() {
        return "user/dashboard"; // Return view name without .html extension
    }

    @RequestMapping("profile")
    public String profile(Principal principal) {
        // You can use the Principal object to get user details if needed
        String username = helper.getEmailOfLoggedInUser(principal);// Get the authenticated user's email
        return "user/profile"; // Return view name without .html extension
    }

    
}
