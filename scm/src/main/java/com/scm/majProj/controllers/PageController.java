package com.scm.majProj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.majProj.entities.User;
import com.scm.majProj.forms.userForm;
import com.scm.majProj.services.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
public class PageController { 

    @Autowired
    private UserService userService; // Assuming you have a UserService to handle user-related operations
   
    @RequestMapping("home")
    public String homePage() {
        return "home"; // Return view name without .html extension
    }

    @RequestMapping("about")
    public String about() {
        return "about";
    }
    
    @RequestMapping("contact")
    public String contact() {
        return "contact";
    }
    
    @RequestMapping("register")
    public String register(Model model) {
        
        userForm userform = new userForm(); // Create a new userForm object
        userform.setFirstName("Dr Sachin");
        userform.setLastName("Sharma");
        model.addAttribute("userForm", userform); // Add the userForm object to the model
        return "register";
    }

    @RequestMapping(value="/registering", method = RequestMethod.POST)
    public String confirmRegister(@ModelAttribute userForm userform,  HttpSession session) {
        // Here you can handle the registration logic, such as saving the user to the database
        // User user = User.builder()
        // .firstName(userform.getFirstName())
        // .lastName(userform.getLastName())
        // .email(userform.getEmail())
        // .phoneNumber(userform.getPhoneNumber())
        // .password(userform.getPassword())   
        // .build();
        // userService.saveUser(user);
        // the above ones were not allowing default values // Assuming you have a saveUser method in your UserService
        
        User user = new User();
        user.setFirstName(userform.getFirstName());
        user.setLastName(userform.getLastName());
        user.setEmail(userform.getEmail());
        user.setPhoneNumber(userform.getPhoneNumber());
        user.setPassword(userform.getPassword());

          // You can also add validation and error handling here
          // For example, check if the email already exists, etc.
          // If registration is successful, redirect to a success page or home page
          // For now, just print the userForm object to the console for debugging
          User saveduser = userService.saveUser(user); // Save the user to the database
          
        System.out.println(userform);
          return "redirect:/home"; // Redirect to home page after registration
    }
    
    
}
