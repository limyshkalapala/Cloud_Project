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

            if (!file.exists()) {
                return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"error\": \"File not found.\"}");
            }

            // Read and parse the CSV file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (!isFileInCSVFormat(lines)) {
                return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"error\": \"Input file not in CSV format.\"}");
            }

            int sum = calculateSum(lines, product);

            return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"sum\": " + sum + "}");
        } catch (IOException e) {
            return ResponseEntity.ok("{\"file\": \"" + fileName + "\", \"error\": \"Input file not in CSV format.\"}");
        }

    }
    private int calculateSum(List<String> lines, String product) {
        int sum = 0;

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

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length != 2) {
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
