package com.qrpokemon.qrpokemon.activities.qrscanned;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.PlayerController;
import com.qrpokemon.qrpokemon.models.QrCodeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrScannedController {
    final private String TAG = "QrSannedCotroller: ";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int CAMERA_ACTION_CODE = 100;
    private QrCodeController qrCodeController = QrCodeController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();


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
    public String analyzeImage(Bitmap bitmap) throws NotFoundException {
        // convert bitmap to string
        String content = null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();

        Result result = reader.decode(binaryBitmap);
        content = result.getText();
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

    /**
     * Save the QRcode scanned.
     * Before saving, it checks database of current QR code:
     * If it is a new qrcode, initialize its hash, score, location and photo (if user provided)
     * Otherwise, update information provided by new user who scanned an exisited qrcode (update location and photo if provided)
     * @param context Activity whichever calls it
     * @param qrHash qrHash of this QR code (AKA Identifier)
     * @param score score of this QR code
     * @param location Location of where this QR is captured
     * @param bitmap A small size photo of this QR code.
     * @throws Exception getQR() may throw exception, but highly unlikely since the error is raised
     * from DatabaseProxy complaining about incorrect collection name
     */
    public void saveQrCode(Context context, String qrHash, int score, String location, String bitmap) throws Exception {
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {
                Log.e("QrScannedController",qrHash);
                if (!dataList.isEmpty()){
                    Log.e("QrScannedController","if");
                    // this QR code is in Database
                    ArrayList<String> location1 = (ArrayList<String>) dataList.get(0).get("Location");
                    location1.add(location);
                    HashMap<String, String> bitmapHash = (HashMap<String, String>) dataList.get(0).get("Bitmap");

                    try {
                        HashMap currentPlayer = playerController.getPlayer(null,null,null,null);
                        bitmapHash.put((String) currentPlayer.get("Identifier"), bitmap);
                        qrCodeController.saveQr(qrHash, score, location1, null, bitmapHash,true);
                        Log.e("QrScannedController","Qrcode found");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else {
                    // new QrCode added
                    Log.e("QrScannedController","else");
                    ArrayList<String> location1 = new ArrayList<>();
                    location1.add(location);
                    HashMap<String, String> bitmapHash = new HashMap<>();
//                    Log.e("QrScannedController",bitmap);
                    try {
                        HashMap currentPlayer = playerController.getPlayer(null,null,null,null);
                        bitmapHash.put((String) currentPlayer.get("Identifier"), bitmap);
                        qrCodeController.saveQr(qrHash, score, location1,null ,bitmapHash, true);
//                        playerController.savePlayerData()

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        };

        // create a list map to store Identifier
        List<Map> list = new ArrayList<>();
        qrCodeController.getQR(databaseCallback, list, qrHash);

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
