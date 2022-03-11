package com.qrpokemon.qrpokemon;

import java.util.HashMap;

public class DatabaseController {
    private Database database;
    
    DatabaseController(){
        Database database = new Database();
    }

    public HashMap readFromDatabase(String collection, String objectName) throws Exception{
        return (HashMap) database.getData(collection, objectName);
    }

    public void writeToDatabase(String collection, String objectName, HashMap data, Boolean overwrite) throws Exception{
        database.writeData(collection, objectName, data, overwrite);
    }
}
