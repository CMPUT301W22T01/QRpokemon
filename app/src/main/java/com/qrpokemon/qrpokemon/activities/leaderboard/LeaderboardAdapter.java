package com.qrpokemon.qrpokemon.activities.leaderboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qrpokemon.qrpokemon.R;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Adapter for leaderboard listview
  */
public class LeaderboardAdapter extends RecyclerView.Adapter implements Observer {
    final private Context context;
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ranking;
        TextView username;
        TextView highestUnique;
        TextView qrQuantity;
        TextView totalScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link views to their variables
            ranking = itemView.findViewById(R.id.leaderboard_item_rank);
            username = itemView.findViewById(R.id.leaderboard_item_username);
            highestUnique = itemView.findViewById(R.id.leaderboard_item_unique);
            qrQuantity = itemView.findViewById(R.id.leaderboard_item_qrcount);
            totalScore = itemView.findViewById(R.id.leaderboard_item_score);
        }

        public void bind(LeaderboardItem leaderboardItem) {
            ranking.setText(String.valueOf(leaderboardItem.getRank()));
            username.setText(leaderboardItem.getUsername());
            highestUnique.setText(String.valueOf(leaderboardItem.getHighestScore()));
            qrQuantity.setText(String.valueOf(leaderboardItem.getQrQuantity()));
            totalScore.setText(String.valueOf(leaderboardItem.getTotalScore()));
        }
    }
}
