package com.qrpokemon.qrpokemon.activities.leaderboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qrpokemon.qrpokemon.R;

public class LeaderboardActivity extends AppCompatActivity {
    // Init variables
    private RecyclerView leaderboardRecyclerView;
    private Spinner sortBy;
    private ImageButton backButton;

    // Leaderboard variables
    LeaderboardController leaderboardController;
    private LeaderboardList leaderboardList;
    private LeaderboardAdapter leaderboardAdapter;
    // User's leaderboard rank
    private TextView playerRank;
    private TextView playerUsername;
    private TextView playerHighestUnique;
    private TextView playerQrCount;
    private TextView playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        // data binding
        leaderboardRecyclerView = findViewById(R.id.rv_leaderboard);
        sortBy = findViewById(R.id.sp_sort_selection);
        backButton = findViewById(R.id.leaderboard_back);

        playerRank = findViewById(R.id.tv_leaderboard_player_rank);
        playerUsername = findViewById(R.id.tv_leaderboard_player_username);
        playerHighestUnique = findViewById(R.id.tv_leaderboard_player_unique);
        playerQrCount = findViewById(R.id.tv_leaderboard_player_qrcount);
        playerScore = findViewById(R.id.tv_leaderboard_player_score);

        // Create and fill our list with player data
        leaderboardList = new LeaderboardList();
        leaderboardController = LeaderboardController.getInstance();

        // set adapter
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardList.getList());
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardList.addObserver(leaderboardAdapter);

        // set spinner for select different sort type
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // i=0 | High Scores (aggregate total score)
                // i=1 | Highest Unique
                // i=2 | Most QR Codes Scanned
                leaderboardController.sortLeaderboard(LeaderboardActivity.this, leaderboardList, (int) id);
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

    public void setPersonalRank(int rank, String username, int highestUnique, int qrCount, int score) {
        playerRank.setText(String.valueOf(rank));
        playerUsername.setText(username);
        playerHighestUnique.setText(String.valueOf(highestUnique));
        playerQrCount.setText(String.valueOf(qrCount));
        playerScore.setText(String.valueOf(score));
    }
}