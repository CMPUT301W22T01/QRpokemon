package com.qrpokemon.qrpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String,String> data = new HashMap<>();
        data.put("Ego", "Huge");
        Database database = new Database();
        database.writeData("Mjhunag", data);

//        database.deleteData("Mjhuang");
    }
}