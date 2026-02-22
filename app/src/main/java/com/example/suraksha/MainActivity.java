package com.example.suraksha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView previewImage;
    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // SURAKSHA FEATURE: Prevent screenshots and screen recording
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, 
                            WindowManager.LayoutParams.FLAG_SECURE);
        
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        previewImage = findViewById(R.id.previewImage);

        // Select Image Button Logic
        findViewById(R.id.btnPick).setOnClickListener(v -> openGallery());

        // Armor & Send Button Logic
        findViewById(R.id.btnArmSend).setOnClickListener(v -> {
            if (selectedBitmap != null) {
                armorAndSend();
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Load the selected image into a Bitmap
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                previewImage.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void armorAndSend() {
        // This calls the SteganoEngine to hide the security layer
        Bitmap armoredBitmap = SteganoEngine.encodeMessage(selectedBitmap, "SECURE_DATA");
        
        // For the demo, we update the preview to show the 'armored' version
        previewImage.setImageBitmap(armoredBitmap);
        Toast.makeText(this, "Image Armored! Ready to send.", Toast.LENGTH_LONG).show();
        
        // TODO: Implement Intent to share the armored image via WhatsApp/Email
    }
}