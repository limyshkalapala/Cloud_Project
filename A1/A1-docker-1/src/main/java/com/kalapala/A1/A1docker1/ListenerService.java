package com.kalapala.A1.A1docker1;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ListenerService {
    public ResponseEntity<String> listener(FileRequestDTO fileRequestDTO) throws IOException {
        String url = "http://localhost:6001/listen";
        String requestBody = fileRequestDTO.toString();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Send request
        con.setDoOutput(true);
        con.getOutputStream().write(requestBody.getBytes("UTF-8"));

        // Get response
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return ResponseEntity.ok(response.toString());
    }


}
