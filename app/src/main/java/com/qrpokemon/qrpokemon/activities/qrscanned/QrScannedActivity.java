package com.qrpokemon.qrpokemon.activities.qrscanned;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.MainActivity;
import com.qrpokemon.qrpokemon.MapController;
import com.qrpokemon.qrpokemon.QrCodeController;
import com.qrpokemon.qrpokemon.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QrScannedActivity extends AppCompatActivity {

    // init variable
    private ImageView photoImage;
    private Button confirmButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap photoBitmap;
    private String codeContent;
    private String hash = "";
    private Boolean savePhoto = false, saveLocation = false;
    private Location location;
    final private String TAG = "TestCamera";
    public static final int CAMERA_ACTION_CODE = 100;
    private QrScannedController qrScannedController = QrScannedController.getInstance();
    private QrCodeController qrCodeController = QrCodeController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);

        // setup for getting location:
        MapController mapController  = MapController.getInstance();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // get location in mapController
        mapController.run(this, null, locationManager,  fusedLocationProviderClient);

        // binding variables with layout
        photoImage = findViewById(R.id.code_not_found_imageView);
        confirmButton = findViewById(R.id.scan_Button_no_code_found);
        FloatingActionButton backButton = findViewById(R.id.backButton_no_code_found);
        TextView title = findViewById(R.id.no_code_found_textView);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch photoSave = findViewById(R.id.qr_switch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch locationSave = findViewById(R.id.location_switch);

        // set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrScannedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // two switch buttons let user choose either to save image and location or not
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
                    location = mapController.returnLocation();
                    Log.e("QrScannedActivity: ", String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                    Toast.makeText(QrScannedActivity.this, "Location will be saved", Toast.LENGTH_SHORT).show();
                    Toast.makeText(QrScannedActivity.this, String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // ask permission
            qrScannedController.checkPermission(this);
        }
        else {
            // Pop camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(intent);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String currentLocation = null;
                    Bitmap bitmap = null;
                    if (saveLocation) {
                        //if user chooses to save location
                        currentLocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                        Toast.makeText(QrScannedActivity.this, "LOCATION SAVED", Toast.LENGTH_SHORT).show();
                    }
                    if (savePhoto){
                        //if user chooses to save of QR code
                        bitmap = photoBitmap;
                    }
                    qrScannedController.saveQrCode(QrScannedActivity.this, hash, qrScannedController.scoreCalculator(hash), currentLocation, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("QrScannedActivity: ",e.toString());
                }

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_ACTION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Pop camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(intent);
                    break;
                }

        }
    }
}

