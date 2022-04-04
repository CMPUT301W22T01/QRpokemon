package com.qrpokemon.qrpokemon.views.signup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.NotFoundException;
import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.controllers.QrScannedController;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestoreSession extends AppCompatActivity {
    private static final int CAMERA_ACTION_CODE = 100;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap photoBitmap;
    private String codeContent;
    private QrScannedController qrScannedController = QrScannedController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    photoBitmap = (Bitmap) bundle.get("data");

                    // identify if its a QR code and get the bitmap for the picture
                    try {
                        codeContent = qrScannedController.analyzeImage(photoBitmap);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreSession.this, "QRcode Not found",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    MessageDigest messageDigest;
                    try {
                        DatabaseCallback databaseCallback = new DatabaseCallback(RestoreSession.this) {
                            @Override
                            public void run(List<Map> dataList) {
                                if (!dataList.isEmpty()){
                                    Map mapList = dataList.get(0);
                                    playerController.setupPlayer((String ) mapList.get("Identifier"),
                                                                (ArrayList<String>) mapList.get("qrInventory"),
                                                                (HashMap) mapList.get("contact"),
                                                                ((Long) mapList.get("qrCount")).intValue(),
                                                                ((Long) mapList.get("totalScore")).intValue(),
                                                                ((Long) mapList.get("highestUnique")).intValue(),
                                                                (Boolean) mapList.get("Owner"),
                                                                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                                    try {
                                        Log.e("RestoreSession: ", "User "+(String ) mapList.get("Identifier")+" found!");
                                        playerController.savePlayerData(null, null, null, null, null, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), null, false);
                                    } catch (Exception e) { // if collection is incorrect for DatabaseController:
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.e("RestoreSession: ", "User "+codeContent +" Not found!");
                                }
                                finish();
                            }
                        };
                        Log.e("Restore Session: ", "Getting data");
                        playerController.getPlayer(databaseCallback, new ArrayList<>(), codeContent, null);
                    } catch (Exception e){ // if a qr isn't found
                        Log.e("Restore Session: ", "Qr not found");
                        e.printStackTrace();
                        finish();
                    }
                } else {
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
     * Receive user's decision on whether gives a camera permission
     * pop camera if permission is granted
     * @param requestCode 100 is camera request code
     * @param permissions All permission asked
     * @param grantResults Result of request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("RestoreSession: ", "Request now has the result: " + String.valueOf(requestCode));
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
