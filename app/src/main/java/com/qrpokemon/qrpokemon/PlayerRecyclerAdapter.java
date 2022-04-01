package com.qrpokemon.qrpokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder> {

    private List<Map> players;
    private OnItemListener mOnItemListener;

    public PlayerRecyclerAdapter(List<Map> players, OnItemListener onItemListener) {
        this.players = players;
        this.mOnItemListener = onItemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView playerNameTxt;
        private OnItemListener onItemListener;
        private Button delete;

        public ViewHolder (final View view, OnItemListener onItemListener) {
            super(view);

            this.playerNameTxt = view.findViewById(R.id.username);
            this.delete = view.findViewById(R.id.player_delete_btn);
            this.onItemListener = onItemListener;

            this.delete.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.player_delete_btn:
                    onItemListener.onItemClick(getBindingAdapterPosition());
                    break;
            }
        }
    }

    @NonNull
    @Override
    public PlayerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_items, parent, false);

        return new ViewHolder(itemView, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerRecyclerAdapter.ViewHolder holder, int position) {
        String playerName = ((String) players.get(position).get("Identifier"));
        holder.playerNameTxt.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
