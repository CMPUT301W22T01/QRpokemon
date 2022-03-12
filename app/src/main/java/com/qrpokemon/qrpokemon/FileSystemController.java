package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FileSystemController {
    /**
     * reading off from local filesystem
     * @param context is the Context class from Signup Activity class
     * @param filename indicates which file needs to be read off
     * @return null if nothing found, otherwise return string
     */
    public String readToFile(Context context, String filename ) {
        SharedPreferences preferences = context.getSharedPreferences("First run", Activity.MODE_PRIVATE);
        String data = preferences.getString(filename,null);
        return data; //could be null due to first run
    }

    /**
     * Write data to local filesystem, return nothing
     * @param context is the Context class from Signup Activity class
     * @param filename indicates which file needs to save
     * @param data a String that carries username and first run check flag.
     */
    public void writeToFile(Context context, String filename, String data) {
        SharedPreferences preferences = context.getSharedPreferences("First run", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(filename, data);
        editor.commit();
    }

}
