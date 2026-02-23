package com.example.suraksha;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class CryptoEngine {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // Android's closest standard to XTS behavior for files

    public static byte[] encrypt(byte[] data, String key) throws Exception {
        // Ensure key is exactly 32 bytes for AES-256
        byte[] keyBytes = new byte[32];
        System.arraycopy(key.getBytes(), 0, keyBytes, 0, Math.min(key.length(), 32));
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv); // Generate a fresh IV for every file
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] encryptedData = cipher.doFinal(data);

        // Prepend IV to the data so the receiver can use it
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);
        
        return combined;
    }
}