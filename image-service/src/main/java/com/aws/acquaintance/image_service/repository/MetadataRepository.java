package com.aws.acquaintance.image_service.repository;

import com.aws.acquaintance.image_service.model.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetadataRepository extends MongoRepository<Metadata, String> {

    Metadata findByName(String name);

    List<Metadata> findByUploadedBy(String uploadedBy);

    boolean existsByName(String name);

    void deleteByName(String name);

}

