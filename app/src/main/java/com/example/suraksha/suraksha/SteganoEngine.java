package com.example.suraksha;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.Random;

public class SteganoEngine {

    /**
     * Suraksha Feature: Randomized LSB Encoding.
     * Scatters bits across the image using a seed to ensure data 
     * cannot be retrieved without the correct ID.
     */
    public static Bitmap encodeWithSeed(Bitmap source, String message, long seed) {
        Bitmap result = source.copy(Bitmap.Config.ARGB_8888, true);
        byte[] data = (message + "\0").getBytes(); 
        
        int width = result.getWidth();
        int height = result.getHeight();
        
        // Seed ensures the "random" pixels are the same for sender and receiver.
        Random random = new Random(seed);
        
        int bitIndex = 0;
        int byteIndex = 0;

        for (int i = 0; i < data.length * 8; i++) {
            // Pick a pseudo-random pixel based on the seed.
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            int pixel = result.getPixel(x, y);
            int bit = (data[byteIndex] >> bitIndex) & 1;
            
            // Inject into the Least Significant Bit of the Blue channel.
            int blue = (Color.blue(pixel) & 0xFE) | bit;
            
            result.setPixel(x, y, Color.argb(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), blue));
            
            bitIndex++;
            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
        }
        return result;
    }
}