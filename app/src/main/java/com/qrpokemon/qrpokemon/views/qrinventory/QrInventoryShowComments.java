package com.qrpokemon.qrpokemon.views.qrinventory;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QrInventoryShowComments extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> playerList;
    private String selectedBitmap;
    private FloatingActionButton backButton;
    private ImageView codeImage;
    private ExpandableListView commentsOfPlayers;
    private QrInventoryShowCommentsCustomAdapter commentsOfPlayersAdapter;
    private HashMap commentsOfCurQrcode;
    final private String TAG = "QrInventoryShowComments";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory_show_comments);

        codeImage = findViewById(R.id.iv_show_comments);
        backButton = findViewById(R.id.bt_back_show_comments);
        commentsOfPlayers = findViewById(R.id.el_show_comments);

        backButton.setOnClickListener(this);

        // get all the comments of the current selected qrCode
        commentsOfCurQrcode = (HashMap) getIntent().getExtras().get("commentsOfCurQrcode");
        Log.e(TAG, "get comments of " + commentsOfCurQrcode.toString());

        // get and set the image of the selected qrCode
        selectedBitmap = (String) getIntent().getExtras().get("selectedBitmap");
        codeImage.setImageBitmap(StringToBitMap(selectedBitmap));

        // set the ExpandableListView
        String[] tKeys;
        playerList = new ArrayList<>();
        tKeys = (String[]) commentsOfCurQrcode.keySet().toArray(new String[0]);

        for (String player : tKeys) {
            playerList.add(player);
        }

        commentsOfPlayersAdapter = new QrInventoryShowCommentsCustomAdapter(playerList, commentsOfCurQrcode);
        commentsOfPlayers.setAdapter(commentsOfPlayersAdapter);

    }

    @Override
    public void onClick(View view) {
        // only 1 button for going back
        finish();
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
