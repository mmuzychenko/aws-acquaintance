package com.aws.acquaintance.image_service.controller.rest;

import com.aws.acquaintance.image_service.model.Metadata;
import com.aws.acquaintance.image_service.service.ImageService;
import com.aws.acquaintance.image_service.service.MetadataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> uploadImage(@RequestParam(value = "file") MultipartFile file, @RequestParam(defaultValue = "File description") String description
            , @AuthenticationPrincipal Jwt jwt) {

        String fileName = file.getOriginalFilename();

        log.info("--> ImageRestController: upload image");
        log.info("--> ImageRestController: jwt" + jwt);

        String uploadedBy = (String) jwt.getClaims().get("preferred_username");
        String email = (String) jwt.getClaims().get("email");

        log.info("--> ImageRestController: uploadedBy" + uploadedBy);
        log.info("--> ImageRestController: email" + email);

        Metadata metadata = metadataService.extractMetadata(file, description, uploadedBy);
        log.info("--> ImageRestController: metadata" + metadata);

        String resultMessage = "";

        try {
            metadataService.validateIfAlreadyExists(metadata);
            metadataService.save(metadata);

            imageService.upload(fileName, file.getInputStream());

            resultMessage = "Your image has been uploaded successfully.";
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (IOException e) {
            resultMessage = "Error when uploading image: " + e.getMessage();
            throw new RuntimeException(resultMessage, e);
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteImage(@PathVariable String fileName) {
        boolean isFileDeleted = imageService.delete(fileName);

        metadataService.delete(fileName);
        String result = "";
        if (isFileDeleted) {
            result = "File was deleted successfully!";
        } else {
            result = "File was NOT deleted!";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable String fileName) {
        log.info("--> Image Controller: download image");

        byte[] data = imageService.downloadRest(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

}
