package com.aws.acquaintance.image_service.service.impl;

import com.aws.acquaintance.image_service.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

    @Autowired
    private S3Client s3Client;

//    @Autowired
//    public ImageService(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }

    @Override
    public boolean upload(String fileName, InputStream inputStream) throws IOException {
        log.info("--> File service: upload");

        final boolean[] isUploaded = new boolean[]{false};

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));


        //wait until folder is created and then do some other tasks
        S3Waiter waiter = s3Client.waiter();
        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

//        WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectExists(waitRequest);
//        waitResponse.matched().response().ifPresent(x -> {
//            log.info(String.valueOf(x));
//            isUploaded[0] = true;
//        });
//
//        log.info("The file: " + fileName + " has been uploaded.");
//
//        snsService.sendSNS(
//                new NotificationRequest(TOPIC_NAME, "file uploaded", SNS_SUBJECT_UPLOAD_CONFIRMATION)
//        );
        return isUploaded[0];
    }

    public boolean delete(String keyName) {
        log.info("--> File service: delete");
        //keyName - is a full path to file on AWS S3

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(keyName)
                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
                .build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(request);
        log.info("--> deleteObjectResponse=" + deleteObjectResponse);
        log.info("--> deleteObjectResponse.deleteMarker()=" + deleteObjectResponse.deleteMarker());
        //TODO: add check if the file was deleted

//        snsService.sendSNS(
//                new NotificationRequest(TOPIC_NAME, "file deleted", SNS_SUBJECT_DELETE_CONFIRMATION)
//        );
        return true;
    }
}

