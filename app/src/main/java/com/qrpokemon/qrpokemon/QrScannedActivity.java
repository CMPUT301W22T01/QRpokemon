package com.qrpokemon.qrpokemon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class QrScannedActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String qrMessage ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);
        CodeScannerView scannerView = findViewById(R.id.code_not_found_imageView);;
        mCodeScanner = new CodeScanner(this, scannerView);
        checkPermission();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //display user the text of the qr code, see if this is user's wanted
                        //if user not satisfied, keep searching by touch camera view
                        Toast.makeText(QrScannedActivity.this, result.getText(), Toast.LENGTH_LONG).show();
                        qrMessage = result.getText();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        FloatingActionButton backButton = findViewById(R.id.backButton_no_code_found);
        Button scanButton = findViewById(R.id.scan_Button_no_code_found);
        TextView title = findViewById(R.id.no_code_found_textView);
        Switch photoSave = findViewById(R.id.qr_switch);
        Switch locationSave = findViewById(R.id.location_switch);


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //user chooses to save the score
                // Calculates the sha-256 hash
                MessageDigest messageDigest;
                String hash = "";
                try {
                    messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(qrMessage.getBytes("UTF-8"));

                    // Get the hex hash string
                    hash = byte2Hex(messageDigest.digest());


                    TextView qrHash = findViewById(R.id.qr_result);
                    qrHash.setText("Score: " + String.valueOf(scoreCalculator(hash)));
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
        });

        // two switch buttons
        photoSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (photoSave.isChecked()){
                    Toast.makeText(QrScannedActivity.this, "QR code will be saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QrScannedActivity.this, "QR code won't be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        photoSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (photoSave.isChecked()){
                    Toast.makeText(QrScannedActivity.this, "Location will be saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QrScannedActivity.this, "Location won't be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkPermission();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    // Converts bytes to hex string
    private String byte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        String s = null;
        for (int i = 0; i < bytes.length; i++) {
            s = Integer.toHexString(bytes[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private void checkPermission(){
        Log.d("QrScannedActivity: ", "Checkpermission.");
        String permission = Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission) == PackageManager.PERMISSION_GRANTED){

        } else {
            String[] temp = {permission};
            ActivityCompat.requestPermissions(this, temp, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkPermission();
    }

    public int scoreCalculator(String hash){
        // finds repeat strings in hash string
        ArrayList<String> list = new ArrayList<String>();
        String s = "";
        for (int i = 0; i < hash.length(); i++) {
            char ch = hash.charAt(i);
            if (s.isEmpty() || s.charAt(0) == ch) {
                s += ch;
            } else {
                if (s.length() > 1) {
                    list.add(s);
                }
                s = "" + ch;
            }
        }
        if(s.length()>1){
            list.add(s);
        }
        // Calculates the score base on hash string
        int score = 0;
        for (String str : list) {
            int i = Integer.parseInt(str.substring(0, 1), 16);
            score += (int) Math.pow(i, str.length() - 1);
        }
        return score;
    }
}
