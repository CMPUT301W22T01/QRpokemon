package com.qrpokemon.qrpokemon.activities.qrscanned;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.qrpokemon.qrpokemon.DatabaseCallback;
import com.qrpokemon.qrpokemon.DatabaseController;
import com.qrpokemon.qrpokemon.FileSystemController;
import com.qrpokemon.qrpokemon.QrCodeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrScannedController {
    final private String TAG = "QrSannedCotroller: ";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int CAMERA_ACTION_CODE = 100;
    private QrCodeController qrCodeController = QrCodeController.getInstance();

    private static QrScannedController currentInstance;

    public static QrScannedController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrScannedController();

        return currentInstance;
    }

    /**
     * Analyze the photo user passed in
     * @param bitmap photo current player takes
     * @return A string decoded from QRhash found in photo
     */
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

    /**
     * Convert bytes to hex value
     * @param bytes bytes converted from message contained in QR code scanned
     * @return A string contains hex value of bytes passed in
     */
    public String byte2Hex(byte[] bytes) {

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

    /**
     * Calculate the score based on QR code's hash value
     * @param hash QR code's hash
     * @return An integer represents the score of this context
     */
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

    public void saveQrCode(Context context, String qrHash, int score, String location, Bitmap bitmap) throws Exception {
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {
                if (!dataList.isEmpty()){ //this QR code is in Database

                }
                else { //new QrCode added
                    HashMap<String, Bitmap> bitmapHash = new HashMap<>();
                    FileSystemController fileSystemController = new FileSystemController();
                    bitmapHash.put(fileSystemController.readToFile(context, "name"),bitmap);
//                    qrCodeController.saveQr(qrHash, score, location,null ,bitmapHash );
                }
            }
        };
//        qrCodeController.getQR(databaseCallback, );

    }

    /**
     * Check the permission of the camera
     * @param context QrScannedActivity
     */
    public void checkPermission(Context context){
        // ask permission for user camera
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_ACTION_CODE);
        }
    }

}