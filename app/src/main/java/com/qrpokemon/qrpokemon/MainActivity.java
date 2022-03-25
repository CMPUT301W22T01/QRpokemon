package com.qrpokemon.qrpokemon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.activities.leaderboard.LeaderboardActivity;
import com.qrpokemon.qrpokemon.activities.qrinventory.QrInventoryActivity;
import com.qrpokemon.qrpokemon.activities.qrscanned.QrScannedActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private Button qrInventoryMainBt;
    private Button mapMainBt;
    private Button searchMainBt;
    private Button leaderboardMainBt;
    private FloatingActionButton cameraMainBt;
    private ImageView profileMainIv;
    private MainMenuController mainMenuController = MainMenuController.getInstance();

    /**
     * MainMenu is created here, it sends user to different activities by pressing buttons
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

        //setup for getting location:
        MapController mapController  = MapController.getInstance();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get location in mapController
        mapController.run(this, null, locationManager,  fusedLocationProviderClient);


        profileMainIv = findViewById(R.id.avatar_imageView);
        profileMainIv.setImageResource(R.drawable.profile_avadar);

        // setting Listeners for buttons and imageView
        qrInventoryMainBt.setOnClickListener(this);
        mapMainBt.setOnClickListener(this);
        searchMainBt.setOnClickListener(this);
        leaderboardMainBt.setOnClickListener(this);
        cameraMainBt.setOnClickListener(this);
        profileMainIv.setOnClickListener(this);

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
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
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

        }
    }
}