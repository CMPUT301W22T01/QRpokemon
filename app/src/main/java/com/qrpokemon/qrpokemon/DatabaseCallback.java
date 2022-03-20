package com.qrpokemon.qrpokemon;

import android.content.Context;

import java.util.List;
import java.util.Map;

public abstract class DatabaseCallback {
    private Context context;

    public DatabaseCallback(Context context) {
        context = context;
    }

    public abstract void run(List<Map> dataList);
}
