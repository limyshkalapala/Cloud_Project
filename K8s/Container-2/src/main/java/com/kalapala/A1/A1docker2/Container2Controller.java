package com.kalapala.A1.A1docker2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Container2Controller {

    @Autowired
    CalculationService calculationService;
    @PostMapping(value = "/listen")
    public ResponseEntity<String> calculate(@RequestBody FileRequestDTO fileRequestDTO){
        String fileName = fileRequestDTO.getFile();
        String product = fileRequestDTO.getProduct();
        return calculationService.calculateProduct(fileName,product);
    }
}
