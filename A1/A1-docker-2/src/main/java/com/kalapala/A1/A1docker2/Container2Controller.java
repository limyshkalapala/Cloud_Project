package com.kalapala.A1.A1docker2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Container2Controller {
    @PostMapping(value = "/listen")
    public ResponseEntity<String> calculate(@RequestBody FileRequestDTO fileRequestDTO){

        return ResponseEntity.ok("");
    }
}
