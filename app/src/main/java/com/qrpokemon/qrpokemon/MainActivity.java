package com.qrpokemon.qrpokemon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.activities.leaderboard.LeaderboardActivity;
import com.qrpokemon.qrpokemon.activities.map.MapActivity;
import com.qrpokemon.qrpokemon.activities.map.MapController;
import com.qrpokemon.qrpokemon.activities.profile.ProfileActivity;
import com.qrpokemon.qrpokemon.activities.qrinventory.QrInventoryActivity;
import com.qrpokemon.qrpokemon.activities.qrscanned.QrScannedActivity;
import com.qrpokemon.qrpokemon.activities.search.SearchActivity;
import com.qrpokemon.qrpokemon.activities.signup.SignupActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private Button qrInventoryMainBt;
    private Button mapMainBt;
    private Button searchMainBt;
    private Button leaderboardMainBt;
    private FloatingActionButton otherProfileBt;
    private FloatingActionButton cameraMainBt;
    private ImageView profileMainIv;
    private MainMenuController mainMenuController = MainMenuController.getInstance();
    private static final int CAMERA_ACTION_CODE = 100;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * MainMenu is created here, it sends user to different activities by pressing buttons
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    mainMenuController.load(MainActivity.this); //load & display current player's username
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity: ", "No current player");
                }
            }
        });

        setContentView(R.layout.main_menu);
        Intent intent = new Intent(this, SignupActivity.class);
        getContent.launch(intent);

        qrInventoryMainBt = findViewById(R.id.QR_Inventory_Button);
        mapMainBt = findViewById(R.id.Map_Button);
        searchMainBt = findViewById(R.id.Search_Button);
        leaderboardMainBt = findViewById(R.id.Leaderboard_Button);
        cameraMainBt = findViewById(R.id.Camera_Button);
        otherProfileBt = findViewById(R.id.Other_Player_Button);

        //setup for getting location:
        MapController mapController = MapController.getInstance();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get location in mapController
        mapController.run(this, null, locationManager, fusedLocationProviderClient);


        profileMainIv = findViewById(R.id.avatar_imageView);
        profileMainIv.setImageResource(R.drawable.profile_avadar);

        // setting Listeners for buttons and imageView
        qrInventoryMainBt.setOnClickListener(this);
        mapMainBt.setOnClickListener(this);
        searchMainBt.setOnClickListener(this);
        leaderboardMainBt.setOnClickListener(this);
        cameraMainBt.setOnClickListener(this);
        profileMainIv.setOnClickListener(this);
        otherProfileBt.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        mainMenuController.findOtherPlayer(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "No QR found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.QR_Inventory_Button:  // open QR Inventory Activity
                intent = new Intent(MainActivity.this, QrInventoryActivity.class);
                startActivity(intent);
                break;
            case R.id.Map_Button:           // open Map Activity
                intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.Search_Button:        // open Search Activity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.Leaderboard_Button:   // open Leaderboard Activity
                intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
                break;
            case R.id.Camera_Button:        // open Camera Activity
                intent = new Intent(this, QrScannedActivity.class);
                startActivity(intent);
                break;
            case R.id.avatar_imageView:     // open Profile Activity
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.Other_Player_Button:  // open Inventory Activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // ask permission
                    mainMenuController.checkPermission(this);
                }
                else {
                    // Pop camera
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(intent);
                }

                break;

        }

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        Log.e("MainMenuController: ", "Request now has the result");
        switch (requestCode){
            case CAMERA_ACTION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Pop camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(intent);
                    Log.e("MainMenuController", "asking permission");
                    break;
                }
        }
    }
}