package com.kalapala.A1.A1docker1;

import org.springframework.beans.factory.annotation.Autowired;
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
        if(fileRequestDTO.getFile() == null){
            return ResponseEntity.ok("Error");
        }
        String filePath = "/app/file.txt";
        File file = new File(filePath);
        ResponseEntity<String> listener = listenerService.listener(fileRequestDTO);
        if(file.exists()){
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return ResponseEntity.ok("true :"+ fileContent);
        }
        else{
            return ResponseEntity.ok("false");
        }
    }
}
