package com.scm.majProj.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class LeukemiaMLService {

    @Value("${ml.api.url:http://localhost:5000}")
    private String mlApiUrl;
    
    private final RestTemplate restTemplate;
    
    public LeukemiaMLService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Send image to ML model API and get prediction result
     */
    public Map<String, Object> predictLeukemiaStage(MultipartFile imageFile) throws IOException {
        String url = mlApiUrl + "/predict";
        
        // Convert image to Base64
        byte[] imageBytes = imageFile.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        
        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", base64Image);
        
        // Configure headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Create HTTP entity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        try {
            // Make API call
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    url, 
                    HttpMethod.POST, 
                    requestEntity, 
                    Map.class
            );
            
            // Return the prediction result
            return responseEntity.getBody();
        } catch (Exception e) {
            // Handle API errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("prediction", "Error");
            errorResponse.put("confidence", 0.0);
            errorResponse.put("error", e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * Method to interpret prediction results and provide a medical description
     */
    public String getResultDescription(String prediction) {
        return switch (prediction) {
            case "Benign" -> "No signs of leukemia detected. The blood cells appear normal.";
            case "Early" -> "Early stage leukemia detected. Early intervention recommended.";
            case "Pre" -> "Pre-advanced stage leukemia detected. Immediate medical attention required.";
            case "Pro" -> "Advanced stage leukemia detected. Urgent medical intervention needed.";
            default -> "Unable to determine leukemia stage. Please consult a medical professional.";
        };
    }
}