package com.kalapala.A1.A1docker1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ListenerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> listener(FileRequestDTO fileRequestDTO) throws IOException {
        String url = "http://docker-2:6001/listen";

    HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<FileRequestDTO> requestEntity = new HttpEntity<>(fileRequestDTO, headers);

        return restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
    );
}

}
