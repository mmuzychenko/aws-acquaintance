package com.aws.acquaintance.image_service.service;

import com.aws.acquaintance.image_service.model.NotificationRequest;

public interface SNSService {

    void sendSNS(NotificationRequest notificationRequest);
}
