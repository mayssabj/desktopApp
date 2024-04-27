package edu.esprit.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class TranslationService {

    private static final String API_URL = "https://api.mymemory.translated.net/get";

    public static String translateText(String text, String fromLang, String toLang) {
        try {
            // Ensure all parts of the URL are correctly encoded
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String encodedLangPair = URLEncoder.encode(fromLang + "|" + toLang, StandardCharsets.UTF_8.toString());
            String requestUrl = API_URL + "?q=" + encodedText + "&langpair=" + encodedLangPair;

            URI uri = URI.create(requestUrl);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            String translatedText = jsonResponse.getJSONObject("responseData").getString("translatedText");

            System.out.println("Original: " + text);
            System.out.println("Translated: " + translatedText);
            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
            return text; // Return the original text if there's an error
        }
    }
}
