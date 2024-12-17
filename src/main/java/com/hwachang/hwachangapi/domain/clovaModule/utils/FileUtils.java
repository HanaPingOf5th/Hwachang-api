package com.hwachang.hwachangapi.domain.clovaModule.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtils {

    public static InputStream downloadFileAsStream(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to download file. HTTP Code: " + connection.getResponseCode());
        }

        return connection.getInputStream();
    }
}