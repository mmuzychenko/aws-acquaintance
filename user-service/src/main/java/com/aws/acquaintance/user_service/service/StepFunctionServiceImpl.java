package com.aws.acquaintance.user_service.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StepFunctionServiceImpl implements StepFunctionService {

    private static final String ARN_AWS_STATE_MACHINE = "arn:aws:states:eu-west-3:026806867949:execution:MyStateMachine-00awpq02g:b7a06f09-2ae8-4682-8502-032dfce04473";

    private final AWSStepFunctions awsStepFunctionsClient;

    @Autowired
    public StepFunctionServiceImpl(AWSStepFunctions awsStepFunctionsClient) {
        this.awsStepFunctionsClient = awsStepFunctionsClient;
    }


    @Override
    public void startStepFunction() {
        log.info("---> StepFunctionServiceImpl: startStepFunction: step function started...");

        //TODO: this is mock - move it to Step Function
        //simulateProcessing(10);

        //TODO: this is mock - move it to Step Function
        //log.info("---> StepFunctionServiceImpl: startStepFunction: initialize file deletion...");



        ClientConfiguration clientConfiguration = new ClientConfiguration();
//        clientConfiguration.setClientExecutionTimeout(2*60*1000);//2 mins
        clientConfiguration.setClientExecutionTimeout(20*1000);//20 seconds


        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();

        log.info("---> StepFunctionServiceImpl: startStepFunction: startExecutionRequest=" + startExecutionRequest);

        //TODO: create model class for this
        JSONObject inputFile = new JSONObject();
        inputFile.put("fileName", "cmd_commands.txt");

        JSONObject sfnInput = new JSONObject();
        sfnInput.put("file", inputFile);

        log.info("---> StepFunctionServiceImpl: startStepFunction: sfnInput=" + sfnInput);

        StartExecutionRequest request = new StartExecutionRequest()
                .withStateMachineArn(ARN_AWS_STATE_MACHINE)
                .withInput(sfnInput.toString());

        log.info("---> StepFunctionServiceImpl: startStepFunction: request=" + request);

        StartExecutionResult result = awsStepFunctionsClient.startExecution(request);

        log.info("---> StepFunctionServiceImpl: startStepFunction: result="+result);
    }


    private void simulateProcessing(int seconds) {
        try {
            for (int i = 0; i < seconds; i++) {
                log.info((seconds - i) + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
