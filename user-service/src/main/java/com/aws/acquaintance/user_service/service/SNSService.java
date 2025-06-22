package com.aws.acquaintance.user_service.service;

import software.amazon.awssdk.services.sns.model.SubscribeResponse;

public interface SNSService {

    SubscribeResponse subscribeHTTPToImageToUserTopic(String url);

    void processSNSMessage(String message, String subject);
}
