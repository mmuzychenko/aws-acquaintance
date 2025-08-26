package com.aws.acquaintance.image_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "image_metadata")
public class ImageMetadata {
    private String id;
    private String fileName;
    private String userId;
    private LocalDateTime uploadedAt;

}
