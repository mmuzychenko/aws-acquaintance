package com.aws.acquaintance.image_service.repository;

import com.aws.acquaintance.image_service.model.ImageMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageMetadataRepository extends MongoRepository<ImageMetadata, String> {

    ImageMetadata findByFileName(String fileName);
}
