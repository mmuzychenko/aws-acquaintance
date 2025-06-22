package com.aws.acquaintance.user_service.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/step-function")
@Slf4j
public class StepFunctionRestController {

    @GetMapping("cancel-deletion")
    public void cancelDeletion(){
        log.info("---> StepFunctionRestController: cancelDeletion: "+ " cancel deletion of file");
    }
}
