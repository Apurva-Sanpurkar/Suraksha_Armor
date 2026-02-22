package com.example.suraksha;

import android.graphics.Bitmap;
import android.graphics.Color;

public class SteganoEngine {
    /**
     * Encodes a secret message into the blue channel of the bitmap pixels.
     * This is a standard Least Significant Bit (LSB) implementation.
     */
    public static Bitmap encodeMessage(Bitmap source, String message) {
        Bitmap result = source.copy(Bitmap.Config.ARGB_8888, true);
        char[] chars = (message + "\0").toCharArray(); // Null terminator to find end of string
        int charIndex = 0;
        int bitIndex = 0;

        for (int y = 0; y < result.getHeight() && charIndex < chars.length; y++) {
            for (int x = 0; x < result.getWidth() && charIndex < chars.length; x++) {
                int pixel = result.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                
                // Get the current bit of the current character
                int bit = (chars[charIndex] >> bitIndex) & 1;
                
                // Replace the LSB of the blue channel
                blue = (blue & 0xFE) | bit;
                
                result.setPixel(x, y, Color.argb(alpha, red, green, blue));
                
                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    charIndex++;
                }
            }
        }
        return result;
    }
}