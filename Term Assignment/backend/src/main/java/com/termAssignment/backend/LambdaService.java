package com.termAssignment.backend;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;


@Service
public class LambdaService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.sessionToken}")
    private String awsSessionToken;






    @Autowired
    RestTemplate restTemplate;

    public AmazonS3 s3Client() {

        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                awsAccessKey,
                awsSecretKey,
                awsSessionToken
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        return s3client;
    }

    public String triggerManOverboardLambda(IncidentReportMessage incidentReport) {
        return triggerLambdaFunction("processManOverboard", incidentReport);
    }

    // Method to trigger Hurricane Lambda
    public String triggerHurricaneLambda(IncidentReportMessage incidentReport) {
        String imageDataBase64 = incidentReport.getImageDataBase64();
        String[] parts = imageDataBase64.split(",");
        String imageStringSplit = parts[1];
        if (imageStringSplit != null && !imageStringSplit.isEmpty()) {
            try {
                // Decode the base64 image data
                byte[] imageBytes = Base64.getDecoder().decode(imageStringSplit);

                // Create a unique S3 key for the image using the current timestamp
                String s3Key = "hurricane_image_" + new Date().getTime() + ".jpg";
                AmazonS3 s3client = s3Client();
                InputStream imageStream = new ByteArrayInputStream(imageBytes);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(imageBytes.length);
                metadata.setContentType("image/jpeg");
                PutObjectRequest request = new PutObjectRequest("b00934899-incident-reports", s3Key, imageStream, metadata);
                PutObjectResult result = s3client.putObject(request);

                // Get the S3 URL for the uploaded image
                String imageUrl = s3client.getUrl("b00934899-incident-reports", s3Key).toString();
                return imageUrl;
            } catch (Exception e) {
                // Handle the exception if there's an error during image upload
                e.printStackTrace();
                return null;
            }
        } else {
            // If there's no image, return null for the URL
            return null;
        }
    }

    // Method to trigger Oil Spill Lambda
    public String triggerOilSpillLambda(IncidentReportMessage incidentReport) {
        return triggerLambdaFunction("processOilSpill", incidentReport);
    }

    private String triggerLambdaFunction(String functionName, IncidentReportMessage incidentReport) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = objectMapper.writeValueAsString(incidentReport);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Replace "functionName" in the URL with the actual API Gateway resource path for your Lambda function
            String apiUrl = "URL";
            restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, requestEntity, String.class);

            return "Lambda function triggered successfully.";
        } catch (Exception e) {
            return "Failed to trigger Lambda function: " + e.getMessage();
        }
    }
}
