package com.aws.acquaintance.user_service.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
//import software.amazon.awssdk.services.sns.SnsClient;
//import software.amazon.awssdk.services.sns.model.SnsException;
//import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Service
@Slf4j
public class SNSServiceImpl implements SNSService {

    public static final String PROTOCOL_NAME_HTTP = "http" ;

    @Value("${cloud.aws.topic.arn}")
    private String topicArn;

    private final AmazonSNSClient amazonSnsClient;
    private final SnsClient snsClient;
    private final StepFunctionService stepFunctionService;

    @Autowired
    public SNSServiceImpl(AmazonSNSClient amazonSnsClient, SnsClient snsClient, StepFunctionService stepFunctionService) {
        this.amazonSnsClient = amazonSnsClient;
        this.snsClient = snsClient;
        this.stepFunctionService = stepFunctionService;
    }

    public SubscribeResponse subscribeHTTPToImageToUserTopic(String url) {

        SubscribeResponse response = SubscribeResponse.builder().build();

        try {
            software.amazon.awssdk.services.sns.model.SubscribeRequest request = software.amazon.awssdk.services.sns.model.SubscribeRequest.builder()
                    .protocol(PROTOCOL_NAME_HTTP)
                    .endpoint(url)
                    .returnSubscriptionArn(true)
                    .topicArn(topicArn)
                    .build();

            log.info("---> request=" + request);

            response = snsClient.subscribe(request);

            log.info("---> response=" + response);

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return response;
    }

    @Override
    public void processSNSMessage(String message, String subject) {
        log.info("---> SNSServiceImpl: processSNSMessage: message=" + message);

        //TODO: add if logic with message and subject, throw exception
        stepFunctionService.startStepFunction();
    }
}
