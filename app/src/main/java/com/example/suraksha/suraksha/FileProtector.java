package com.example.suraksha;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileProtector {
    private static final String MAGIC_HEADER = "SURAKSHA_V1";

    public static File saveAsSuraksha(Bitmap bitmap, File directory, String fileName, boolean isSensitive, String key) {
        File file = new File(directory, fileName + ".suraksha");
        try (FileOutputStream out = new FileOutputStream(file)) {
            // 1. Convert Bitmap to PNG bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] pngData = stream.toByteArray();

            // 2. Encrypt the data
            byte[] encryptedData = CryptoEngine.encrypt(pngData, key);

            // 3. Write Metadata Header
            out.write(MAGIC_HEADER.getBytes(StandardCharsets.UTF_8));
            out.write(isSensitive ? 1 : 0); // Sensitivity Flag
            
            // 4. Write TTL Timestamp (Long = 8 bytes)
            long currentTime = System.currentTimeMillis();
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(currentTime);
            out.write(buffer.array());

            // 5. Write Encrypted Payload
            out.write(encryptedData);
            
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}