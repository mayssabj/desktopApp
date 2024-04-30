package edu.esprit.utils;

import edu.esprit.entities.VerificationCode;

import java.util.Random;

public class VerificationCodeUtil {

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generates a 4-digit code
        return String.valueOf(code);
    }

    public static int validateVerificationCode(VerificationCode verificationCode, String inputCode) {
        if (verificationCode == null) {
            return -1; // No code available
        }
        if (verificationCode.isExpired()) {
            return 0; // Code expired
        }
        if (verificationCode.getCode().equals(inputCode)) {
            return 1; // Code valid
        }
        return 2; // Code invalid
    }
}
