package org.kbot.FileHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFull {
    public static String read(String filePath) {
        String fullText = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fullText += line;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fullText;
    }
}
