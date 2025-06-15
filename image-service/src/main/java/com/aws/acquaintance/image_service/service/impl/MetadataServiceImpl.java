package com.aws.acquaintance.image_service.service.impl;

//import com.springboot.image_service.model.Metadata;
//import com.springboot.image_service.repository.MetadataRepository;
import com.aws.acquaintance.image_service.model.Metadata;
import com.aws.acquaintance.image_service.repository.MetadataRepository;
import com.aws.acquaintance.image_service.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {

    //TODO: decide what to do with metadata logic
    //TODO: using mock for MongoDB for now

    private final MetadataRepository metadataRepository;

    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }


    //TODO: this is MOCK
//    @Override
//    public Metadata save(Metadata metadata) {
//        log.info("--> MetadataServiceImpl: save");
//        log.info("--> metadata=" + metadata);
//
//        return metadataRepository.save(metadata);
//    }
    @Override
    public Metadata save(Metadata metadata) {
        log.info("--> MetadataServiceImpl: save");
        log.info("--> metadata=" + metadata);

        return metadata;
    }

    //TODO: this is MOCK
//    @Override
//    public void validateIfAlreadyExists(Metadata metadata) {
//        log.info("--> MetadataServiceImpl: validateIfAlreadyExists");
//        if (existByFileName(metadata.getFileName())) {
//            throw new IllegalArgumentException("Metadata for file name: " + metadata.getFileName() + " already exists");
//        }
//    }
    @Override
    public void validateIfAlreadyExists(Metadata metadata) {
        log.info("--> MetadataServiceImpl: validateIfAlreadyExists");
    }

    //TODO: this is MOCK
//    @Override
//    public boolean delete(String fileName) {
//        log.info("--> MetadataServiceImpl: delete");
//
//        Metadata metadataToDelete = metadataRepository.findByFileName(fileName);
//
//        if (metadataToDelete == null) {
//            throw new IllegalArgumentException("Not found metadata for file name: " + fileName);
//        } else {
//            metadataRepository.delete(metadataToDelete);
//            return true;
//        }
//    }
    @Override
    public boolean delete(String fileName) {
        log.info("--> MetadataServiceImpl: delete");
        return true;
    }


    //TODO: do we need this method or just move this to controller?
    @Override
    public Metadata extractMetadata(MultipartFile multipartFile, String description, String uploadedBy) {

        String fileName = multipartFile.getOriginalFilename();

        return Metadata.builder()
                .fileName(fileName)
                .fileDescription(description)
                //.createdBy(createdBy)
                //.createdAt(createdAt)
                .uploadedBy(uploadedBy)
                .uploadedAt(Instant.now())
                .build();
    }

    //TODO: this is MOCK
//    @Override
//    public boolean existByFileName(String fileName) {
//        log.info("--> MetadataServiceImpl: existByFileName");
//        Metadata metadata = metadataRepository.findByFileName(fileName);
//        return metadata != null;
//    }
    @Override
    public boolean existByFileName(String fileName) {
        log.info("--> MetadataServiceImpl: existByFileName");
        return false;
    }
}

