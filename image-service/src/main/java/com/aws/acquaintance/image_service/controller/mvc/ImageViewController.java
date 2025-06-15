package com.aws.acquaintance.image_service.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
@Slf4j
public class ImageViewController {


    @GetMapping
    public String showUploadPage() {
        log.info("--> ImageController: showUploadPage");
        return "uploadPage";
    }

}
