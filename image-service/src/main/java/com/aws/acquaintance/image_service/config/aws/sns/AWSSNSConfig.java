package com.aws.acquaintance.image_service.config.aws.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

//TODO: 2 sns clients are used, 2 sns dependencies in pom.xml. Can we use one of them?????
@Configuration
public class AWSSNSConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;


    @Primary
    @Bean
    public AmazonSNSClient amazonSnsClient() {
        return (AmazonSNSClient) AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNSClient amazonSNSClient) {
        return new NotificationMessagingTemplate(amazonSNSClient);
    }


    //TODO: hardcoded Region
    @Bean
    public SnsClient snsClient() {

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
