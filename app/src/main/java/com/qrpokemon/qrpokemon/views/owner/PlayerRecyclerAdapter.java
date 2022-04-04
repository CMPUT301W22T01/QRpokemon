package com.qrpokemon.qrpokemon.views.owner;

import static android.view.View.INVISIBLE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder> {

    private Boolean isPlayer;
    private List<Map> items;
    private OnItemListener mOnItemListener;

    final private String TAG = "PlayerRecyclerAdapter";

    public PlayerRecyclerAdapter(List<Map> items, Boolean isPlayer, OnItemListener onItemListener) {
        this.items = items;
        this.isPlayer = isPlayer;
        this.mOnItemListener = onItemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemNameTxt;
        private ImageView imageOfQrcode;
        private OnItemListener onItemListener;
        private Button delete;

        /**
         * Hold information on different area in one Recycler view item
         * @param view current View
         * @param onItemListener Listener for items clicked
         */
        public ViewHolder (final View view, OnItemListener onItemListener) {
            super(view);

            this.itemNameTxt = view.findViewById(R.id.username);
            this.delete = view.findViewById(R.id.player_delete_btn);
            this.onItemListener = onItemListener;
            this.imageOfQrcode = view.findViewById(R.id.iv_admin_image);

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

    /**
     * The holder that binds the element that being hold, it has it's index on Recycler view
     * @param holder A ViewHolder object
     * @param position The position of this item in Recycler view
     */
    @Override
    public void onBindViewHolder(@NonNull PlayerRecyclerAdapter.ViewHolder holder, int position) {

        if (isPlayer) { // if the user "click" player button

            String itemName = ((String) items.get(position).get("Identifier"));
            holder.itemNameTxt.setText(itemName);
            holder.imageOfQrcode.setVisibility(INVISIBLE);


        } else { // if the user click "code" button

            try {
                String itemName = ((String) items.get(position).get("Content"));
                holder.itemNameTxt.setText(itemName);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error from 'clicking code', 'get content'");
            }

            try {
                HashMap tMap = (HashMap) items.get(position).get("Bitmap");

                // get all the key(player id) of the current qrCode
                String[] tKeys;
                ArrayList<String> playerList = new ArrayList<>();
                tKeys = (String[]) tMap.keySet().toArray(new String[0]);

                String bitmapString = (String) tMap.get(tKeys[0]);
                Bitmap bitmap = StringToBitMap(bitmapString);
                holder.imageOfQrcode.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error from 'clicking code', 'get bitmap'");
            }
        }
    }

    /**
     * Count the total number of elements in this view
     * @return size of the items in view
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

    /**
     * convert string to Bitmap image for display
     * @param encodedString bitmap string
     * @return Bitmap image
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
