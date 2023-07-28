package com.termAssignment.backend;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentReportService {

    @Autowired
    LambdaService lambdaService;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.sessionToken}")
    private String awsSessionToken;

    @Autowired
    ObjectMapper objectMapper;
    public AmazonSQS sqsClient() {

        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                awsAccessKey,
                awsSecretKey,
                awsSessionToken
        );
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        return sqs;
    }
    public void incidentReporting() throws JsonProcessingException {
        AmazonSQS sqsClient = sqsClient();
        List<Message> messages = pollMessagesFromSQS(sqsClient);
        for(Message message : messages){
            IncidentReportMessage incidentReportMessage = objectMapper.readValue(message.getBody(), IncidentReportMessage.class);

            if(incidentReportMessage.getType().equals("Man Overboard") ){
                deleteMessageFromSQS(message.getReceiptHandle(), sqsClient);

            }
            else if(incidentReportMessage.getType().equals("Oil Spill")){
                deleteMessageFromSQS(message.getReceiptHandle(), sqsClient);

            }
            else if(incidentReportMessage.getType().equals("Hurricane") ){
                lambdaService.triggerHurricaneLambda(incidentReportMessage);
                deleteMessageFromSQS(message.getReceiptHandle(), sqsClient);

            }
            else{
                deleteMessageFromSQS(message.getReceiptHandle(), sqsClient);
            }
        }
    }

    public List<Message> pollMessagesFromSQS(AmazonSQS sqsClient) {
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/209474665153/incidents";
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10).withWaitTimeSeconds(10); // Adjust the number of messages to retrieve

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
        return messages;
    }

    public void deleteMessageFromSQS(String receiptHandle, AmazonSQS sqsClient) {
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/209474665153/incidents";
        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, receiptHandle);
        sqsClient.deleteMessage(deleteMessageRequest);
    }

}
