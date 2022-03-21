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
import java.util.Comparator;
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
        descendingButton.setOnClickListener(this);
        ascendingButton.setOnClickListener(this);

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

                String cut = " ";
                String curString = adapterView.getItemAtPosition(i).toString();
                String[] tStr = curString.split(cut);
                selectedHash = tStr[1];

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

                curTotalScore -= Integer.valueOf(tStr[0]);
                totalScore.setText(curTotalScore.toString());

                // delete from arraylist
                qrHashCodes.remove(selectedHash);
                qrInventoryDataAdapter.remove(qrInventoryDataAdapter.getItem(selectedPosition));

                // delete from local and firebase
                PlayerController playerController = PlayerController.getInstance();
                try {
                    playerController.savePlayerData(qrInventoryDataAdapter.getCount(), curTotalScore, qrHashCodes, null);

                    qrInventoryList.setAdapter(qrInventoryDataAdapter);
                    totalCount.setText("Total Number: " + qrInventoryDataAdapter.getCount());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error from Onclick (delete from local and firebase): " + e);
                }

                deleteButton.setVisibility(INVISIBLE);

                break;

            case R.id.bt_descending:
                Log.e(TAG, "After clicking the DESCENDING button: " + qrInventoryDataAdapter.getCount());

                qrInventoryDataAdapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        String cut = " ";
                        String[] tStr1 = s.split(cut);
                        String[] tStr2 = t1.split(cut);
                        Integer s1 = Integer.valueOf(tStr1[0]);
                        Integer s2 = Integer.valueOf(tStr2[0]);

                        if (s1 > s2) {
                            return -1;
                        }
                        else if (s1.equals(s2)) {
                            return 0;
                        }
                        else {
                            return 1;
                        }
                    }
                });

                qrInventoryList.setAdapter(qrInventoryDataAdapter);

                break;

            case R.id.bt_ascending:
                Log.e(TAG, "After clicking the ASCENDING button: " + qrInventoryDataAdapter.getCount());

                qrInventoryDataAdapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        String cut = " ";
                        String[] tStr1 = s.split(cut);
                        String[] tStr2 = t1.split(cut);
                        Integer s1 = Integer.valueOf(tStr1[0]);
                        Integer s2 = Integer.valueOf(tStr2[0]);

                        if (s1 > s2) {
                            return 1;
                        }
                        else if (s1.equals(s2)) {
                            return 0;
                        }
                        else {
                            return -1;
                        }
                    }
                });

                qrInventoryList.setAdapter(qrInventoryDataAdapter);

                break;
        }
    }
}

