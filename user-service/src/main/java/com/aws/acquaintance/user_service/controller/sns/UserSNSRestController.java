package com.aws.acquaintance.user_service.controller.sns;

import com.aws.acquaintance.user_service.model.Message;
import com.aws.acquaintance.user_service.service.SNSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@RestController
@Slf4j
public class UserSNSRestController {

    private final SNSService snsService;

    @Autowired
    public UserSNSRestController(SNSService snsService) {
        this.snsService = snsService;
    }

    //TODO: this method we don`t need because we do it in InitialConfiguration
    @PostMapping("/add-http-subscription")
    public ResponseEntity<?> subscribeHTTPToTopic(@RequestBody Message message) {

        log.info("---> UserSNSRestController: subscribeHTTPToTopic: message=" + message.getMessage());

        SubscribeResponse subscribeResponse = snsService.subscribeHTTPToImageToUserTopic(message.getMessage());

        return ResponseEntity.ok(subscribeResponse);
    }
}
