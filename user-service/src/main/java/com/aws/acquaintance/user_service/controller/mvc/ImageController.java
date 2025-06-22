package com.aws.acquaintance.user_service.controller.mvc;


//import com.springboot.user_service.service.FileService;
import com.aws.acquaintance.user_service.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
@Slf4j
public class ImageController {

    //TODO: add exceptions handlers
    //TODO: handle exceptions - org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException: the request was rejected because its size (27671082) exceeds the configured maximum (10485760)

    private final FileService fileService;

    @Autowired
    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String showUploadPage() {
        log.info("--> ImageController: showUploadPage");
        return "uploadPage";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             String description,
                             Model model) {

        log.info("--> ImageController: uploadFile");

        String fileName = multipartFile.getOriginalFilename();

        log.info("--> File name: " + fileName);
        log.info("--> Description: " + description);

        String resultMessage = "";

        try {
            fileService.sendToImageService(multipartFile);
            resultMessage = "Your file has been uploaded successfully!";
        } catch (Exception e) {
//            resultMessage = "Error when uploading file! See server logs or contact your admin... ";
            resultMessage = "Error when uploading file! See server logs or contact your admin... "+e.getMessage()+"//////"+e.getStackTrace()+"//////"+e.getCause();
            log.info("--> Error when uploading file: " + e.getMessage());
        }

        model.addAttribute("resultMessage", resultMessage);
        return "messagePage";
    }
}
