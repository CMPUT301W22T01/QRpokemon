package com.qrpokemon.qrpokemon;
import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.TextView;

import com.qrpokemon.qrpokemon.activities.qrscanned.QrScannedController;
import com.qrpokemon.qrpokemon.models.DatabaseController;
import com.qrpokemon.qrpokemon.models.FileSystemController;
import com.qrpokemon.qrpokemon.models.PlayerController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qrpokemon.qrpokemon.activities.signup.SignupActivity;
import com.qrpokemon.qrpokemon.activities.signup.SignupController;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenuController  {
    private static final int RESULT_OK = -1;
    private static MainMenuController currentInstance;
    private DatabaseController database = DatabaseController.getInstance();
    private FileSystemController fileSystemController = new FileSystemController();
    private QrScannedController qrScannedController = new QrScannedController();
    public static final int CAMERA_ACTION_CODE = 101;
    private Bitmap photoBitmap;
    private String photoContent;
    private String hash = "";


    public static MainMenuController getInstance() {
        if (currentInstance == null)
            currentInstance = new MainMenuController();

        return currentInstance;
    }


    /**
     * load current username from firestore
     *
     * @param context context of current activity
     */
    public void load(Context context) {

        TextView text = ((MainActivity) context).findViewById(R.id.user_textView);
        PlayerController playerController = PlayerController.getInstance();

        try {
            text.setText((String) playerController.getPlayer(null, null, null, null).get("Identifier"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPermission(Context context) {
        qrScannedController.checkPermission(context);
    }

    public void findOtherPlayer(ActivityResult result) throws  Exception{
        Bundle bundle = result.getData().getExtras();
        photoBitmap = (Bitmap) bundle.get("data");
        photoContent = qrScannedController.analyzeImage(photoBitmap);
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            messageDigest.update(photoContent.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Get the hex hash string
        hash = qrScannedController.byte2Hex(messageDigest.digest());
    }

}





