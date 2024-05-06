package edu.esprit.Api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfanityFilter {
    private Set<String> badWords;

    public ProfanityFilter() {
        loadBadWords();
    }

    private void loadBadWords() {
        try (InputStream in = getClass().getResourceAsStream("/badwords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            badWords = reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            badWords = new HashSet<>();
        }
    }

    public boolean containsProfanity(String content) {
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (badWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String filterContent(String content) {
        String[] words = content.split("\\s+");
        return Arrays.stream(words)
                .map(word -> badWords.contains(word.toLowerCase()) ? "*".repeat(word.length()) : word)
                .collect(Collectors.joining(" "));
    }
}
