package com.aws.acquaintance.user_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Message {
    private String message;

    @JsonCreator
    public Message(@JsonProperty("message") String message) {
        this.message = message;
    }
}