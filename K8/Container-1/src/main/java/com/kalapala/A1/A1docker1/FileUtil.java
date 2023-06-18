package com.kalapala.A1.A1docker1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void writeStringToFile(String content, String filePath) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content);
//            testing CI/CD
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}