package com.example.suraksha;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileProtector {
    private static final String MAGIC_HEADER = "SURAKSHA_V1";

    public static File saveAsSuraksha(Bitmap bitmap, File directory, String fileName, boolean isSensitive) {
        File file = new File(directory, fileName + ".suraksha");
        try (FileOutputStream out = new FileOutputStream(file)) {
            // 1. Write Header
            out.write(MAGIC_HEADER.getBytes(StandardCharsets.UTF_8));
            
            // 2. Write Security Flag: 1 for Sensitive (Card UI), 0 for Normal
            out.write(isSensitive ? 1 : 0);
            
            // 3. Write Body
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}