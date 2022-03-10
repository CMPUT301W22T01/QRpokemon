package com.qrpokemon.qrpokemon;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class FileSystemController extends AppCompatActivity {

    public String readToFile(String filename ) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(filename,null);
        Type type = new TypeToken<String>() {}.getType();
        String data = gson.fromJson(json, type);

        return data; //could be null due to first run
    }

    public void writeToFile(String filename, String data) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(filename, json);
        editor.apply();
    }
}
