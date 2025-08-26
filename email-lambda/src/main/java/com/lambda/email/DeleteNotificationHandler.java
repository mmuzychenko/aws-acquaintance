//package com.lambda.email;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.SNSEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import software.amazon.awssdk.services.ses.SesClient;
//import software.amazon.awssdk.services.ses.model.*;
//import java.util.HashMap;
//import java.util.Map;
//
//public class DeleteNotificationHandler implements RequestHandler<SNSEvent, Map<String, Object>> {
//    private final SesClient sesClient;
//    private final ObjectMapper objectMapper;
//
//    public DeleteNotificationHandler() {
//        this.sesClient = SesClient.create();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    @Override
//    public Map<String, Object> handleRequest(SNSEvent event, Context context) {
//        try {
//            String message = event.getRecords().get(0).getSNS().getMessage();
//            Map<String, String> messageData = objectMapper.readValue(message, Map.class);
//            String fileId = messageData.get("fileId");
//
//            // Placeholder: Replace with user email lookup (e.g., from PostgreSQL)
//            String userEmail = "user@example.com";
//
//            sesClient.sendEmail(SendEmailRequest.builder()
//                    .source("no-reply@yourdomain.com")
//                    .destination(Destination.builder().toAddresses(userEmail).build())
//                    .message(Message.builder()
//                            .subject(Content.builder().data("File Deleted").build())
//                            .body(Body.builder().text(Content.builder()
//                                            .data(String.format("File ID: %s has been deleted.", fileId))
//                                            .build())
//                                    .build())
//                            .build())
//                    .build());
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("statusCode", 200);
//            response.put("body", "Email sent");
//            return response;
//        } catch (Exception e) {
//            context.getLogger().log("Error: " + e.getMessage());
//            Map<String, Object> response = new HashMap<>();
//            response.put("statusCode", 500);
//            response.put("body", "Error sending email: " + e.getMessage());
//            return response;
//        }
//    }
//}
