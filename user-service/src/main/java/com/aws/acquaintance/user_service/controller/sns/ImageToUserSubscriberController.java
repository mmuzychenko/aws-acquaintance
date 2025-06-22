package com.aws.acquaintance.user_service.controller.sns;

//import com.springboot.user_service.service.SNSService;
import com.aws.acquaintance.user_service.service.SNSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image-to-user") // IMPORTANT: image-to-user is a name of topic!
@Slf4j
public class ImageToUserSubscriberController {

    private final SNSService snsService;

    @Autowired
    public ImageToUserSubscriberController(SNSService snsService) {
        this.snsService = snsService;
    }

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message, @NotificationSubject String subject) {
        log.info("---> Received message: {}, having subject: {}", message, subject);

        snsService.processSNSMessage(message, subject);
    }

    @NotificationUnsubscribeConfirmationMapping
    public void confirmSubscriptionMessage(NotificationStatus notificationStatus) {
        log.info("---> Unsubscribed from Topic");

        notificationStatus.confirmSubscription();
    }

    @NotificationSubscriptionMapping
    public void confirmUnsubscribeMessage(NotificationStatus notificationStatus) {
        log.info("---> Subscribed to Topic");

        notificationStatus.confirmSubscription();
    }
}
