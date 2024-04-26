package edu.esprit.services;
import java.net.URI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;


public class TranslationService {
    public static String translateText(String text, String sourceLang, String targetLang) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://libretranslate.com/translate";
        String requestBody = String.format(
                "{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\",\"format\":\"text\"}",
                text, sourceLang, targetLang
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            return response.body();  // Parse this JSON response to get the translated text
        } catch (Exception e) {
            e.printStackTrace();
            return "Translation error";
        }
    }
}
