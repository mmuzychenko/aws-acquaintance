package com.aws.acquaintance.image_service.config.aws;//package com.springboot.image_service.config.aws;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.sns.AmazonSNSClient;
//import com.amazonaws.services.sns.AmazonSNSClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//public class SNSConfig {
//
//    //TODO: try to use this env var
//    @Value("${cloud.aws.region.static}")
//    private String region;
//
//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secretKey;
//
//
//    //TODO: remove @Primary ?
//    @Primary
//    @Bean
//    public AmazonSNSClient snsClient() {
//        return (AmazonSNSClient) AmazonSNSClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }
//}
