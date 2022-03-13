package com.qrpokemon.qrpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button qrInventoryMainBt;
    private Button mapMainBt;
    private Button searchMainBt;
    private Button leaderboardMainBt;
    private FloatingActionButton cameraMainBt;

    private TextView usernameMainTv;
    private ImageView profileMainIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

        qrInventoryMainBt = findViewById(R.id.QR_Inventory_Button);
        mapMainBt = findViewById(R.id.Map_Button);
        searchMainBt = findViewById(R.id.Search_Button);
        leaderboardMainBt = findViewById(R.id.Leaderboard_Button);
        cameraMainBt = findViewById(R.id.Camera_Button);

        usernameMainTv = findViewById(R.id.user_textView);
        profileMainIv = findViewById(R.id.avatar_imageView);

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
        switch (view.getId()){
            case R.id.QR_Inventory_Button:  // open QR Inventory Activity
//                Intent intent = new Intent(MainActivity.this,QRInventoryActivity.class);
//                startActivity(intent);
                break;
            case R.id.Map_Button:           // open Map Activity
//                Intent intent = new Intent(MainActivity.this,MapActivity.class);
//                startActivity(intent);
                break;
            case R.id.Search_Button:        // open Search Activity
//                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
//                startActivity(intent);
                break;
            case R.id.Leaderboard_Button:   // open Leaderboard Activity
//                Intent intent = new Intent(MainActivity.this,LeaderboardActivity.class);
//                startActivity(intent);
                break;
            case R.id.Camera_Button:        // open Camera Activity
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, 101);

//                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
//                startActivity(intent);
                break;
            case R.id.avatar_imageView:     // open Profile Activity
//                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
//                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

// get information from https://www.geeksforgeeks.org/android-how-to-open-camera-through-intent-and-display-captured-image/#:~:text=When%20the%20app%20is%20opened%2C%20it%20displays%20the,is%20captured%2C%20it%20is%20displayed%20in%20the%20imageview.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

//            Intent intent = new Intent(MainActivity.this,QrScannedActivity.class);
//            intent.putExtra("data", photo);
//            startActivity(intent);

        }
    }
}