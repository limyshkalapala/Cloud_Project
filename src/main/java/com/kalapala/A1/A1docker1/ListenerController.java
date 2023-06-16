package com.kalapala.A1.A1docker1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

@RestController
public class ListenerController {

    @Autowired
    ListenerService listenerService;

    @PostMapping(value = "/calculate")
    public ResponseEntity<String> getInput(@RequestBody FileRequestDTO fileRequestDTO) throws IOException {
        if (fileRequestDTO.getFile() == null) {
            ErrorResponse errorResponse = new ErrorResponse(fileRequestDTO.getFile(), "Invalid JSON input.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertObjectToJsonString(errorResponse));
        }

        String filePath = "/app/" + fileRequestDTO.getFile();
        File file = new File(filePath);
        if (!file.exists()) {
            ErrorResponse errorResponse = new ErrorResponse(fileRequestDTO.getFile(), "File not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(convertObjectToJsonString(errorResponse));
        }

        ResponseEntity<String> responseEntity = listenerService.listener(fileRequestDTO);

        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

    private String convertObjectToJsonString(Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    class ErrorResponse {
        private String file;
        private String error;
//

        public ErrorResponse(String file, String error) {
            this.file = file;
            this.error = error;
        }

        public String getFile() {
            return file;
        }

        public String getError() {
            return error;
        }
    }









    @PostMapping(value = "/store-file")
    public ResponseEntity<String> storeFile(@RequestBody FileRequestDTO fileRequestDTO) {
        if (fileRequestDTO.getFile() == null) {
            ErrorResponseStoreFile errorResponseStoreFile = new ErrorResponseStoreFile(null, "Invalid JSON input.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertObjectToJsonStringStoreFile(errorResponseStoreFile));
        }

        String filePath = "/app/" + fileRequestDTO.getFile();
        File file = new File(filePath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileRequestDTO.getData());
            fileWriter.close();
        } catch (IOException e) {
            ErrorResponseStoreFile errorResponseStoreFile = new ErrorResponseStoreFile(fileRequestDTO.getFile(), "Error while storing the file to the storage.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(convertObjectToJsonStringStoreFile(errorResponseStoreFile));
        }

        SuccessResponseStoreFile successResponseStoreFile = new SuccessResponseStoreFile(fileRequestDTO.getFile(), "Success.");
        return ResponseEntity.ok(convertObjectToJsonStringStoreFile(successResponseStoreFile));
    }

    private String convertObjectToJsonStringStoreFile(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Handle the exception or return a default error response
            return "{\"error\": \"Failed to convert object to JSON.\"}";
        }
    }

    class ErrorResponseStoreFile {
        private String file;
        private String error;

        public ErrorResponseStoreFile(String file, String error) {
            this.file = file;
            this.error = error;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    class SuccessResponseStoreFile {
        private String file;
        private String message;

        public SuccessResponseStoreFile(String file, String message) {
            this.file = file;
            this.message = message;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }






}



