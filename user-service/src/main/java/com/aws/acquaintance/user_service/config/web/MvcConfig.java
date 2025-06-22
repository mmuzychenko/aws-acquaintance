package com.aws.acquaintance.user_service.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
    }

    //TODO: Home page icon not seen

    //TODO: remove unused images in resources dir
/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                        "/images/**",
                        "/css/**",
                        "/js/**")
                .addResourceLocations(
                        "/resources/images/",
                        "/resources/css/",
                        "/resources/js/");
    }*/
}

