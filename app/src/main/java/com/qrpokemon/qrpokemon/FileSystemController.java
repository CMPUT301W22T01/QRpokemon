package com.qrpokemon.qrpokemon;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FileSystemController {

    public String readToFile(Context context, String filename ) {
        SharedPreferences preferences = context.getSharedPreferences("First run", Activity.MODE_PRIVATE);
        String data = preferences.getString(filename,null);
        return data; //could be null due to first run
    }

    public void writeToFile(Context context, String filename, String data) {
        SharedPreferences preferences = context.getSharedPreferences("First run", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(filename, data);
        editor.commit();
    }

}
