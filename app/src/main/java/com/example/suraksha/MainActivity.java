package com.example.suraksha; 
 
import android.content.Intent; 
import android.graphics.Bitmap; 
import android.graphics.Color; 
import android.net.Uri; 
import android.os.Bundle; 
import android.provider.MediaStore; 
import android.widget.Button; 
import android.widget.ImageView; 
import android.widget.Toast; 
import androidx.appcompat.app.AppCompatActivity; 
import java.io.IOException; 
 
public class MainActivity extends AppCompatActivity { 
    private static final int PICK_IMAGE = 100; 
    private ImageView previewImage; 
    private Bitmap selectedBitmap; 
 
    @Override protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main); 
        previewImage = findViewById(R.id.previewImage); 
        findViewById(R.id.btnPick).setOnClickListener(v -> { 
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); 
            startActivityForResult(gallery, PICK_IMAGE); 
        }); 
        findViewById(R.id.btnArmSend).setOnClickListener(v -> { 
            if (selectedBitmap != null) armAndShare(); 
            else Toast.makeText(this, "Select an image first", Toast.LENGTH_SHORT).show(); 
        }); 
    } 
 
    @Override protected void onActivityResult(int req, int res, Intent data) { 
        super.onActivityResult(req, res, data); 
        if (res == RESULT_OK && req == PICK_IMAGE) { 
            try { selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()); 
                  previewImage.setImageBitmap(selectedBitmap); 
            } catch (IOException e) { e.printStackTrace(); } 
        } 
    } 
 
    private void armAndShare() { 
        Bitmap mutable = selectedBitmap.copy(Bitmap.Config.ARGB_8888, true); 
        mutable.setPixel(0, 0, Color.rgb(255, 0, 0)); // The 'Magic' Red Trigger Pixel 
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutable, "Armed", null); 
        Intent share = new Intent(Intent.ACTION_SEND); 
        share.setType("image/jpeg"); share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path)); 
        share.setPackage("com.whatsapp"); startActivity(share); 
    } 
} 
