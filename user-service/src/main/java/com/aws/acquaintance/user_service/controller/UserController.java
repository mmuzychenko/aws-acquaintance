package com.aws.acquaintance.user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/images")
public class UserController {


//    private final BookService bookService;

    @GetMapping
    public String showUploadPage() {
//        log.info("--> ImageController: showUploadPage");
        return "homePage";
    }

//    public BookController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @GetMapping("/viewBooks")
//    public String viewBooks(Model model) {
//        model.addAttribute("books", bookService.getBooks());
//        return "view-books";
//    }
}
