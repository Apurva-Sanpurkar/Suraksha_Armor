package com.example.suraksha;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileProtector {
    // Magic bytes that identify the file as a Suraksha container
    private static final String MAGIC_HEADER = "SURAKSHA_V1";

    /**
     * Suraksha Feature: Custom Binary Packaging (.suraksha).
     * Wraps media in a format that appears as "Unknown Data" to other apps.
     */
    public static File saveAsSuraksha(Bitmap bitmap, File directory, String fileName) {
        File file = new File(directory, fileName + ".suraksha");
        try (FileOutputStream out = new FileOutputStream(file)) {
            // 1. Write Header: Identifying this as a Suraksha file
            out.write(MAGIC_HEADER.getBytes(StandardCharsets.UTF_8));
            
            // 2. Write Body: Encoded Bitmap data
            // We use PNG compression to ensure LSB bits remain perfect
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}