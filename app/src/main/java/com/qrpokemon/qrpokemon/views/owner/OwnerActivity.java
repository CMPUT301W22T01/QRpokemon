package com.qrpokemon.qrpokemon.views.owner;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.R;
import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.controllers.QrCodeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class OwnerActivity extends AppCompatActivity implements PlayerRecyclerAdapter.OnItemListener, Observer {
    private PlayerController playerController = PlayerController.getInstance();
    private QrCodeController qrCodeController = QrCodeController.getInstance();
    private RecyclerView recyclerView;
    private List<Map> items;
    private PlayerRecyclerAdapter radapter;
    private Button playerBtn, codeBtn;
    private FloatingActionButton backBtn;
    private Boolean isPlayer;
    private Integer highest = 0;
    private HashMap selectedQr;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity);
        recyclerView = findViewById(R.id.owner_recycler);
        codeBtn = findViewById(R.id.code_button);
        playerBtn = findViewById(R.id.player_button);
        backBtn = findViewById(R.id.owner_backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        playerBtn.setOnClickListener(new View.OnClickListener() { //if user choose to delete from player collection:
            @Override
            public void onClick(View view) {
                isPlayer = true;
                DatabaseCallback databaseCallback = new DatabaseCallback(OwnerActivity.this) {
                    @Override
                    public void run(List<Map> dataList) {
                        if (!dataList.isEmpty()) {//get all players in this game:
                            items = dataList;
                            setAdapter(dataList);
                        }
                    }
                };
                playerController.getAllPlayers(databaseCallback, new ArrayList<>());
            }
        });


        codeBtn.setOnClickListener(new View.OnClickListener() {//if user choose to delete from qr code collection:
            @Override
            public void onClick(View view) {
                isPlayer = false;
                DatabaseCallback databaseCallback = new DatabaseCallback(OwnerActivity.this) {
                    @Override
                    public void run(List<Map> dataList) {
                        if (!dataList.isEmpty()) {//get all players in this game:
                            items = dataList;
                            setAdapter(dataList);
                        }
                    }
                };
                try {
                    qrCodeController.getQR(databaseCallback, new ArrayList<>(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    /**
     * Set items to be displayed for owner on Recycler view
     * @param players Items that will be displayed on Recycler view
     */
    private void setAdapter(List<Map> players) {
        radapter = new PlayerRecyclerAdapter(players, isPlayer, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(radapter);
    }

    /**
     * User/Qrcode gets deleted once the button beside username is clicked in OwnerActivity
     * @param position the position of player to be deleted from Game
     */
    @Override
    public void onItemClick(int position) {

        try {
            if (((String) items.get(position).get("Identifier")).equals(playerController.getPlayer(null,null,null,null).get("Identifier"))){ //if user chooses to delete itself:
                Toast.makeText(this, "You cannot delete yourself",Toast.LENGTH_SHORT).show();
            } else {
                if (isPlayer) {
                    playerController.deletePlayer((String)items.get(position).get("Identifier"));

                } else { //deleting an QRcode:

                    selectedQr = (HashMap) items.get(position);

                    DatabaseCallback databaseCallback = new DatabaseCallback(this) {
                        @Override
                        public void run(List<Map> dataList) {
                            if (!dataList.isEmpty()) { // if there are players:
                                for (Map player : dataList) { // iterate each player has involved with this qrcode:

                                    ArrayList<String> qrInventory = (ArrayList<String>) player.get("qrInventory");
                                    if (qrInventory.contains((String) selectedQr.get("Identifier"))){ // if qrcode to be deleted is in this player's qrInventory:
                                        Log.e("Player has this qrcode ", (String) player.get("Identifier") + " " + player.get("qrInventory").toString());
                                        //qrInvnetory
                                        qrInventory.remove((String)selectedQr.get("Identifier"));


                                        //highest unique
                                        if ((Long) selectedQr.get("Score") - (Long) player.get("highestUnique") == 0){ // save highest value here:
                                            Log.e("This qrcode is player's highest score ", (String) player.get("Identifier") + " " + player.get("qrInventory").toString());
                                            highest = 0;
                                            DatabaseCallback databaseCallback1 = new DatabaseCallback(OwnerActivity.this) {
                                                @Override
                                                public void run(List<Map> dataList) {
                                                    Log.e("OwnerActivity: ", "Searching qr code's highest score among qr Inventory: ");
                                                    if (!dataList.isEmpty()){
                                                        Log.e("OwnerActivity ", "dataList is empty!");
                                                        if (((Long)dataList.get(dataList.size()-1).get("Score")).intValue() > highest){
                                                            highest = ((Long)dataList.get(dataList.size()-1).get("Score")).intValue(); // update new highest score
                                                        }
                                                    }

                                                    try {
                                                        Log.e("OwnerActivity ","Highest Score is " + String.valueOf(highest));
                                                        Integer total = Integer.valueOf(String.valueOf((Long) player.get("totalScore")));
                                                        playerController.saveOtherPlayerData( ((Long)player.get("qrCount")).intValue() -1,
                                                                total - ((Long)selectedQr.get("Score")).intValue(),
                                                                qrInventory,
                                                                (HashMap) player.get("contact"),
                                                                highest,
                                                                (String) player.get("DeviceId"),
                                                                (Boolean) player.get("Owner"),
                                                                (String) player.get("Identifier"),
                                                                true);


                                                        //check if it's currentplayer, if true, updates locally
                                                        HashMap currentPlayer = playerController.getPlayer(null,null,null,null);
                                                        if (((String) player.get("Identifier")).equals((String)player.get("Identifier"))) {
                                                            playerController.setupPlayer((String) currentPlayer.get("Identifier"),
                                                                    qrInventory,
                                                                    (HashMap) currentPlayer.get("contact"),
                                                                    ((Long)player.get("qrCount")).intValue() -1,
                                                                    total - ((Long)selectedQr.get("Score")).intValue(),
                                                                    highest,
                                                                    (Boolean) currentPlayer.get("Owner"),
                                                                    Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            try {
                                                Log.e("check if this qrcode is player's only qr ", (String) player.get("Identifier") + " " + player.get("qrInventory").toString());
                                                if (qrInventory.isEmpty()) { // if qrInventory is empty already:
                                                    Log.e("OwnerActivity: ", "player is: " + player.get("Identifier").toString() +" with qrInventory: " + player.get("qrInventory").toString());
                                                    try {
                                                        Integer total = Integer.valueOf(String.valueOf((Long) player.get("totalScore")));
                                                        playerController.saveOtherPlayerData( ((Long)player.get("qrCount")).intValue() -1,
                                                                total - ((Long)selectedQr.get("Score")).intValue(),
                                                                qrInventory,
                                                                (HashMap) player.get("contact"),
                                                                0,
                                                                (String) player.get("DeviceId"),
                                                                (Boolean) player.get("Owner"),
                                                                (String) player.get("Identifier"),
                                                                true);

                                                        //check if it's currentplayer, if true, updates locally
                                                        HashMap currentPlayer = playerController.getPlayer(null,null,null,null);
                                                        if (((String) player.get("Identifier")).equals((String) currentPlayer.get("Identifier"))) {
//
                                                            playerController.addObserver(OwnerActivity.this);
                                                            playerController.savePlayerData( ((Long)player.get("qrCount")).intValue() -1,
                                                                    total - ((Long)selectedQr.get("Score")).intValue(),
                                                                    qrInventory,
                                                                    null,
                                                                    0,
                                                                    null,
                                                                    null,
                                                                    false);
//                                                            currentPlayer = playerController.getPlayer(null,null,null,null);
//                                                            Log.e("OwnerActivity: ", "Local player is: " + currentPlayer.toString());
                                                        }


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                for (String hash : qrInventory) {
                                                    qrCodeController.getQR(databaseCallback1, new ArrayList<>(),hash);
                                                    Log.e("OwnerActivity", "Changing the highest unique");
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else { // if this is not highest scored qr for this player:
                                            Log.e("Player doesn't have this qr as highest qr: ", player.get("Identifier") + " " + qrInventory.toString() );
                                            // save total qrInventroy and count only
                                            Integer total = Integer.valueOf(String.valueOf((Long) player.get("totalScore")));
                                            try {
                                                playerController.saveOtherPlayerData( ((Long)player.get("qrCount")).intValue() -1,
                                                        total - ((Long)selectedQr.get("Score")).intValue(),
                                                        qrInventory,
                                                        (HashMap) player.get("contact"),
                                                        ((Long) player.get("highestUnique")).intValue(),
                                                        (String) player.get("DeviceId"),
                                                        (Boolean) player.get("Owner"),
                                                        (String) player.get("Identifier"),
                                                        true);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    playerController.getAllPlayers(databaseCallback, new ArrayList<>());

                    qrCodeController.deleteQr((String)items.get(position).get("Identifier"));
                }

                Toast.makeText(this, items.get(position).get("Identifier").toString() + " is deleted",Toast.LENGTH_SHORT).show();
                items.remove(position);
                radapter.notifyItemRemoved(position);
                radapter.notifyItemRangeChanged(position, items.size());
            }

        } catch (Exception e) { // if user doesn't exist in database:
            e.printStackTrace();
            Toast.makeText(this, items.get(position).get("Identifier").toString() + " doesn't exist",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap currentPlayer = null;
        try {
            currentPlayer = playerController.getPlayer(null,null,null,null);
            Log.e("OwnerActivity: ", "Local player is: " + currentPlayer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
