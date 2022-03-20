package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Adapter for leaderboard listview
  */
public class LeaderboardAdapter extends RecyclerView.Adapter implements Observer {
    private Context context;
    private List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(Context context, List<LeaderboardItem> leaderboardList) {
        this.context = context;
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.leaderboard_list,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind data to ViewHolder
        LeaderboardItem leaderboardItem = leaderboardList.get(position);
        ((ViewHolder) holder).bind(leaderboardItem);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof LeaderboardList)
            // Update the list
            leaderboardList = ((LeaderboardList) observable).getList();

        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankOrderList;
        TextView userNameList;
        TextView highestScoreList;
        TextView qrNumList;
        TextView totalScoreList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link views to their variables
            rankOrderList = itemView.findViewById(R.id.rand_order_list);
            userNameList = itemView.findViewById(R.id.username_list);
            highestScoreList = itemView.findViewById(R.id.highestscore_list);
            qrNumList = itemView.findViewById(R.id.qr_quantity_list);
            totalScoreList = itemView.findViewById(R.id.total_score_list);
        }

        public void bind(LeaderboardItem leaderboardItem) {
            rankOrderList.setText(String.valueOf(leaderboardItem.getRank()));
            userNameList.setText(leaderboardItem.getUsername());
            highestScoreList.setText(String.valueOf(leaderboardItem.getHighestScore()));
            qrNumList.setText(String.valueOf(leaderboardItem.getQrQuantity()));
            totalScoreList.setText(String.valueOf(leaderboardItem.getTotalScore()));
        }
    }
}
