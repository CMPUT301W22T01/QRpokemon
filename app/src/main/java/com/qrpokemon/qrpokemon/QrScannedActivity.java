package com.qrpokemon.qrpokemon;
package com.qrpokemon.qrpokemon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrScannedActivity extends AppCompatActivity {

//    private CodeScanner mCodeScanner;
//    String qrMessage;

    // create variable for capture and save image
    private ImageView photoImage;
    private Button takePhoto;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap photoBitmap;
    private String codeContent;
    final private String TAG = "TestCamera";
    public static final int CAMERA_ACTION_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanned_activity);

        // binding variables with layout
        photoImage = findViewById(R.id.code_not_found_imageView);
        takePhoto = findViewById(R.id.scan_Button_no_code_found);

        FloatingActionButton backButton = findViewById(R.id.backButton_no_code_found);
        TextView title = findViewById(R.id.no_code_found_textView);
        Switch photoSave = findViewById(R.id.qr_switch);
        Switch locationSave = findViewById(R.id.location_switch);
//      Button scanButton = findViewById(R.id.scan_Button_no_code_found);


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

        locationSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (locationSave.isChecked()){
                    Toast.makeText(QrScannedActivity.this, "Location will be saved", Toast.LENGTH_SHORT).show();
                } else {
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
                    codeContent = doInBackground(photoBitmap);
//                    Toast.makeText(QrScannedActivity.this, codeContent, Toast.LENGTH_LONG).show();
                    MessageDigest messageDigest;
                    String hash = "";
                    try {
                        messageDigest = MessageDigest.getInstance("SHA-256");
                        messageDigest.update(codeContent.getBytes("UTF-8"));

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
                    }
                }

            }
        });

        // ask permission
        checkPermission();
        // create click event for button take photo
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
            }
        });

    }

    protected String doInBackground(Bitmap bitmap) {
        // convert bitmap to string
        String content = null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(binaryBitmap);
            content = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "decode exception", e);
        }
        return content;
    }

    private String byte2Hex(byte[] bytes) {

        // Converts bytes to hex string
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

    private void checkPermission(){
        // ask permission for user camera
        if (ContextCompat.checkSelfPermission(QrScannedActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QrScannedActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_ACTION_CODE);
        }

    }



}
