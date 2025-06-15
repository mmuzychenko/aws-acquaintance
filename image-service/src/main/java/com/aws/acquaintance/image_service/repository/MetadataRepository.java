package com.aws.acquaintance.image_service.repository;

import com.aws.acquaintance.image_service.model.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends MongoRepository<Metadata, String> {

    Metadata findByFileName(String fileName);
/*
    //TODO: refactor to use real DB

    public static final List<Metadata> metadataList = new ArrayList<>();


    public  boolean save(Metadata metadata) {
        metadataList.add(metadata);
        return false;
    }


    public Metadata getByFileName(String fileName) {
        return metadataList.stream()
                .filter(m -> fileName.equals(m.getFileName()))
                .findFirst()
                .orElse(null);
    }

    public List<Metadata> getByUserId(String userId) {
        return metadataList.stream()
                .filter(m -> userId.equals(m.getUploadedBy()))
                .collect(Collectors.toList());
    }

    public boolean update(String fileName, Metadata metadata) {
        Metadata metadataToUpdate = getByFileName(fileName);
        if (metadataToUpdate != null) {
            metadataToUpdate.setId(metadata.getId());
            metadataToUpdate.setFileName(metadata.getFileName());
            metadataToUpdate.setFileDescription(metadata.getFileDescription());
            metadataToUpdate.setUploadedBy(metadata.getUploadedBy());
            metadataToUpdate.setUploadedAt(metadata.getUploadedAt());
            return true;
        }
        return false;
    }

    public boolean delete(String fileName) {
        Metadata metadataToDelete = getByFileName(fileName);
        if (metadataToDelete != null) {
            return metadataList.remove(metadataToDelete);
        }
        return false;
    }

    public boolean existByFileName(String fileName) {
        return metadataList.stream()
                .anyMatch(m -> fileName.equals(m.getFileName()));
    }
 */
}

