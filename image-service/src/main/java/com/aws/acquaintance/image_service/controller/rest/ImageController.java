package com.aws.acquaintance.image_service.controller.rest;

import com.aws.acquaintance.image_service.service.ImageService;
import com.aws.acquaintance.image_service.service.MetadataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MetadataService metadataService;


    @GetMapping
    @RequestMapping("/test")
    public String test() {
        log.info("--> ImageController: test");
        return "uploadPage";
    }


    @SneakyThrows
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadImage(@RequestParam(value = "file") MultipartFile file) {
//            , @AuthenticationPrincipal Jwt jwt) {

        String fileName = file.getOriginalFilename();

        log.info("--> ImageRestController: upload image");
//        log.info("--> ImageRestController: jwt" + jwt);
//
//        String uploadedBy = (String) jwt.getClaims().get("preferred_username");
//        String email = (String) jwt.getClaims().get("email");

//        log.info("--> ImageRestController: uploadedBy" + uploadedBy);
//        log.info("--> ImageRestController: email" + email);

//        Metadata metadata = metadataService.extractMetadata(multipartFile, description, uploadedBy);
//        log.info("--> ImageRestController: metadata" + metadata);

        String resultMessage = "";

        try {
//            metadataService.validateIfAlreadyExists(metadata);
//
//            //TODO: Instant.now().toString() -> Instant.now()
//            //TODO: decide what to do with metadata
//            metadataService.save(metadata);

            imageService.upload(fileName, file.getInputStream());

            resultMessage = "Your image has been uploaded successfully.";
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (IOException e) {
            resultMessage = "Error when uploading image: " + e.getMessage();
            throw new RuntimeException(resultMessage, e);
        }
    }


//    @DeleteMapping("/delete/{imageName}")
//    public ResponseEntity<String> deleteImage(@PathVariable String fileName) {
//        boolean isFileDeleted = imageService.delete(fileName);
//
//        //TODO: decide what to do with metadata
//        //TODO: check if the file was realy deleted (existed before and disappear then)
//        boolean isMetadataDeleted = metadataService.delete(fileName);
//        String result = "";
//        if (isFileDeleted) {
//            result = "File was deleted successfully!";
//        } else {
//            result = "File was NOT deleted!";
//        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
}
