package com.aws.acquaintance.image_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationRequest {
    private String topicName;
    private String message;
    private String subject;

    @JsonCreator
    public NotificationRequest(@JsonProperty("topicName") String topicName, @JsonProperty("message") String message, @JsonProperty("subject") String subject) {
        this.topicName = topicName;
        this.message = message;
        this.subject = subject;
    }
}
