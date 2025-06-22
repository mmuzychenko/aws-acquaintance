package com.aws.acquaintance.user_service.controller;//package com.springboot.user_service.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
//import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
//import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
//import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
//import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
//import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@Controller
////@RequestMapping("/sns-rest")
//@RequestMapping("/image-topic")
//@Slf4j
//public class SNSController {
//
//    @PostMapping("/upload-complete")
//    public String uploadComplete(@RequestBody String message) {
//        log.info("--> SNSRestController: uploadComplete: message=" + message);
//        return "uploadComplete";
//    }
//
//
//    @NotificationSubscriptionMapping
//    public void handleSubscriptionMessage(NotificationStatus status) throws IOException {
//        //We subscribe to start receive the message
//
//        log.info("--> SNSRestController: handleSubscriptionMessage:status="+status.toString());
//        status.confirmSubscription();
//    }
//
//    @NotificationMessageMapping
//    public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) {
//        // ...
//        log.info("--> SNSRestController: handleNotificationMessage:message="+message);
//    }
//
//    @NotificationUnsubscribeConfirmationMapping
//    public void handleUnsubscribeMessage(NotificationStatus status) {
//        //e.g. the client has been unsubscribed and we want to "re-subscribe"
//
//        log.info("--> SNSRestController:handleUnsubscribeMessage:status="+status.toString());
//        status.confirmSubscription();
//    }
//}
