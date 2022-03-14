package com.qrpokemon.qrpokemon;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class QrInventoryActivity extends AppCompatActivity {

    HashMap listOfPlayerData;
    ListView qrInventoryList;
    ArrayList<String> qrInventoryDataList;
    ArrayAdapter<String> qrInventoryDataAdapter;

    QrInventoryController qrInventoryController = QrInventoryController.getInstance();
    private String currentPlayer = null;
    final private String TAG = "QrInventoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_inventory);

        // 得到所有的对应的player document的信息
        try {
            listOfPlayerData = qrInventoryController.getPlayerInfo(currentPlayer);
            Log.e(TAG, "ddd" + listOfPlayerData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        // 到此为止， listOfPlayData里面包含了所有数据库中关于此player的信息，本身是一个hashMap
        qrInventoryList = findViewById(R.id.QR_inventory_list);

        // Step1 得到该用户所有拥有的qrhash
        qrInventoryDataList = (ArrayList<String>) listOfPlayerData.get("qrInventory");
        Log.e(TAG, qrInventoryDataList.toString());

        qrInventoryDataAdapter = new QrInventoryCustomList(this, qrInventoryDataList);
        qrInventoryList.setAdapter(qrInventoryDataAdapter);


        // 接下的部分可以得到某一个特定qr hash的所有信息

//        try {
//            qrInventoryController.getQrCode(this, "wobuxihuanchijiba");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
