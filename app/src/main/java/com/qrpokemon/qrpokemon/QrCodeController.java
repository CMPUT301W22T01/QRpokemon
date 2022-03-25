package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrCodeController {

    private static QrCodeController currentInstance;
    private DatabaseController databaseController = DatabaseController.getInstance();
    private QrCode currentQrCode = null;

    private QrCodeController() {}

    public static QrCodeController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrCodeController();

        return currentInstance;
    }

    // Todo: Waiting for more implementation, currently not used

    public void saveQr(String qrHash, @Nullable Integer score, @Nullable ArrayList<String> location,
                       @Nullable ArrayList<String> comments, @Nullable HashMap<String,Bitmap> bitmap, Boolean newCode) throws Exception {

        HashMap<String, Object> current = new HashMap<>();

        if (newCode) {
            current.put("Identifier", qrHash);
            current.put("Photo", bitmap);
            current.put("Score", score);
            current.put("Comments", comments);
            current.put("Location",location);
            current.put("Bitmap", bitmap);
            databaseController.writeData("QrCode", qrHash,current,true);
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

            databaseController.writeData("QrCode", qrHash,current,false);
        }

    }
    public void saveQr(String qrHash, @Nullable Integer score, @Nullable ArrayList<String> location,
                       @Nullable ArrayList<String> comments, @Nullable HashMap<String,Bitmap> bitmap) throws Exception {
        saveQr(qrHash,score,location,comments,bitmap,false);
    }

    public void getQR(DatabaseCallback databaseCallback, List<Map> list, String objectName) throws Exception {
        databaseController.getData(databaseCallback, list, "QrCode", objectName);
    }
}
