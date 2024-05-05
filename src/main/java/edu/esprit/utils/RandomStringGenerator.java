package edu.esprit.utils;

import java.util.Random;

public class RandomStringGenerator {

    // Define the pattern
    private static final String PATTERN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DELIMITER = "-";
    private static final int GROUP_LENGTHS[] = {6, 4, 7}; // Lengths of each group

    public static String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Create random object
        Random random = new Random();

        // Generate random characters for each group
        for (int groupLength : GROUP_LENGTHS) {
            for (int i = 0; i < groupLength; i++) {
                int randomIndex = random.nextInt(PATTERN.length());
                stringBuilder.append(PATTERN.charAt(randomIndex));
            }
            // Add delimiter except for the last group
            if (groupLength != GROUP_LENGTHS[GROUP_LENGTHS.length - 1]) {
                stringBuilder.append(DELIMITER);
            }
        }

        return stringBuilder.toString();
    }

}
