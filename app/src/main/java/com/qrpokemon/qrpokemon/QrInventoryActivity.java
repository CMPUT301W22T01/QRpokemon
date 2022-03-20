package com.qrpokemon.qrpokemon;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class QrInventoryActivity extends AppCompatActivity implements View.OnClickListener {

    private String selectedHash, currentPlayer;
    private Integer selectedPosition;
    private TextView totalScore, totalCount;
    private Button ascendingButton, descendingButton;
    private FloatingActionButton backButton, deleteButton;
    private ListView qrInventoryList;
    private HashMap hashMapOfPlayerData;
    private HashMap<String, Object> m = new HashMap<>();
    private ArrayList<String> qrHashCodes;
    private ArrayAdapter<String> qrInventoryDataAdapter;
    private QrInventoryController qrInventoryController = QrInventoryController.getInstance();


    final private String TAG = "QrInventoryActivity";

    /**
     * Displays all QrInventory related information, including a title, a total score board,
     * a list of all qrcodes currently owned by the player, and a back button
     *
     * @param savedInstanceState saved Instances so far
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory);

        totalCount = findViewById(R.id.tv_total_count);
        totalScore = findViewById(R.id.tv_total_score);
        qrInventoryList = findViewById(R.id.QR_inventory_list);
        backButton = findViewById(R.id.bt_back);
        deleteButton = findViewById(R.id.bt_delete);
        descendingButton = findViewById(R.id.bt_descending);
        ascendingButton = findViewById(R.id.bt_ascending);

        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

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

        // Set the total # of the qrCodes the current user has
        totalCount.setText("Total Number: " + qrHashCodes.size());

        // todo Set the total score of the current player
        totalScore.setText("Total Score: ");

        qrInventoryDataAdapter = new QrInventoryCustomList(this, new ArrayList<String>());
        qrInventoryList.setAdapter(qrInventoryDataAdapter);

        // todo Get all the scores of the qrcode that owned by the player
        try {
            qrInventoryController.getAndSetQrCodeData(this, qrHashCodes, qrInventoryDataAdapter);
            Log.e(TAG, "QrCode documents of the current player has the following: " + qrInventoryDataAdapter.getItem(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error: " + e);
        }


        // Set a click listener for the listview
        qrInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
                selectedHash = adapterView.getItemAtPosition(i).toString();

                Log.e(TAG, "Current hash: " + selectedHash);
                deleteButton.setVisibility(VISIBLE);
            }
        });
    }

    /**
     * Check on which button user have entered
     * User can either choose to:
     * delete a selected qrCodes from his inventory
     * go back to main menu
     *
     * @param view QrInventory's view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            case R.id.bt_delete:
                // deducted the current score from the total score
                Integer curTotalScore;
                String cut = " ";

                String[] tStr = qrInventoryDataAdapter.getItem(selectedPosition).split(cut);
                curTotalScore = Integer.valueOf((String) totalScore.getText());

                curTotalScore -= Integer.valueOf(tStr[1]);
                totalScore.setText(curTotalScore.toString());

                // delete from arraylist
                qrHashCodes.remove(selectedHash);
                qrInventoryDataAdapter.remove(selectedHash);

                // delete from local and firebase
                PlayerController playerController = PlayerController.getInstance();
                try {
                    playerController.savePlayerData(qrHashCodes.size(), curTotalScore, qrHashCodes, null);

                    qrInventoryList.setAdapter(qrInventoryDataAdapter);
                    totalCount.setText("Total Number: " + qrHashCodes.size());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "error from Onclick (delete from local and firebase): " + e);
                }

                deleteButton.setVisibility(INVISIBLE);

            case R.id.bt_descending:

            case R.id.bt_ascending:
        }
    }
}

