package edu.esprit.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.*;

public class OpenAIService {
    private final OkHttpClient client = new OkHttpClient();

    public String getChatGPTResponse(String prompt) throws IOException {
        // The name of the environment variable should be passed, not the API key itself
        String apiKey = System.getenv("OPENAI_API_KEY");  // Ensure this environment variable is set in your system
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key is not set in environment variables.");
        }

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        String json = "{\"model\": \"gpt-3.5-turbo\", \"prompt\": \"" + prompt + "\", \"max_tokens\": 150}";
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Assuming the response JSON has a structure where the text response is in a "choices" array
            JsonObject jsonResponse = JsonParser.parseString(response.body().string()).getAsJsonObject();
            return jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject().get("text").getAsString();
        }
    }
}
