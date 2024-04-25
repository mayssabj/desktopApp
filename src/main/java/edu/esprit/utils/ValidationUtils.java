package edu.esprit.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static List<String> validateEmail(String email) {
        List<String> errors = new ArrayList<>();
        if (email.isEmpty()) {
            errors.add("• Email field is required.");
        } else {
            Pattern pattern = Pattern.compile("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$");
            if (!pattern.matcher(email).matches()) {
                errors.add("• Invalid email format.");
            }
        }
        return errors;
    }

    public static List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        if (password.isEmpty()) {
            errors.add("• Password field is required.");
        } else {
            if (password.length() < 8) {
                errors.add("• At least 8 characters long.");
            }
            if (!password.matches(".*[a-zA-Z]+.*")) {
                errors.add("• At least one letter.");
            }
            if (!password.matches(".*\\d+.*")) {
                errors.add("• At least one number.");
            }
            if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
                errors.add("• At least one special character.");
            }
        }
        return errors;
    }

    public static List<String> validateConfirmPassword(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        if (!confirmPassword.equals(password)) {
            errors.add("• Passwords do not match.");
        }
        return errors;
    }

    public static List<String> validatePhone(String phone) {
        List<String> errors = new ArrayList<>();
        if (!phone.isEmpty() && !Pattern.matches("(\\+216)\\d{8}", phone)) {
            errors.add("• Must be +216 xx xxx xxx");
        }
        return errors;
    }

    public static List<String> validateAddress(String text) {
        return new ArrayList<String>();
    }
}
