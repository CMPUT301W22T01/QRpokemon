package com.qrpokemon.qrpokemon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardActivity extends AppCompatActivity {
    // Init variables
    private ListView leaderboardListView;
    private Spinner sortBy;
    private ImageButton backButton;
    private int sortMethod = 0;

    // Leaderboard variables
    LeaderboardController leaderboardController;
    private LeaderboardList leaderboardList;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        // data binding
        leaderboardListView = findViewById(R.id.leaderboard_listview);
        sortBy = findViewById(R.id.sp_sort_selection);
        backButton = findViewById(R.id.leaderboard_back);

        // Create and fill our list with player data
        leaderboardList = new LeaderboardList();
        leaderboardController = LeaderboardController.getInstance();
        leaderboardController.getLeaderboard(this, leaderboardList);

        // set adapter
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardList.getList());
        leaderboardListView.setAdapter(leaderboardAdapter);
        leaderboardList.addObserver(leaderboardAdapter);

        // set spinner for select different sort type
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 0 = i = sortMethod( one Qrcode score)
                // 1 = i = sortMethod( total Qrcode score)
                // 2 = i = sortMethod( numbers of  Qrcode score)
                sortMethod = i;  // FIXME: This assignment is local scoped
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // set click event for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Return to calling activity
            }
        });
    }
}