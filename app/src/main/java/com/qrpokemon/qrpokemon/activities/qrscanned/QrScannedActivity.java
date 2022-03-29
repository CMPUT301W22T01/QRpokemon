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
import android.util.Base64;
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
import com.google.zxing.NotFoundException;
import com.qrpokemon.qrpokemon.LocationController;
import com.qrpokemon.qrpokemon.MainActivity;
import com.qrpokemon.qrpokemon.models.PlayerController;
import com.qrpokemon.qrpokemon.R;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class QrScannedActivity extends AppCompatActivity {

    // init variable
    private ImageView photoImage;
    private Button confirmButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap photoBitmap;
    private String codeContent, hash = "", cityName = null;
    private Boolean savePhoto = false, saveLocation = false;
    private Location location;
    public static final int CAMERA_ACTION_CODE = 100;
    private QrScannedController qrScannedController = QrScannedController.getInstance();

    /**
     * open camera collaborate with player controller and map controller
     * to get current player info and called qr scanned controller to save data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);

        // setup for getting location:
        LocationController locationController  = LocationController.getInstance();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // get location in locationController
        locationController.run(this, null, locationManager,  fusedLocationProviderClient);

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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // identify if its a QR code and get the bitmap for the picture
                    Bundle bundle = result.getData().getExtras();
                    photoBitmap = (Bitmap) bundle.get("data");
                    photoImage.setImageBitmap(photoBitmap);

                    try {
                        codeContent = qrScannedController.analyzeImage(photoBitmap);
                        MessageDigest messageDigest;
                        messageDigest = MessageDigest.getInstance("SHA-256");
                        messageDigest.update(codeContent.getBytes("UTF-8"));
                        // Get the hex hash string
                        hash = qrScannedController.byte2Hex(messageDigest.digest());
                        TextView qrHash = findViewById(R.id.qr_result);
                        qrHash.setText("Score: " + String.valueOf(qrScannedController.scoreCalculator(hash)));
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(QrScannedActivity.this, "QRcode not found!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                    try {
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
                                    cityName = locationController.getCity(QrScannedActivity.this);
                                    location = locationController.returnLocation();
                                    Log.e("QrScannedActivity: ", String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                                    Toast.makeText(QrScannedActivity.this, "Location will be saved " + String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
                                } else {
                                    saveLocation = false;
                                    Toast.makeText(QrScannedActivity.this, "Location won't be saved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        confirmButton.setOnClickListener(new View.OnClickListener() { // save QR code after user made her/his decision. Now saving code via QrCodeController, PlayerController and DatabaseController:
                            @Override
                            public void onClick(View view) {

                                try { // save user's information
                                    PlayerController playerController = PlayerController.getInstance();

                                    // if th Qr code already scanned by player
                                    if((((ArrayList<String>) (playerController.getPlayer(null, null,null,null).get("qrInventory"))).contains(hash))){
                                        Toast.makeText(QrScannedActivity.this, "Already have this QR code", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    // if this QR code is the first time scanned by player
                                    else {
                                        HashMap player = (HashMap) playerController.getPlayer(null, null, null, null);
                                        ArrayList<String> qrInventory = (ArrayList<String>) player.get("qrInventory");

                                        // update player's total score and counts
                                        int qrCount = qrInventory.size() + 1;
                                        int qrTotal = (int) player.get("totalScore") + qrScannedController.scoreCalculator(hash);

                                        // update highest scores
                                        Integer highestUnique = null;
                                        if ( (Integer) player.get("highestUnique") == null || qrScannedController.scoreCalculator(hash) > (int) player.get("highestUnique")) {
                                            highestUnique = qrScannedController.scoreCalculator(hash);
                                        }
                                        qrInventory.add(hash);
                                        playerController.savePlayerData(qrCount, qrTotal, qrInventory, null, highestUnique, null, false);
                                    }

                                    String currentLocation = null;
                                    Bitmap bitmap = null;
                                    String bitMapString = null;
                                    if (saveLocation) {
                                        //if player chooses to save location
                                        currentLocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
//                                        Toast.makeText(QrScannedActivity.this, "LOCATION SAVED", Toast.LENGTH_SHORT).show();
//                                        Log.e("QrScannedActivity: ",currentLocation);
                                    }

                                    if (savePhoto){
                                        //if user chooses to save of QR code
                                        bitmap = photoBitmap;

                                        // convert bitmap to string
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                                        byte[] b = byteArrayOutputStream.toByteArray();
                                        bitMapString = Base64.encodeToString(b, Base64.DEFAULT);
//                                        Toast.makeText(QrScannedActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();
                                    }
                                    //save qrcode information
                                    qrScannedController.saveQrCode(QrScannedActivity.this, hash, qrScannedController.scoreCalculator(hash), currentLocation, bitMapString);

                                    // save location information
                                    locationController.saveLocation(cityName, String.valueOf(location.getLatitude()) +"," + String.valueOf(location.getLongitude()), QrScannedActivity.this, hash);
                                    Toast.makeText(QrScannedActivity.this, "QR saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    Log.e("QrScannedActivity: ",e.toString());
                                }
                            }
                        });

                    } catch (Exception e){
                        // if a qr isn't found
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
    }

    /**
     * Ask permission for device to open camera
     * @param requestCode permission code
     * @param permissions the array of permission app asked
     * @param grantResults the result of permission asked
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("QrScannedActivity: ", "Request now has the result");
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

