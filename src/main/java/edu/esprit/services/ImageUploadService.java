package edu.esprit.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ImageUploadService {
    private Cloudinary cloudinary;

    public ImageUploadService() {
        // Initialize Cloudinary object with your credentials
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwrjro3r8",
                "api_key", "636767622855557",
                "api_secret", "IKJpD6A0bu3ypZNxrG1W1OgVQ3o",
                "secure", true));
    }

    public CompletableFuture<String> uploadImageAsync(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Upload the file to Cloudinary and retrieve the URL
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                return uploadResult.get("url").toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        });
    }
}