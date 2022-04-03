package com.qrpokemon.qrpokemon.views.qrinventory;

import static android.view.View.VISIBLE;
import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.R;
import com.qrpokemon.qrpokemon.controllers.QrInventoryController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class QrInventoryActivity
        extends AppCompatActivity
        implements View.OnClickListener, QrInventoryAddCommentFragment.OnFragmentInteractionListener, Observer {

    private String selectedHash, selectedBitmap, currentPlayer;
    private Integer selectedPosition, curMaxScore;
    private TextView totalScore, totalCount, qrInventoryTitle;
    private Button ascendingButton, descendingButton;
    private FloatingActionButton backButton, deleteButton, commentButton, showCommentsButton;
    private ListView qrInventoryList;
    private HashMap hashMapOfPlayerData, commentsOfCurQrcode;
    private Map currentQR;
    private ArrayList<Integer> allTheScores;
    private ArrayList<String> qrHashCodes = new ArrayList<>();
    private ArrayAdapter<String> qrInventoryDataAdapter;
    private QrInventoryController qrInventoryController = QrInventoryController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();

    final private String TAG = "QrInventoryActivity";

    /**
     * Displays all QrInventory related information, including a title, a total score board,
     * a total number board, a list of all qrcodes currently owned by the player,a delete button,
     * and a back button
     *
     * @param savedInstanceState saved Instances so far
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory);

        totalCount = findViewById(R.id.tv_qr_count);
        totalScore = findViewById(R.id.tv_total_score);
        qrInventoryList = findViewById(R.id.QR_inventory_list);
        backButton = findViewById(R.id.bt_back);
        deleteButton = findViewById(R.id.bt_delete);
        descendingButton = findViewById(R.id.bt_descending);
        ascendingButton = findViewById(R.id.bt_ascending);
        commentButton = findViewById(R.id.bt_comment);
        showCommentsButton = findViewById(R.id.bt_show_comments);
        qrInventoryTitle = findViewById(R.id.QR_inventory_title);

        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        descendingButton.setOnClickListener(this);
        ascendingButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        showCommentsButton.setOnClickListener(this);

        // Get all the data of the current player's document
        try {
            // So far, 'listOfPlayData' should have contained all the information of the current player's document, which is a hashMap.
            hashMapOfPlayerData = qrInventoryController.getPlayerInfo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the identifier of the current player
        currentPlayer = hashMapOfPlayerData.get("Identifier").toString();

        // Get all hashcode of the qrcode that owned by the player
        try {

            // Get and set the data from the documents of each qrcode in ArrayList "qrHashCode"
            qrInventoryDataAdapter = new QrInventoryCustomList(this, new ArrayList<String>());
            qrInventoryList.setAdapter(qrInventoryDataAdapter);

            DatabaseCallback databaseCallback = new DatabaseCallback(this) {
                @Override
                public void run(List<Map> dataList) {

                    // Get all hashcode
                    for (String codeIdentifier : (ArrayList<String>)dataList.get(0).get("qrInventory")) {
                        qrHashCodes.add(codeIdentifier);
                    }
                    qrInventoryList.setAdapter(qrInventoryDataAdapter);

                    // Get and set the data
                    try {
                        qrInventoryController.getAndSetQrCodeData(QrInventoryActivity.this, qrHashCodes, qrInventoryDataAdapter, currentPlayer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            playerController.getPlayer(databaseCallback, new ArrayList<>(), currentPlayer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set a click listener for the listview
        qrInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set "selectedPosition" as the index (Integer) of the line that player just clicked
                selectedPosition = i;

                // set "selectedHash" as the hash code (String) of the line that player just clicked
                String cut = " ";
                String curString = adapterView.getItemAtPosition(i).toString();
                String[] tStr = curString.split(cut);
                selectedHash = tStr[1];
                selectedBitmap = tStr[2];

                // show the delete button
                deleteButton.setVisibility(VISIBLE);
                commentButton.setVisibility(VISIBLE);
                showCommentsButton.setVisibility(VISIBLE);

                // do not let others delete or edit qrCodes
                if (!hashMapOfPlayerData.get("DeviceId").equals(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))){
                    deleteButton.setVisibility(GONE);
                    commentButton.setVisibility(GONE);
                }
            }
        });
    }

    /**
     * Check on which button user have entered
     * User can either choose to:
     *  - go back to main menu
     *  - delete a selected qrCodes from his inventory
     *  - sort the list in descending order by score to the qrCode with highest score
     *  - sort the list in ascending order by score to the qrCode with lowest score
     * @param view QrInventory's view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * if the player clicked the back button, that is, want to go back to main menu
             */
            case R.id.bt_back:
                try {
                    qrInventoryController.getPlayerInfo(null,true,this, this);
                    qrInventoryTitle.setText("QR Inventory");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                break;

            /**
             * if the player clicked the delete button, that is, want to delete one of his/her qrCode
             */
            case R.id.bt_delete:

                // deducted the current score from the total score
                Integer curTotalScore;
                String cut = " ";

                String[] tStr = qrInventoryDataAdapter.getItem(selectedPosition).split(cut);
                curTotalScore = Integer.valueOf((String) totalScore.getText());

                curTotalScore -= Integer.valueOf(tStr[0]);
                totalScore.setText(curTotalScore.toString());

                // delete from Arraylist/ListView
                qrHashCodes.remove(selectedHash);
                qrInventoryDataAdapter.remove(qrInventoryDataAdapter.getItem(selectedPosition));

                // change the highest unique if needed
                allTheScores = new ArrayList<>();
                for (int i = 0; i < qrInventoryDataAdapter.getCount(); i++) {
                    String s = qrInventoryDataAdapter.getItem(i);
                    String[] ttStr = s.split(" ");
                    allTheScores.add(Integer.valueOf(ttStr[0]));
                }

                // get the max value in the ArrayList 'allTheScores'
                curMaxScore = 0;
                for (Integer curNum : allTheScores) {
                    if (curNum > curMaxScore) {
                        curMaxScore = curNum;
                    }
                }

                // delete from Local and Firebase, update totalCount/totalNumber/highestUnique, update display
                PlayerController playerController = PlayerController.getInstance();
                try {
                    playerController.savePlayerData(qrInventoryDataAdapter.getCount(), curTotalScore, qrHashCodes, null, curMaxScore, null, null,false);
                    qrInventoryList.setAdapter(qrInventoryDataAdapter);

                    totalCount.setText(String.valueOf(qrInventoryDataAdapter.getCount()));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // hide buttons
                deleteButton.setVisibility(GONE);
                commentButton.setVisibility(GONE);
                showCommentsButton.setVisibility(GONE);

                break;

            /**
             * if the user clicked the descending button, that is, want to sort the list in descending order by score
             */
            case R.id.bt_descending:

                qrInventoryDataAdapter.sort(new Comparator<String>() {
                    @Override
                    // how the "sort" function know how to sort
                    public int compare(String s, String t1) {
                        String cut = " ";
                        String[] tStr1 = s.split(cut);
                        String[] tStr2 = t1.split(cut);

                        // get the score
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

            /**
             * if the user clicked the descending button, that is, want to sort the list in descending order by score
             */
            case R.id.bt_ascending:

                qrInventoryDataAdapter.sort(new Comparator<String>() {
                    @Override
                    // how the "sort" function know how to sort
                    public int compare(String s, String t1) {
                        String cut = " ";
                        String[] tStr1 = s.split(cut);
                        String[] tStr2 = t1.split(cut);

                        // get the score
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

            /**
             * if the user clicked the comment button, that is, the player want to add a comment to one of his/her qrCode
             */
            case R.id.bt_comment:
                commentsOfCurQrcode = new HashMap<String, ArrayList<String>>();
                // first, get all the comments that that qrCode('selectedHash') has
                try {
                    DatabaseCallback databaseCallback = new DatabaseCallback(this) {
                        @Override
                        public void run(List<Map> dataList) {

                            // call the fragment and ask player for comment
                            QrInventoryAddCommentFragment qrInventoryAddCommentFragment = new QrInventoryAddCommentFragment();
                            qrInventoryAddCommentFragment.show(getSupportFragmentManager(), "ADD_COMMENT");

                            if (!dataList.isEmpty()){
                                currentQR = dataList.get(0);
                                if (dataList.get(0).get("Comments") == null){
                                } else {

                                    HashMap<String, ArrayList<String>> tMap;
                                    String[] tKeys;
                                    ArrayList<String> keys = new ArrayList<>();

                                    try {
                                        // Get all keys (ArrayList<String>) and store them in ArrayList keys
                                        tMap = (HashMap<String, ArrayList<String>>) dataList.get(0).get("Comments");
                                        tKeys = tMap.keySet().toArray(new String[0]);

                                        //get all data related to selected QRcode:
                                        for (String k : tKeys) {
                                            keys.add(k);
                                        }

                                        // Store all the comments of qrCode into HashMap 'commentsOfCurQrcode'
                                        for (int i = 0; i < tMap.size(); i++) {
                                            commentsOfCurQrcode.put(keys.get(i), tMap.get(keys.get(i)));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    };
                    List<Map> result = new ArrayList<>();
                    qrInventoryController.getAllComments(databaseCallback, selectedHash, result);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            /**
             * if the user clicked the show comments button, that is, the player want to see all the comments of the code
             */
            case R.id.bt_show_comments:
                commentsOfCurQrcode = new HashMap<String, ArrayList<String>>();
                DatabaseCallback databaseCallback = new DatabaseCallback(this) {
                    @Override
                    public void run(List<Map> dataList) {
                        if (!dataList.isEmpty()){ // if this qrcode is found:

                            currentQR = dataList.get(0);

                            if (dataList.get(0).get("Comments") == null){
                                Toast.makeText(QrInventoryActivity.this, "There is no comment for this QR Code", Toast.LENGTH_SHORT).show();

                            } else {//there are comments to show:
                                HashMap<String, ArrayList<String>> tMap;
                                String[] tKeys;
                                ArrayList<String> keys = new ArrayList<>();

                                try {
                                    // Get all keys (ArrayList<String>) and store them in ArrayList keys
                                    tMap = (HashMap<String, ArrayList<String>>) dataList.get(0).get("Comments");
                                    tKeys = tMap.keySet().toArray(new String[0]);

                                    //get all data related to selected QrCode:
                                    for (String k : tKeys) {
                                        keys.add(k);
                                    }

                                    // Store all the comments of qrCode into HashMap 'commentsOfCurQrcode'
                                    for (int i = 0; i < tMap.size(); i++) {
                                        commentsOfCurQrcode.put(keys.get(i), tMap.get(keys.get(i)));
                                    }

                                    // new intent to activity 'QrInventoryShowComments'
                                    Intent intent = new Intent(QrInventoryActivity.this, QrInventoryShowComments.class);
                                    intent.putExtra("commentsOfCurQrcode", commentsOfCurQrcode);
                                    intent.putExtra("selectedBitmap", selectedBitmap);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            Toast.makeText(QrInventoryActivity.this, "There is no comment to show", Toast.LENGTH_SHORT).show();
                        }

                        deleteButton.setVisibility(GONE);
                        commentButton.setVisibility(GONE);
                        showCommentsButton.setVisibility(GONE);
                    }
                };

                // first, get all the comments that that qrCode('selectedHash') has
                try {
                    List<Map> result = new ArrayList<>();
                    qrInventoryController.getAllComments(databaseCallback, selectedHash, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Function used by QrInventoryAddCommentFragment, the fragment passes the argument (String) "comment"
     * to and call this function, then it writes the new arrayList of comments of that qrCode ('selectedHash')
     * for the current player into Firebase.
     * @param comment comment that player just entered
     * @throws Exception
     */
    public void addComment(String comment) throws Exception {
        HashMap<String, HashMap> tHash = new HashMap<>();
        ArrayList<String> tList;

        // add current date to comment
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        comment = "[" + formatter.format(date) + "]" + " " + comment;

        // add the new comment into the original HashMap
        tList = (ArrayList<String>) commentsOfCurQrcode.get(currentPlayer);
        try {
            tList.add(comment);
        } catch(Exception e) {
            tList = new ArrayList<>();
            tList.add(comment);
        }

        commentsOfCurQrcode.put(currentPlayer, tList);

        // update data of Firebase
        tHash.put("Comments", commentsOfCurQrcode);
        qrInventoryController.updateQR((String) currentQR.get("Identifier"), null, null, commentsOfCurQrcode, null);

        deleteButton.setVisibility(GONE);
        commentButton.setVisibility(GONE);
        showCommentsButton.setVisibility(GONE);

    }

    /**
     * If user is done browsing other player reset playerController to owner' of this device
     * Then finish itself so user can head back to MainActivity.
     * @param observable PlayerController
     * @param o QrInventoryActivity
     */
    @Override
    public void update(Observable observable, Object o) {
        finish();
    }
}



