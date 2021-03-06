package com.qrpokemon.qrpokemon.views.qrinventory;

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
import com.qrpokemon.qrpokemon.controllers.PlayerController;

import java.util.ArrayList;
import java.util.HashMap;

public class QrInventoryCustomList extends ArrayAdapter<String> {

    private ArrayList<String> qrInventoryDataList;
    private Context context;

    final private String TAG = "QrInventoryCustomList";

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
        TextView scannedTv = view.findViewById(R.id.tv_has_scanned);
        TextView hashTV = view.findViewById(R.id.tv_hash_code);
        TextView scoreTV = view.findViewById(R.id.tv_hash_score);
        ImageView codeIV = view.findViewById(R.id.imageView);

        // set score
        scoreTV.setText("Score: " + tStr[0]);

        // set text (hash / content)
        if (tStr.length < 5){

            // if code doesn't have content, show qrHash
            if (tStr[1].length() > 16) {
                hashTV.setText(tStr[1].substring(0, 15) + "...");
            } else {
                hashTV.setText(tStr[1]);
            }

            // if code has content, show content
        } else {
            String temp = "";
            for (int i = 4; i < tStr.length; i++) {
                temp += tStr[i] + " ";
            }
            if (temp.length() > 16) {
                hashTV.setText(temp.substring(0, 15) + "...");
            } else {
                hashTV.setText(temp);
            }
        }

        // setImageBitmap
        String encodedString = ""; // user has no photo in default
        if (tStr[2] != "null") { // if user saves a photo, display his/her photo:
            encodedString = tStr[2];
        }

        Bitmap bitmap = StringToBitMap(encodedString);
        codeIV.setImageBitmap(bitmap);

        // set text that if it's also scanned by others
        try {
            if (Integer.valueOf(tStr[3]) > 1) {
                scannedTv.setText("This code was also scanned by others!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    /**
     * Coverts a string to a bitmap
     *
     * @param encodedString the String needed to be converted to a bitmap
     * @return a bit map if no exception, else null
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
