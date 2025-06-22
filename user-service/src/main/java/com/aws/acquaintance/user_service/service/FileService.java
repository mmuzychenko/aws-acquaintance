package com.aws.acquaintance.user_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    boolean sendToImageService(MultipartFile multipartFile);

//    boolean download(String keyName) throws IOException;
//
//    byte[] downloadRest(String keyName);
//
//    boolean delete(String keyName);
//
//    void listAll();
}
