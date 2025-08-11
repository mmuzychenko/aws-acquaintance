package com.aws.acquaintance.image_service.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.aws.acquaintance.image_service.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AmazonSNS snsClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${aws.sns.upload-topic}")
    private String uploadTopicArn;

    @Value("${aws.sns.delete-topic}")
    private String deleteTopicArn;

    @Value("${jwt.secret}")
    private String jwtSecret;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {

            // Extract userId from JWT
            String token = request.getHeader("Authorization").substring(7);
            String userId = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            String fileId = imageService.uploadFile(file, userId);
            Map<String, String> message = new HashMap<>();
            message.put("fileId", fileId);
            message.put("fileName", file.getOriginalFilename());
            message.put("recipient", "");
            snsClient.publish(new PublishRequest()
                    .withTopicArn(uploadTopicArn)
                    .withMessage(objectMapper.writeValueAsString(message)));
            return ResponseEntity.ok("File uploaded with ID: " + fileId);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/delete-file")
    public ResponseEntity<?> deleteFile(@RequestBody Map<String, String> payload) {
        String fileId = payload.get("fileId");
        imageService.deleteFile(fileId);
        snsClient.publish(new PublishRequest()
                .withTopicArn(deleteTopicArn)
                .withMessage("{\"fileId\": \"" + fileId + "\"}"));
        return ResponseEntity.ok("File deleted");
    }

}
