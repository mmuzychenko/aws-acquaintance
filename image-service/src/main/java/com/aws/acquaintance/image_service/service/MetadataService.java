package com.aws.acquaintance.image_service.service;

import com.aws.acquaintance.image_service.model.Metadata;
import org.springframework.web.multipart.MultipartFile;

public interface MetadataService {

    Metadata save(Metadata metadata);

    void validateIfAlreadyExists(Metadata metadata);
    /*
        Metadata getByFileName(String fileName);

        List<Metadata> getByUserId(String userId);

        boolean update(String id, Metadata metadata);
     */
    boolean delete(String fileName);

    Metadata extractMetadata(MultipartFile multipartFile, String description, String uploadedBy);

    boolean existByFileName(String fileName);
}
