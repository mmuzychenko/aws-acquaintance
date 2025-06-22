package com.aws.acquaintance.image_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("Metadata")
public class Metadata {

    @Id
    private String id;
    private String name;
    private String description;

    private String uploadedBy;
    private Instant uploadedAt;

    private String createdBy;
    private Instant createdAt;
}

