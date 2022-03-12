package com.qrpokemon.qrpokemon;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseController {
    private Database database;
    
    DatabaseController(){
        database = Database.getInstance();
    }

    public List<Map> readFromDatabase(String collection, String objectName) throws Exception{
//        List<Map> temp = database.getData(collection, objectName);
//        Log.e("DatabaseController: ", temp.toString());
//        return temp;
        return null;
    }

    public void writeToDatabase(String collection, String objectName, HashMap data, Boolean overwrite) throws Exception{
        this.database.writeData(collection, objectName, data, overwrite);
    }

    public void deleteFromDatabase(String collection, String objectName) throws Exception {
        this.database.deleteData(collection, objectName);
    }
}
