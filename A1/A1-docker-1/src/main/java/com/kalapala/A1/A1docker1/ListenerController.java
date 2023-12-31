package com.kalapala.A1.A1docker1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

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
}
