package com.aws.acquaintance.user_service.controller;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.aws.acquaintance.user_service.model.User;
import com.aws.acquaintance.user_service.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final AWSStepFunctions stepFunctionsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.stepfunctions.state-machine}")
    private String stateMachineArn;

    public UserController(UserService userService, AWSStepFunctions stepFunctionsClient, ObjectMapper objectMapper) {
        this.userService = userService;
        this.stepFunctionsClient = stepFunctionsClient;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/sns/upload")
    public ResponseEntity<?> handleUploadEvent(@RequestBody String snsMessage) {
        try {
            JsonNode message = objectMapper.readTree(snsMessage).get("Message");
            String fileId = message.get("fileId").asText();
            stepFunctionsClient.startExecution(new com.amazonaws.services.stepfunctions.model.StartExecutionRequest()
                    .withStateMachineArn(stateMachineArn)
                    .withInput("{\"fileId\": \"" + fileId + "\"}"));
            return ResponseEntity.ok("Step Function triggered");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing SNS message: " + e.getMessage());
        }
    }

//    @PostMapping("/confirm/{fileId}")
//    public ResponseEntity<?> confirmFile(@PathVariable String fileId) {
//        String executionArn = findExecutionArn(fileId);
//        if (executionArn != null) {
//            stepFunctionsClient.stopExecution(new StopExecutionRequest().withExecutionArn(executionArn));
//            return ResponseEntity.ok("File deletion cancelled");
//        }
//        return ResponseEntity.badRequest().body("No active execution found for fileId: " + fileId);
//    }
//
//    private String findExecutionArn(String fileId) {
//        ListExecutionsRequest request = new ListExecutionsRequest()
//                .withStateMachineArn(stateMachineArn)
//                .withStatusFilter("RUNNING");
//        return stepFunctionsClient.listExecutions(request).getExecutions().stream()
//                .filter(exec -> exec.getInput().contains(fileId))
//                .findFirst()
//                .map(exec -> exec.getExecutionArn())
//                .orElse(null);
//    }
}
