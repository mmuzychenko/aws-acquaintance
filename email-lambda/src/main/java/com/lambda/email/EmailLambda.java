package com.lambda.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.util.logging.Logger;

public class EmailLambda implements RequestHandler<SNSEvent, Object> {


    private static final String EMAIL_SUBJECT = "From AWS Lambda" ;
    private static final String SNS_SUBJECT_UPLOAD_CONFIRMATION = "SNS image-service. Image uploaded" ;
    private static final String SNS_SUBJECT_DELETE_CONFIRMATION = "SNS image-service. Image deleted" ;

    private static final Logger LOGGER = Logger.getLogger(EmailLambda.class.getName());


    @Override
    public Boolean handleRequest(SNSEvent request, Context context) {

        for (SNSEvent.SNSRecord snsRecord : request.getRecords()) {
            String subject = snsRecord.getSNS().getSubject();
            String text;
            if (SNS_SUBJECT_UPLOAD_CONFIRMATION.equals(subject)) {
                text = "FILE UPLOADED !";
            } else if (SNS_SUBJECT_DELETE_CONFIRMATION.equals(subject)) {
                text = "FILE DELETED !";
            } else {
                throw new IllegalArgumentException("Unsupported subject of SNS notification!");
            }
            sendEmail(snsRecord.getSNS().getMessage(), EMAIL_SUBJECT, text);
        }

        return Boolean.TRUE;
    }


    public static void sendEmail(String emailTo, String subject, String text) {

        String emailFrom = System.getenv("EMAIL_SENDER");

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withRegion(Regions.EU_NORTH_1).build();

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(emailTo))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(text)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource(emailFrom);

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

}
