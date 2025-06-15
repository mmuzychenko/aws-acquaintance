package com.aws.acquaintance.image_service.config.aws.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    /*
    added these env vars to IDE configuration:
    AWS_ACCESS_KEY_ID=;
    AWS_REGION=eu-west-3;
    AWS_SECRET_ACCESS_KEY=;
     */

    //TODO: use env vars from properties as in SNSConfig

    @Bean
    public S3Client s3Client(){
        return S3Client.builder().build();
    }
}
