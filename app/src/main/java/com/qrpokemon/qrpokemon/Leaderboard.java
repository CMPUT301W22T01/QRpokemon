package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // init variables
    private ListView leaderboardListView;
    private Spinner sortBy;
    private ImageButton backButton;
    private List<LeaderboardItem> leaderboardList = new ArrayList<>();
    private LeaderboardAdapter leaderboardAdapter;
    private int sortType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        // data binding
        leaderboardListView = findViewById(R.id.leaderboard_listview);
        sortBy = findViewById(R.id.sp_sort_selection);
        backButton = findViewById(R.id.leaderboard_back);

        // set adapter
        leaderboardAdapter = new LeaderboardAdapter(Leaderboard.this, leaderboardList);
        leaderboardListView.setAdapter(leaderboardAdapter);

        // set spinner for select different sort type
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 0 = i = sortType( one Qrcode score)
                // 1 = i = sortType( total Qrcode score)
                // 2 = i = sortType( numbers of  Qrcode score)
                sortType = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // get data to the list





        
//        LeaderboardItem item1 = new LeaderboardItem(2,"xml", 7,8,9);
//        LeaderboardItem item2 = new LeaderboardItem(3,"xml", 5,8,9);
//        leaderboardList.add(item1);
//        leaderboardList.add(item2);





        // set click event for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Leaderboard.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}

// create object for each item in leaderboard
class LeaderboardItem{
    private int rank;
    private String userName;
    private int highestScore;
    private int qrQuantity;
    private int totalScore;

    public LeaderboardItem(int rank, String userName, int highestScore, int qrQuantity, int totalScore) {
        this.rank = rank;
        this.userName = userName;
        this.highestScore = highestScore;
        this.qrQuantity = qrQuantity;
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getQrQuantity() {
        return qrQuantity;
    }

    public void setQrQuantity(int qrQuantity) {
        this.qrQuantity = qrQuantity;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}

// set adapter for leaderboard listview
class LeaderboardAdapter extends BaseAdapter{
    private Context context;
    private List<LeaderboardItem> leaderboardItems;

    public LeaderboardAdapter(Context context, List<LeaderboardItem> leaderboardItem) {
        this.context = context;
        this.leaderboardItems = leaderboardItem;
    }


    @Override
    public int getCount() {
        return leaderboardItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = View.inflate(context, R.layout.leaderboard_list,null);

        TextView rankOrderList = view1.findViewById(R.id.rand_order_list);
        TextView userNameList = view1.findViewById(R.id.username_list);
        TextView highestScoreList = view1.findViewById(R.id.highestscore_list);
        TextView qrNumList = view1.findViewById(R.id.qr_quantity_list);
        TextView totalScoreList = view1.findViewById(R.id.total_score_list);

        String a = String.valueOf(22);

        rankOrderList.setText(String.valueOf(leaderboardItems.get(i).getRank()));
        userNameList.setText(String.valueOf(leaderboardItems.get(i).getUserName()));
        highestScoreList.setText(String.valueOf(leaderboardItems.get(i).getHighestScore()));
        qrNumList.setText(String.valueOf(leaderboardItems.get(i).getQrQuantity()));
        totalScoreList.setText(String.valueOf(leaderboardItems.get(i).getTotalScore()));

        return view1;
    }
}


