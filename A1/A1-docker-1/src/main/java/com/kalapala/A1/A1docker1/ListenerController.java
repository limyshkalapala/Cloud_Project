package com.kalapala.A1.A1docker1;

import com.kalapala.A1.A1docker1.FileRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ListenerController {

    @Autowired
    ListenerService listenerService;

    @PostMapping(value ="/calculate")
    public ResponseEntity<String> getInput(@RequestBody FileRequestDTO fileRequestDTO) throws IOException {
        if(fileRequestDTO.getFile() == null){
            return ResponseEntity.ok("Error");
        }
        ResponseEntity<String> listener = listenerService.listener(fileRequestDTO);
        return ResponseEntity.ok("ok");
    }
}
