package com.kalapala.A1.A1docker2;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CalculationService {

    public ResponseEntity<String> calculateProduct(String fileName, String product){
        try {
            String filePath = "/app/" + fileName;
            File file = new File(filePath);

//           https://www.baeldung.com/java-json-escaping
//           Took code snippets from here for the response string, note that I did not use Jackson Object mapper,
//           because it was a custom message


            if (!file.exists()) {
                return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"error\": \"File not found.\"}");
            }

            // Read and parse the CSV file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (!isFileInCSVFormat(lines) ) {

                return ResponseEntity.ok("{\"file "+lines+"\": \"" + fileName + "\", \"error\": \"Input file not in CSV format.\"}");
            }

            int sum = calculateSum(lines, product);

            return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"sum\": \"" + sum + "\"}");
        } catch (IOException e) {
            return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"error\": \"Input file not in CSV format.\"}");
        }

    }
    private int calculateSum(List<String> lines, String product) {
        int sum = 0;
//https://www.baeldung.com/java-check-string-number code snippets from here to check if the string parts are integer or not.
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].trim().equals(product)) {
                sum += Integer.parseInt(parts[1].trim());
            }
        }

        return sum;
    }

    private boolean isFileInCSVFormat(List<String> lines) {
        if (lines.isEmpty()) {
            return false;
        }
        String header = lines.get(0);
        if (!header.trim().equalsIgnoreCase("product,amount")) {
            return false;
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                return false;
            }

            try {
                Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

}
