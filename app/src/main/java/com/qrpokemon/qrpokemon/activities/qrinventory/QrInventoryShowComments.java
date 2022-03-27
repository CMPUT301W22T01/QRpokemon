package com.qrpokemon.qrpokemon.activities.qrinventory;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qrpokemon.qrpokemon.R;

import java.util.HashMap;

public class QrInventoryShowComments extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory_show_comments);

        HashMap commentsOfCurQrcode = (HashMap) getIntent().getExtras().get("commentsOfCurQrcode");
        Log.e("QrInventoryShowComments: ", "get comments of " + commentsOfCurQrcode.toString());
    }
}
