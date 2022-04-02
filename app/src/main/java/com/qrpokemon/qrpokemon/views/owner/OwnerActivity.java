package com.qrpokemon.qrpokemon.views.owner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qrpokemon.qrpokemon.R;
import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.controllers.QrCodeController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerActivity extends AppCompatActivity implements PlayerRecyclerAdapter.OnItemListener {
    private PlayerController playerController = PlayerController.getInstance();
    private QrCodeController qrCodeController = QrCodeController.getInstance();
    private RecyclerView recyclerView;
    private List<Map> items;
    private PlayerRecyclerAdapter radapter;
    private Button playerBtn, codeBtn, backBtn;
    private Boolean isPlayer;
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
                playerController.getAllPlayer(databaseCallback, new ArrayList<>());
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

    private void setAdapter(List<Map> players) {
        radapter = new PlayerRecyclerAdapter(players, isPlayer, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(radapter);
    }

    /**
     * User gets deleted once the button beside username is clicked in OwnerActivity
     * @param position the position of player to be deleted from Game
     */
    @Override
    public void onItemClick(int position) {

        Log.e("OwnerActivity: ",items.get(position).toString());
        try {
            if (((String) items.get(position).get("Identifier")).equals(playerController.getPlayer(null,null,null,null).get("Identifier"))){ //if user chooses to delete itself:
                Toast.makeText(this, "You cannot delete yourself",Toast.LENGTH_SHORT).show();
            } else {
                if (isPlayer) {
                    playerController.deletePlayer((String)items.get(position).get("Identifier"));
                } else {
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
}
