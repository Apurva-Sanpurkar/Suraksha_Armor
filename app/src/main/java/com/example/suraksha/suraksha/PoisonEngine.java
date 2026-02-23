package com.example.suraksha;

import android.graphics.Bitmap;
import android.graphics.Color;

public class PoisonEngine {
    /**
     * FGSM-Lite: Injects high-frequency noise.
     * Confuses AI scrapers while keeping the image clear for humans.
     */
    public static Bitmap applyAdversarialNoise(Bitmap source) {
        Bitmap poisoned = source.copy(Bitmap.Config.ARGB_8888, true);
        float epsilon = 0.015f; // Noise intensity

        for (int y = 0; y < poisoned.getHeight(); y++) {
            for (int x = 0; x < poisoned.getWidth(); x++) {
                int pixel = poisoned.getPixel(x, y);
                // Alternate sign to create high-frequency pattern
                int sign = ((x + y) % 2 == 0) ? 1 : -1;

                int r = clamp((int) (Color.red(pixel) + (sign * epsilon * 255)));
                int g = clamp((int) (Color.green(pixel) + (sign * epsilon * 255)));
                int b = clamp((int) (Color.blue(pixel) + (sign * epsilon * 255)));

                poisoned.setPixel(x, y, Color.argb(Color.alpha(pixel), r, g, b));
            }
        }
        return poisoned;
    }

    private static int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }
}