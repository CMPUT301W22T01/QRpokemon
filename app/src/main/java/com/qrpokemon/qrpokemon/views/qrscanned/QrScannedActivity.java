package com.qrpokemon.qrpokemon.views.qrscanned;

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

import com.qrpokemon.qrpokemon.MainActivity;
import com.qrpokemon.qrpokemon.R;
import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.LocationController;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.controllers.QrScannedController;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private LocationController locationController = LocationController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);

        // setup for getting location:
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
                                    Toast.makeText(QrScannedActivity.this, "Photo will be saved", Toast.LENGTH_SHORT).show();
                                    savePhoto = true;
                                } else {
                                    savePhoto = false;
                                    Toast.makeText(QrScannedActivity.this, "Photo won't be saved", Toast.LENGTH_SHORT).show();
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

                        confirmButton.setOnClickListener(new View.OnClickListener() { // save QR code after user made her/his decision. Now saving code via QrCodeController, PlayerController and DatabaseProxy:
                            @Override
                            public void onClick(View view) {

                                try { // save user's information
                                    PlayerController playerController = PlayerController.getInstance();

                                    DatabaseCallback databaseCallback = new DatabaseCallback(QrScannedActivity.this) {
                                        @Override
                                        public void run(List<Map> dataList) {
                                            if (!dataList.isEmpty()) { // if player found:
                                                if(((ArrayList<String>) dataList.get(0).get("qrInventory")).contains(hash)){// if th Qr code already scanned by player

                                                    Toast.makeText(QrScannedActivity.this, "Already have this QR code", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    // if this QR code is the first time scanned by player
                                                    HashMap player = null;
                                                    try {
                                                        player = (HashMap) dataList.get(0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    ArrayList<String> qrInventory = (ArrayList<String>) dataList.get(0).get("qrInventory");

                                                    // update player's total score and counts
                                                    int qrCount = qrInventory.size() + 1;
                                                    int qrTotal = ((Long)player.get("totalScore")).intValue()+ qrScannedController.scoreCalculator(hash);

                                                    // update highest scores
                                                    Integer highestUnique = null;
                                                    if (player.get("highestUnique") == null || qrScannedController.scoreCalculator(hash) > ((Long)player.get("highestUnique")).intValue()) {
                                                        highestUnique = qrScannedController.scoreCalculator(hash);
                                                    }
                                                    qrInventory.add(hash);
                                                    Log.e("QrScannedActivity: ","Player's qrInventory now is: " + qrInventory.toString());
                                                    try {
                                                        playerController.savePlayerData(qrCount, qrTotal, qrInventory, (HashMap) player.get("contact"), highestUnique, (String) player.get("DeviceId"), (Boolean) player.get("Owner"),false);
                                                        Toast.makeText(QrScannedActivity.this, "QR saved", Toast.LENGTH_SHORT).show();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }

                                        }
                                    };
                                    playerController.getPlayer(databaseCallback, new ArrayList<>(), (String) playerController.getPlayer(null, null,null,null).get("Identifier"), null);

                                    String currentLocation = null;
                                    Bitmap bitmap = null;
                                    String bitMapString = null;
                                    if (saveLocation) {
                                        //if player chooses to save location
                                        currentLocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                                        // save location information
                                        locationController.saveLocation(cityName, String.valueOf(location.getLatitude()) +"," + String.valueOf(location.getLongitude()), QrScannedActivity.this, hash);
                                    }

                                    if (savePhoto){
                                        //if user chooses to save of QR code
                                        bitmap = photoBitmap;

                                        // convert bitmap to string
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream); // 100 is permission code for location
                                        byte[] b = byteArrayOutputStream.toByteArray();
                                        bitMapString = Base64.encodeToString(b, Base64.DEFAULT);
//                                        Toast.makeText(QrScannedActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();
                                    }
                                    //save qrcode information
                                    qrScannedController.saveQrCode(QrScannedActivity.this, hash, qrScannedController.scoreCalculator(hash), currentLocation, bitMapString, codeContent);

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
                } else {
                    Toast.makeText(QrScannedActivity.this, "No QR found!", Toast.LENGTH_SHORT).show();
                    finish();
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("QrScannedActivity: ", "Request now has the result");
        switch (requestCode) {
            case CAMERA_ACTION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Pop camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(intent);
                    break;
                }
            case 101: // location request code
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // setup for getting location:
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                        // get location in locationController
                        locationController.run(this, null, locationManager,  fusedLocationProviderClient);

                        // ask permission
                        qrScannedController.checkPermission(this);
                    }
                }
        }
    }


}

