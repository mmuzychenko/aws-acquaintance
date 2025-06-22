package com.aws.acquaintance.user_service.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.MultipartBodyBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Service
//@Slf4j
//public class FileServiceImpl implements FileService {
//
//    @Value("${image-service.server.baseurl}")
//    private String IMAGE_SERVICE_SERVER_BASE_URL;
//
//    private final WebClient webClient;
//
//    @Autowired
//    public FileServiceImpl(WebClient webClient) {
//        this.webClient = webClient;
//    }
//
//
//    @Override
//    public boolean sendToImageService(MultipartFile multipartFile) {
//        log.info("--> FileServiceImpl: sendToImageService");
//
//        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//        builder.part("file", multipartFile.getResource());
//
//        String url = IMAGE_SERVICE_SERVER_BASE_URL + "/file-rest/upload";
//
//        String result = webClient
//                .post()
//                .uri(url)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(builder.build()))
//                .retrieve()
//                .bodyToMono(String.class).block();
//
//        //If exception happens during webClient request, then we don't go here
//        //exc message = 400 Bad Request from POST http://localhost:8082/file-rest/upload
//        log.info("--> result=" + result);
//        return true;
//
//    }
///*
//    @Override
//    public boolean download(String keyName) throws IOException {
//        log.info("--> FileServiceImpl: download");
//        //keyName - is a full path to file on AWS S3
//
//        GetObjectRequest request = GetObjectRequest.builder()
//                .bucket(BUCKET_NAME)
//                .key(keyName)
//                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
//                .build();
//
//        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request);
//
//        String fileName = new File(keyName).getName();
//
//        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
//
//        byte[] buffer = new byte[4096];
//        int bytesRead = -1;
//
//        while ((bytesRead = responseInputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//
//        responseInputStream.close();
//        outputStream.close();
//
//        //TODO: add check if the file was downloaded
//        return true;
//    }
//
//    public byte[] downloadRest(String keyName) {
//        log.info("--> FileServiceImpl: downloadRest");
//        //keyName - is a full path to file on AWS S3
//        GetObjectRequest request = GetObjectRequest.builder()
//                .bucket(BUCKET_NAME)
//                .key(keyName)
//                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
//                .build();
//
//        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request);
//
//        String fileName = new File(keyName).getName();
//
//        byte[] content = null;
//        try {
//            content = responseInputStream.readAllBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }
//
//    @Override
//    public boolean delete(String keyName) {
//        log.info("--> FileServiceImpl: delete");
//        //keyName - is a full path to file on AWS S3
//
//        DeleteObjectRequest request = DeleteObjectRequest.builder()
//                .bucket(BUCKET_NAME)
//                .key(keyName)
//                //.acl("public-read")//TODO: software.amazon.awssdk.services.s3.model.S3Exception: The bucket does not allow ACLs
//                .build();
//
//        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(request);
//        log.info("--> deleteObjectResponse=" + deleteObjectResponse);
//        log.info("--> deleteObjectResponse.deleteMarker()=" + deleteObjectResponse.deleteMarker());
//        //TODO: add check if the file was deleted
//        return true;
//    }
//
//    @Override
//    public void listAll() {
//        log.info("--> FileServiceImpl: listAll");
//
//        ListObjectsRequest request = ListObjectsRequest.builder()
//                .bucket(BUCKET_NAME)
//                .build();
//
//        ListObjectsResponse response = s3Client.listObjects(request);
//        List<S3Object> s3Objects = response.contents();
//
//        for (S3Object s3Object : s3Objects) {
//            log.info("Key: " + s3Object.key());
//            log.info("Owner: " + s3Object.owner());
//            log.info("Size: " + s3Object.size());
//        }
//    }
//
// */
//}






import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${image-service.server.baseurl}")
    private String IMAGE_SERVICE_SERVER_BASE_URL;

    private final WebClient webClient;

    @Autowired
    public FileServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public boolean sendToImageService(MultipartFile multipartFile) {
        log.info("--> FileServiceImpl: sendToImageService");

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", multipartFile.getResource());

        String url = IMAGE_SERVICE_SERVER_BASE_URL + "/file-rest/upload";
//        String url = "some-url";

        String result = webClient
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class).block();

        //If exception happens during webClient request, then we don't go here
        //exc message = 400 Bad Request from POST http://localhost:8082/file-rest/upload
        log.info("--> result=" + result);
        return true;

    }

}