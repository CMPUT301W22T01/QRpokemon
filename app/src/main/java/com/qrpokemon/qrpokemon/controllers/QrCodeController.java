package com.qrpokemon.qrpokemon.controllers;

import android.util.Log;

import androidx.annotation.Nullable;

import com.qrpokemon.qrpokemon.models.QrCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrCodeController {

    private static QrCodeController currentInstance;
    private DatabaseProxy databaseProxy = DatabaseProxy.getInstance();
    private QrCode currentQrCode = null;

    private QrCodeController() {}

    public static QrCodeController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrCodeController();

        return currentInstance;
    }

    /**
     * Create/Update a qrcode to Firestore database based on information given
     * @param qrHash hash of qrcode
     * @param score score of qrcode
     * @param location location where qr code is scanned
     * @param comments comments of this qrcode
     * @param bitmap photo of this qrcode, a HashMap which has key of username and value is the photo captured by that user
     * @param codeContent The message of this qr code
     * @param newCode if this is a new qrcode to Firestore database
     * @throws Exception throws Exception if collection name is incorrect
     */
    public void saveQr(String qrHash, @Nullable Integer score, @Nullable ArrayList<String> location,
                       @Nullable HashMap<String, Object> comments, @Nullable HashMap<String,String> bitmap,@Nullable String codeContent, Boolean newCode) throws Exception {

        HashMap<String, Object> current = new HashMap<>();

        if (newCode) {
            current.put("Identifier", qrHash);
            current.put("Score", score);
            current.put("Comments", comments);
            current.put("Location",location);
            current.put("Bitmap", bitmap);
            current.put("Content", codeContent);
            Log.e("Qrcode controller", location.toString());
            databaseProxy.writeData("QrCode", qrHash,current,true);
        }
        else {
            current = new HashMap<>();
            if (score != null) {
                current.put("Score",score);
            }
            if (location != null) {
                current.put("Location", location);
            }
            if (comments != null) {
                current.put("Comments", comments);
            }
            if (bitmap != null) {
                current.put("Bitmap", bitmap);
            }
//            Log.e("Qrcode controller", location.toString());

            databaseProxy.writeData("QrCode", qrHash,current,false);
        }

    }
    public void saveQr(String qrHash, @Nullable Integer score, @Nullable ArrayList<String> location,
                       @Nullable HashMap<String, Object> comments, @Nullable HashMap<String,String> bitmap, @Nullable String codeContent) throws Exception {
        saveQr(qrHash,score,location,comments,bitmap,codeContent,false);
    }

    /**
     * Get a specific qrcode by qrhash
     * @param databaseCallback A callback function when data is retrieved from Firestore database
     * @param list A List(Map) object that contains result from Firestore
     * @param objectName Hash of qrcode
     * @throws Exception throws Exception if collection name is incorrect
     */
    public void getQR(DatabaseCallback databaseCallback, List<Map> list, String objectName) throws Exception {
        databaseProxy.getData(databaseCallback, list, "QrCode", objectName);
    }

    /**
     * Delete a qrcode by it's unique hash
     * @param objectName qrhash of this qrcode
     */
    public void deleteQr(String objectName) {
        try {
            databaseProxy.deleteData("QrCode", objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
