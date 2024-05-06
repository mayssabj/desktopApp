package edu.esprit.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryUtil {

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dx5cteclw",
            "api_key", "489998819632647",
            "api_secret", "b_TAcmWLYB6jDor9fK9KZ3KalXQ"));

    /**
     * Uploads an image file to Cloudinary and returns the URL of the uploaded image.
     * @param file The image file to upload.
     * @return The URL of the uploaded image, or null if upload fails.
     */
    public static String uploadImage(File file) {
        if (file != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                return (String) uploadResult.get("url");
            } catch (IOException e) {
                System.err.println("Error uploading file to Cloudinary: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
