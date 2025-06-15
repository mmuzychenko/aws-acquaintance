package com.aws.acquaintance.image_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SNSMessageSender {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    public SNSMessageSender(NotificationMessagingTemplate notificationMessagingTemplate) {
        this.notificationMessagingTemplate = notificationMessagingTemplate;
    }

    public void send(String topicName, Object message, String subject) {
        log.info("---> SNSMessageSender: send: message=" + message);

        notificationMessagingTemplate.sendNotification(topicName, message, subject);
    }
}
