package com.lambda.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmailLambda implements RequestHandler<SNSEvent, Object> {


    private static final String EMAIL_SUBJECT = "From AWS Lambda" ;
    private static final String SNS_SUBJECT_UPLOAD_CONFIRMATION = "SNS image-service. Image uploaded" ;
    private static final String SNS_SUBJECT_DELETE_CONFIRMATION = "SNS image-service. Image deleted" ;

    private static final Logger LOGGER = Logger.getLogger(EmailLambda.class.getName());


    @Override
    public Boolean handleRequest(SNSEvent request, Context context) {
        //String email = request.getRecords().get(0).getSNS().getMessage();
        String email = "obonemax@gmail.com" ;

        LOGGER.info("--> request=" + request);
        LOGGER.info("--> request.getRecords()=" + request.getRecords());
        LOGGER.info("--> request.getRecords().get(0)=" + request.getRecords().get(0));
        LOGGER.info("--> request.getRecords().get(0).getSNS()=" + request.getRecords().get(0).getSNS());
        LOGGER.info("--> request.getRecords().get(0).getSNS().getSubject()=" + request.getRecords().get(0).getSNS().getSubject());
        LOGGER.info("--> request.getRecords().get(0).getSNS().getMessage()=" + request.getRecords().get(0).getSNS().getMessage());

        StringBuilder text = new StringBuilder()
                .append("--> request=").append(request).append("\n")
                .append("--> request.getRecords()=").append(request.getRecords()).append("\n")
                .append("--> request.getRecords().get(0)=").append(request.getRecords().get(0)).append("\n")
                .append("--> request.getRecords().get(0).getSNS()=").append(request.getRecords().get(0).getSNS()).append("\n")
                .append("--> request.getRecords().get(0).getSNS().getSubject()=").append(request.getRecords().get(0).getSNS().getSubject()).append("\n")
                .append("--> request.getRecords().get(0).getSNS().getMessage()=").append(request.getRecords().get(0).getSNS().getMessage());


        String subject = request.getRecords().get(0).getSNS().getSubject();

//        switch (subject) {
//            case SNS_SUBJECT_UPLOAD_CONFIRMATION: {
//                text.append("\n FILE UPLOADED !");
//            }
//            case SNS_SUBJECT_DELETE_CONFIRMATION: {
//                text.append("\n FILE DELETED !");
//            }
//            default: {
//                throw new IllegalArgumentException("Unsupported subject of SNS notification!");
//            }
//        }

        if (SNS_SUBJECT_UPLOAD_CONFIRMATION.equals(subject)) {
            text.append("\n FILE UPLOADED !");
        } else if (SNS_SUBJECT_DELETE_CONFIRMATION.equals(subject)) {
            text.append("\n FILE DELETED !");
        } else {
            throw new IllegalArgumentException("Unsupported subject of SNS notification!");
        }
        sendEmail(email, EMAIL_SUBJECT, text.toString());
        return Boolean.TRUE;
    }


    public static void sendEmail(String emailTo, String subject, String text) {

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.EU_WEST_3).build();

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(emailTo))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(text)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource("obonemax@gmail.com");

            // Comment or remove the next line if you are not using a
            // configuration set
            // .withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }

    //TODO: remove this after testing
    public static void main(String[] args) {
        SNSEvent.SNS sns = new SNSEvent.SNS();
        sns.setMessage("this is a message");

        SNSEvent.SNSRecord snsRecord = new SNSEvent.SNSRecord();
        snsRecord.setSns(sns);

        List<SNSEvent.SNSRecord> snsRecordList = new ArrayList<>();
        snsRecordList.add(snsRecord);

        SNSEvent request = new SNSEvent();
        request.setRecords(snsRecordList);

        String text = "--> request=" + request + "\n"
                + "--> request.getRecords()=" + request.getRecords() + "\n"
                + "--> request.getRecords().get(0)=" + request.getRecords().get(0) + "\n"
                + "--> request.getRecords().get(0).getSNS()=" + request.getRecords().get(0).getSNS() + "\n"
                + "--> request.getRecords().get(0).getSNS().getMessage()=" + request.getRecords().get(0).getSNS().getMessage();

        System.out.println(text);
    }
}
