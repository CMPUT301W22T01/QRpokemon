package com.qrpokemon.qrpokemon;

import android.app.AppComponentFactory;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    private ListView leaderboardList;
    private ArrayAdapter<String> leaderboardAdapter;
    private ArrayList<String> leaderboardDataList;
    private Spinner sortBy;
    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        init();
        init_click();
    }

    private void init(){
        leaderboardList = findViewById(R.id.leaderboard_listview);
        sortBy = findViewById(R.id.sp_sort_selection);
        backButton = findViewById(R.id.leaderboard_back);

        leaderboardDataList = new ArrayList<>();

//        leaderboardAdapter = new ArrayAdapter<>(this, R.id.leaderboard_list, leaderboardDataList);
    }

    private void init_click(){

        // set button back to main activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Leaderboard.this, MainActivity.class);
            }
        });


        // set spinner
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // select different order to get different leaderboard

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



}

