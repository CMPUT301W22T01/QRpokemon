package com.qrpokemon.qrpokemon;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrCodeController {

    private static QrCodeController currentInstance;
    private QrCode currentQrCode = null;

    private QrCodeController() {}

    public static QrCodeController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrCodeController();

        return currentInstance;
    }

//public void getQrCode(Context context, @Nullable String currentQrCode) throws Exception {
//
//        Database database = Database.getInstance();
//        List<Map> result = new ArrayList<Map>();
//        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
//
//            @Override
//            public void run(List<Map> dataList) {
//                if (dataList != null) {
//                    EditText email = (EditText) ((Activity) context).findViewById(R.id.et_email);
//                }
//            }
//        };
//        database.getData(databaseCallback, result, "QrCode", "hash111");
//    }
}
