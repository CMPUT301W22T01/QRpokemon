package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class QrInventoryController extends Observable {

    private HashMap<String, Object> data;
    private Database database = Database.getInstance();
    private PlayerController player = PlayerController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();

    private static QrInventoryController currentInstance;
    final private String TAG = "QrInventoryController";

    private QrInventoryController(){}

    public static QrInventoryController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrInventoryController();

        return currentInstance;
    }


    /**
     *
     * @param currentPlayer The identifier of the current player
     * @return all the information of the current player's document, which is a hashMap
     * @throws Exception
     */
    public HashMap<String, Object> getPlayerInfo (String currentPlayer) throws Exception {

        HashMap<String, Object> temp;
        temp = playerController.getPlayer(currentPlayer);
        return temp;
    }


    public HashMap<String, Object> getQrCodeDocument(Context context, ArrayList<String> qrHashCodes) throws Exception {

        List<Map> result = new ArrayList<Map>();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {

            @Override
            public void run(List<Map> dataList) {
                if (!dataList.isEmpty()) {
                    if (dataList != null && data.get((String) dataList.get(0).get("Identifier")) == null) {
                        data.put((String) dataList.get(0).get("Identifier"), dataList);
                    }
                    setChanged();
                    notifyObservers(null);
                }
            }
        };
        for (int i = 0; i < qrHashCodes.size(); i++) {
            database.getData(databaseCallback, result, "QrCode", qrHashCodes.get(i));
        }
//        database.getData(databaseCallback, result, "QrCode", qrHash);
        return data;
    }
}

