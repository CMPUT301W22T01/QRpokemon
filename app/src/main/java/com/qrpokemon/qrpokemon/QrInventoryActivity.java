package com.qrpokemon.qrpokemon;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class QrInventoryActivity extends AppCompatActivity implements View.OnClickListener {

    private String seletedHash;
    private HashMap listOfPlayerData;
    private FloatingActionButton backButton, deleteButton;
    private ListView qrInventoryList;
    private ArrayList<String> qrInventoryData;
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

        qrInventoryList = findViewById(R.id.QR_inventory_list);
        backButton = findViewById(R.id.bt_back);
        deleteButton = findViewById(R.id.bt_delete);

        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

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

        // Get all hashcode of the qrcode that owned by the player
        qrInventoryData = (ArrayList<String>) listOfPlayerData.get("qrInventory");
        Log.e(TAG, "Current player has the following qrCodes: " + qrInventoryData.toString());

        qrInventoryDataAdapter = new QrInventoryCustomList(this, qrInventoryData);
        qrInventoryList.setAdapter(qrInventoryDataAdapter);

        // Delete Delete Delete Delete Delete Delete Delete Delete Delete Delete Delete
        qrInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seletedHash = adapterView.getItemAtPosition(i).toString();
                Log.e(TAG, "POP" + seletedHash.toString());
//                deleteButton.setVisibility(VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_back:
                Intent main_menu_intent = new Intent(this, MainActivity.class);
                startActivity(main_menu_intent);
                break;

            case R.id.bt_delete:
                // delete from arraylist
                qrInventoryData.remove(seletedHash);

                // delete from local and firebase
                PlayerController playerController = PlayerController.getInstance();
                try {
                    playerController.savePlayerData(null, null, qrInventoryData, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                deleteButton.setVisibility(INVISIBLE);

        }
    }

}
