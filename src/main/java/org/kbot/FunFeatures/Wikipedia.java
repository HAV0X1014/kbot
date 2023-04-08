package org.kbot.FunFeatures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Wikipedia {
    private static HttpURLConnection conn;
    public static String randomArticle() {

        String line;
        StringBuffer responseContent = new StringBuffer();
        BufferedReader reader;

        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&list=random&utf8=1&formatversion=2&rnnamespace=0");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int status = conn.getResponseCode();

            if (status > 299) {
                return "Failed to get an article";
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (responseContent.toString() != null) {
                String result = responseContent.toString();
                String[] things = result.split("\"title\":\"");
                result = things[1];
                result = result.replace("\"}]}}", "");
                result = "https://en.wikipedia.org/wiki/" + result;
                return result.replace(" ", "_");
            } else {
                return "Failed to get an article";
            }
        }
    }
}
