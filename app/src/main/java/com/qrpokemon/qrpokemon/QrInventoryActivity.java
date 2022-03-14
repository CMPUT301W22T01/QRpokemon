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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class QrInventoryActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    private String seletedHash;
    private String currentPlayer;
    private Button ascendingButton, descendingButton;
    private FloatingActionButton backButton, deleteButton;
    private ListView qrInventoryList;
    private HashMap hashMapOfPlayerData;
    private HashMap<String, Object> m = new HashMap<>();
    private ArrayList<Integer> scoresOfPlayer;
    private ArrayList<String> qrHashCodes;
    private ArrayAdapter<String> qrInventoryDataAdapter;
    private QrInventoryController qrInventoryController = QrInventoryController.getInstance();


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
        descendingButton = findViewById(R.id.bt_descending);
        ascendingButton = findViewById(R.id.bt_ascending);

        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        qrInventoryController.addObserver(this);

        // Get all the data of the current player's document
        try {
            // So far, 'listOfPlayData' should have contained all the information of the current player's
            // document, which is a hashMap.
            hashMapOfPlayerData = qrInventoryController.getPlayerInfo(null);
            Log.e(TAG, "All info of the current player: " + hashMapOfPlayerData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        // Get the identifier of the current player
        currentPlayer = hashMapOfPlayerData.get("Identifier").toString();
        Log.e(TAG, "Current player is: " + currentPlayer);

        // Get all hashcode of the qrcode that owned by the player
        qrHashCodes = (ArrayList<String>) hashMapOfPlayerData.get("qrInventory");
        Log.e(TAG, "Current player has the following qrCodes: " + qrHashCodes.toString());

        // Get all the scores of the qrcode that owned by the player
        try {
            qrInventoryController.getQrCodeDocument(this, qrHashCodes);
//            update(qrInventoryController, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //update(qrInventoryController, null);

        qrInventoryDataAdapter = new QrInventoryCustomList(this, qrHashCodes);
        qrInventoryList.setAdapter(qrInventoryDataAdapter);

        // Set a click listener for the listview
        qrInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seletedHash = adapterView.getItemAtPosition(i).toString();
                Log.e(TAG, "Current hash: " + seletedHash);
                deleteButton.setVisibility(VISIBLE);
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
                qrInventoryDataAdapter.remove(seletedHash);

                // delete from local and firebase
                PlayerController playerController = PlayerController.getInstance();
                try {
                    playerController.savePlayerData(null, null, qrHashCodes, null);

                    qrInventoryList.setAdapter(qrInventoryDataAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                deleteButton.setVisibility(INVISIBLE);

        }
    }

    @Override
    public void update(Observable observable, Object o) {

        try {

            m = qrInventoryController.returnList();
            Log.e(TAG, "MMMMMMMMMM: " + m.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Got: " + e);
        }
    }
}
