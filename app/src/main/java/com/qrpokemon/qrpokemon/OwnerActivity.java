package com.qrpokemon.qrpokemon;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qrpokemon.qrpokemon.controllers.DatabaseCallback;
import com.qrpokemon.qrpokemon.controllers.PlayerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerActivity extends AppCompatActivity implements PlayerRecyclerAdapter.OnItemListener {
    private PlayerController playerController = PlayerController.getInstance();
    private RecyclerView recyclerView;
    private List<Map> players;
    private PlayerRecyclerAdapter radapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity);
        recyclerView = findViewById(R.id.owner_recycler);

        DatabaseCallback databaseCallback = new DatabaseCallback(this) {
            @Override
            public void run(List<Map> dataList) {
                if (!dataList.isEmpty()) {//get all players in this game:
                    players = dataList;
                    setAdapter(dataList);

                }
            }
        };
        playerController.getAllPlayer(databaseCallback, new ArrayList<>());

    }

    private void setAdapter(List<Map> players) {
        radapter = new PlayerRecyclerAdapter(players, this);
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

        Log.e("OwnerActivity: ",players.get(position).toString());
        try {
            playerController.deletePlayer((String)players.get(position).get("Identifier"));
            Toast.makeText(this, players.get(position).get("Identifier").toString() + " is deleted",Toast.LENGTH_SHORT).show();
            players.remove(position);
            radapter.notifyItemRemoved(position);
            radapter.notifyItemRangeChanged(position, players.size());
        } catch (Exception e) { // if user doesn't exist in database:
            e.printStackTrace();
            Toast.makeText(this, players.get(position).get("Identifier").toString() + " doesn't exist",Toast.LENGTH_SHORT).show();
        }
    }
}
