package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Adapter for leaderboard listview
  */
public class LeaderboardAdapter extends BaseAdapter implements Observer {
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
        userNameList.setText(String.valueOf(leaderboardList.get(i).getUsername()));
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
