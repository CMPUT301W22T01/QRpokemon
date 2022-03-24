package com.qrpokemon.qrpokemon.activities.qrscanned;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QrScannedActivity extends AppCompatActivity {
    // create variable for capture and save image
    private ImageView photoImage;
    private Button takePhoto;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap photoBitmap;
    private String codeContent;
    private Boolean savePhoto, saveLocation;
    final private String TAG = "TestCamera";
    public static final int CAMERA_ACTION_CODE = 100;
    private QrScannedController qrScannedController = QrScannedController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);

        // binding variables with layout
        photoImage = findViewById(R.id.code_not_found_imageView);
        takePhoto = findViewById(R.id.scan_Button_no_code_found);

        FloatingActionButton backButton = findViewById(R.id.backButton_no_code_found);
        TextView title = findViewById(R.id.no_code_found_textView);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch photoSave = findViewById(R.id.qr_switch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch locationSave = findViewById(R.id.location_switch);
//      Button scanButton = findViewById(R.id.scan_Button_no_code_found);


        // two switch buttons
        photoSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (photoSave.isChecked()){
                    Toast.makeText(QrScannedActivity.this, "QR code will be saved", Toast.LENGTH_SHORT).show();
                    savePhoto = true;
                } else {
                    savePhoto = false;
                    Toast.makeText(QrScannedActivity.this, "QR code won't be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (locationSave.isChecked()){
                    saveLocation = true;
                    Toast.makeText(QrScannedActivity.this, "Location will be saved", Toast.LENGTH_SHORT).show();
                } else {
                    saveLocation = false;
                    Toast.makeText(QrScannedActivity.this, "Location won't be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    photoBitmap = (Bitmap) bundle.get("data");
                    photoImage.setImageBitmap(photoBitmap);

                    // identify if its a QR code and get the bitmap for the picture
                    codeContent = qrScannedController.doInBackground(photoBitmap);
//                    Toast.makeText(QrScannedActivity.this, codeContent, Toast.LENGTH_LONG).show();
                    MessageDigest messageDigest;
                    String hash = "";
                    try {
                        messageDigest = MessageDigest.getInstance("SHA-256");
                        messageDigest.update(codeContent.getBytes("UTF-8"));

                        // Get the hex hash string
                        hash = qrScannedController.byte2Hex(messageDigest.digest());
                        TextView qrHash = findViewById(R.id.qr_result);
                        qrHash.setText("Score: " + String.valueOf(qrScannedController.scoreCalculator(hash)));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                    } catch (Exception e){ // if a qr isn't found
                        e.printStackTrace();
                        Toast.makeText(QrScannedActivity.this, "No QR found!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

        // ask permission
        qrScannedController.checkPermission(this);

        //Pop camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(intent);

    }
}

