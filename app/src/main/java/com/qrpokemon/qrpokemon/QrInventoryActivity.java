package com.qrpokemon.qrpokemon;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class QrInventoryActivity extends AppCompatActivity {

    private HashMap listOfPlayerData;
    private ListView qrInventoryList;
    private ArrayList<String> qrInventoryDataList;
    private ArrayAdapter<String> qrInventoryDataAdapter;
    private QrInventoryController qrInventoryController = QrInventoryController.getInstance();

    private String currentPlayer = null;
    final private String TAG = "QrInventoryActivity";

    /**
     *
     * Displays all QrInventory related information, including a title, a total score board,
     * a list of all qrcodes currently owned by the player, and a back button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory);

        // Get all the data of the current player's document
        try {
            // So far, 'listOfPlayData' should have contained all the information of the current player's
            // document, which is a hashMap.
            listOfPlayerData = qrInventoryController.getPlayerInfo(currentPlayer);
            Log.e(TAG, "All info of the current player: " + listOfPlayerData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        qrInventoryList = findViewById(R.id.QR_inventory_list);

        // Get all hashcode of the qrcode that owned by the player
        qrInventoryDataList = (ArrayList<String>) listOfPlayerData.get("qrInventory");
        Log.e(TAG, "Current player has the following qrCodes: " + qrInventoryDataList.toString());

        qrInventoryDataAdapter = new QrInventoryCustomList(this, qrInventoryDataList);
        qrInventoryList.setAdapter(qrInventoryDataAdapter);

    }
}
