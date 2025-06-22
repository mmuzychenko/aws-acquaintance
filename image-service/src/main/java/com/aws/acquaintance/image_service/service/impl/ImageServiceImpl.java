package com.aws.acquaintance.image_service.service.impl;

import com.aws.acquaintance.image_service.model.NotificationRequest;
import com.aws.acquaintance.image_service.service.ImageService;
import com.aws.acquaintance.image_service.service.SNSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.*;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private static final String SNS_SUBJECT_UPLOAD_CONFIRMATION = "SNS from image-service. File Uploaded";
    private static final String SNS_SUBJECT_DELETE_CONFIRMATION = "SNS from image-service. File Deleted";


    @Value("${cloud.aws.topic.name}")
    private String TOPIC_NAME;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SNSService snsService;



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

        S3Waiter waiter = s3Client.waiter();
        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectExists(waitRequest);
        waitResponse.matched().response().ifPresent(x -> {
            log.info(String.valueOf(x));
            isUploaded[0] = true;
        });

        log.info("The file: " + fileName + " has been uploaded.");

        snsService.sendSNS(new NotificationRequest(TOPIC_NAME, "file uploaded", SNS_SUBJECT_UPLOAD_CONFIRMATION));

        return isUploaded[0];
    }

    public boolean delete(String keyName) {
        log.info("--> File service: delete");

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(keyName)
                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
                .build();

        DeleteObjectResponse response = s3Client.deleteObject(request);
        log.info("--> DeleteObjectResponse=" + response);
        log.info("--> DeleteObjectResponse.deleteMarker()=" + response.deleteMarker());

        snsService.sendSNS(
                new NotificationRequest(TOPIC_NAME, "file deleted", SNS_SUBJECT_DELETE_CONFIRMATION)
        );
        return true;
    }

    @Override
    public boolean download(String keyName) throws IOException {
        log.info("--> FileServiceImpl: download");
        //keyName - is a full path to file on AWS S3

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(keyName)
                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request);

        String fileName = new File(keyName).getName();

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName));

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = responseInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        responseInputStream.close();
        outputStream.close();

        return true;
    }

    public byte[] downloadRest(String keyName) {
        log.info("--> FileServiceImpl: downloadRest");
        //keyName - is a full path to file on AWS S3
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(keyName)
                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request);

        String fileName = new File(keyName).getName();

        byte[] content = null;
        try {
            content = responseInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

