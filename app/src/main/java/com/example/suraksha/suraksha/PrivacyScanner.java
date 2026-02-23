package com.example.suraksha;

import android.content.Context;
import android.graphics.Bitmap;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;
import org.tensorflow.lite.support.image.TensorImage;
import java.util.List;

public class PrivacyScanner {
    private ObjectDetector detector;

    public PrivacyScanner(Context context) {
        // Step 1: Initialize the detector with a pre-trained model
        // We'll use a model capable of detecting common documents/cards
        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(3)
                .setScoreThreshold(0.5f) // 50% confidence threshold
                .build();
        try {
            detector = ObjectDetector.createFromFileAndOptions(context, "scanner.tflite", options);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Scans the image to see if it's "Sensitive" (e.g., a Card).
     */
    public boolean isSensitiveMedia(Bitmap bitmap) {
        if (detector == null) return false;
        
        TensorImage image = TensorImage.fromBitmap(bitmap);
        List<Detection> results = detector.detect(image);
        
        for (Detection detection : results) {
            String label = detection.getCategories().get(0).getLabel();
            // If we detect a card or document, return true
            if (label.contains("card") || label.contains("document") || label.contains("identity")) {
                return true;
            }
        }
        return false;
    }
}