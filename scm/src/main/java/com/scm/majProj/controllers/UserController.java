package com.scm.majProj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("dashboard")
    public String userDashboard() {
        return "user/dashboard"; // Return view name without .html extension
    }

    @RequestMapping("profile")
    public String profile() {
        return "user/profile"; // Return view name without .html extension
    }

    
}
