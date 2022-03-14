package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class LeaderboardActivity extends AppCompatActivity {
    // Init variables
    LeaderboardController leaderboardController;
    private ListView leaderboardListView;
    private Spinner sortBy;
    private ImageButton backButton;
    private LeaderboardList leaderboardList;
    private LeaderboardAdapter leaderboardAdapter;
    private int sortType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        // TODO: Move controller functions into controller class
        // data binding
        leaderboardListView = findViewById(R.id.leaderboard_listview);
        sortBy = findViewById(R.id.sp_sort_selection);
        backButton = findViewById(R.id.leaderboard_back);

        // Create and fill our list with player data
        leaderboardList = new LeaderboardList();
        leaderboardController = LeaderboardController.getInstance();
        leaderboardController.getLeaderboard(this, leaderboardList);

        // set adapter
        // TODO: Implement observer for list
        // TODO: Get database to sort for us?
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardList.getList());
        leaderboardListView.setAdapter(leaderboardAdapter);
        leaderboardList.addObserver(leaderboardAdapter);

        // set spinner for select different sort type
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: Use dropdown text in switch statement?
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
                finish();
            }
        });
    }
}

// TODO: Delete if unused (or use like a struct)
// create object for each item in leaderboard
class LeaderboardItem {
    private int rank;
    private String username;
    private int highestScore;
    private long qrCount;
    private long totalScore;

    public LeaderboardItem(String username, long qrCount, long totalScore) {
        this.username = username;
        this.highestScore = highestScore;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
    }

    // TODO: Remove unused getters and setters
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    // TODO: Remove since we should just do another database fetch in this case
    public long getQrQuantity() {
        return qrCount;
    }

    public void setQrQuantity(int qrQuantity) {
        this.qrCount = qrQuantity;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}

class LeaderboardList extends Observable {
    private ArrayList<LeaderboardItem> list;

    LeaderboardList() {
        list = new ArrayList<>();
    }

    public void add(LeaderboardItem leaderboardItem) {
        list.add(leaderboardItem);
    }

    public void notifyListUpdate() {
        setChanged();
        notifyObservers();
    }

    public List<LeaderboardItem> getList() {
        return list;
    }
}

// set adapter for leaderboard listview
class LeaderboardAdapter extends BaseAdapter implements Observer {
    private Context context;
    private List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(Context context, List<LeaderboardItem> leaderboardList) {
        this.context = context;
        this.leaderboardList = leaderboardList;
    }

    @Override
    public int getCount() {
        return leaderboardList.size();
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

        rankOrderList.setText(String.valueOf(leaderboardList.get(i).getRank()));
        userNameList.setText(String.valueOf(leaderboardList.get(i).getUserName()));
        highestScoreList.setText(String.valueOf(leaderboardList.get(i).getHighestScore()));
        qrNumList.setText(String.valueOf(leaderboardList.get(i).getQrQuantity()));
        totalScoreList.setText(String.valueOf(leaderboardList.get(i).getTotalScore()));

        return view1;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof LeaderboardList)
            // Update the list
            leaderboardList = ((LeaderboardList) observable).getList();

        this.notifyDataSetChanged();
    }
}


