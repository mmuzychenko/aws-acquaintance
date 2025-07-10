package com.aws.acquaintance.image_service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aws.acquaintance.image_service.model.ImageMetadata;
import com.aws.acquaintance.image_service.repository.ImageMetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.sns.topic-arn}")
    private String snsTopicArn;

    private final AmazonS3 s3Client;
    private final ImageMetadataRepository metadataRepository;

    public ImageService(AmazonS3 s3Client, ImageMetadataRepository metadataRepository,
                        @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.metadataRepository = metadataRepository;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        String fileId = UUID.randomUUID().toString();
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileId);
        try {
            file.transferTo(tempFile);
            s3Client.putObject(new PutObjectRequest(bucketName, fileId, tempFile));
            ImageMetadata metadata = new ImageMetadata();
            metadata.setId(fileId);
            metadata.setFileName(file.getOriginalFilename());
            metadata.setUserId(userId);
            metadata.setUploadedAt(LocalDateTime.now());
            metadataRepository.save(metadata);
            return fileId;
        } finally {
            tempFile.delete();
        }
    }

    public void deleteFile(String fileId) {
        s3Client.deleteObject(bucketName, fileId);
        metadataRepository.deleteById(fileId);
    }


}
