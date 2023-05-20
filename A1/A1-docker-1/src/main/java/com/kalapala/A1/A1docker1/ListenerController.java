package com.kalapala.A1.A1docker1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@RestController
public class ListenerController {

    @Autowired
    ListenerService listenerService;

    @PostMapping(value ="/calculate")
    public ResponseEntity<String> getInput(@RequestBody FileRequestDTO fileRequestDTO) throws IOException {
        if (fileRequestDTO.getFile() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON input.");
        }
        //file exists or not
        String filePath = "/app/" + fileRequestDTO.getFile();
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        // Invoke the listener service in the second container
        ResponseEntity<String> responseEntity = listenerService.listener(fileRequestDTO);

        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }
}
