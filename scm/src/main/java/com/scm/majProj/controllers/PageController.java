package com.scm.majProj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.scm.majProj.entities.User;
import com.scm.majProj.forms.userForm;
import com.scm.majProj.services.UserService;
import com.scm.majProj.services.LeukemiaMLService; // Add this new service

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController { 

    @Autowired
    private UserService userService;
    
    @Autowired
    private LeukemiaMLService leukemiaMLService; // Inject the new ML service
   
    @RequestMapping("home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @RequestMapping("about")
    public String about() {
        return "about";
    }

    @RequestMapping("service")
    public String service() {
        return "service";
    }
    
    @GetMapping("/service")
    public String servicePage() {
        return "service";
    }

    // REST endpoint for image analysis
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> analyzeImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            // Process the image using ML service
            Map<String, Object> result = leukemiaMLService.predictLeukemiaStage(imageFile);
            
            // Add description based on prediction
            String prediction = (String) result.get("prediction");
            String description = leukemiaMLService.getResultDescription(prediction);
            result.put("description", description);
            
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to process image: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Handle form submission and display results
    @PostMapping("/service")
    public String processImage(@RequestParam("image") MultipartFile imageFile, Model model) {
        try {
            // Process the image
            Map<String, Object> result = leukemiaMLService.predictLeukemiaStage(imageFile);
            
            // Add results to model for Thymeleaf template
            String prediction = (String) result.get("prediction");
            double confidence = ((Number) result.get("confidence")).doubleValue() * 100;
            String confidenceFormatted = String.format("%.2f%%", confidence);
            
            model.addAttribute("prediction", prediction);
            model.addAttribute("confidence", confidenceFormatted);
            model.addAttribute("description", leukemiaMLService.getResultDescription(prediction));
            
            return "analysis-result";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to process image: " + e.getMessage());
            return "error";
        }
    }

    @RequestMapping("analysis-result")
    public String results() {
        return "analysis-result";
    }
    
    
    @RequestMapping("contact")
    public String contact() {
        return "contact";
    }
    
    @RequestMapping("register")
    public String register(Model model) {
        userForm userform = new userForm();
        userform.setFirstName("Dr Sachin");
        userform.setLastName("Sharma");
        model.addAttribute("userForm", userform);
        return "register";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping(value="/registering", method = RequestMethod.POST)
    public String confirmRegister(@ModelAttribute userForm userform, HttpSession session) {
        User user = new User();
        user.setFirstName(userform.getFirstName());
        user.setLastName(userform.getLastName());
        user.setEmail(userform.getEmail());
        user.setPhoneNumber(userform.getPhoneNumber());
        user.setPassword(userform.getPassword());

        User saveduser = userService.saveUser(user);
        
        System.out.println(userform);
        return "redirect:/home";
    }
}