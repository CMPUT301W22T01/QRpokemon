package com.qrpokemon.qrpokemon.activities.qrinventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;

public class QrInventoryCustomList extends ArrayAdapter<String> {

    private ArrayList<String> qrInventoryDataList;
    private Context context;

    public QrInventoryCustomList(Context context, ArrayList<String> qrInventoryDataList) {
        super(context, 0, qrInventoryDataList);
        this.qrInventoryDataList = qrInventoryDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.qr_inventory_list_content, parent,false);
        }

        String hashAndScore = qrInventoryDataList.get(position);

        // Split string by space ' '
        String cut = " ";
        String[] tStr = hashAndScore.split(cut);

        // findView
        TextView hashTV = view.findViewById(R.id.tv_hash_code);
        TextView scoreTV = view.findViewById(R.id.tv_hash_score);
        ImageView codeIV = view.findViewById(R.id.imageView);

        // setText
        hashTV.setText(tStr[1].substring(0, 16) + "...");
        scoreTV.setText("Score: " + tStr[0]);

        // setImageBitmap
        String encodedString = tStr[2];
        Bitmap bitmap = StringToBitMap(encodedString);
        codeIV.setImageBitmap(bitmap);

        return view;
    }

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
