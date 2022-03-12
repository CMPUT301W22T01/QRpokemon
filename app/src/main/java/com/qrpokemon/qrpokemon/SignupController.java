package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupController {
    private PlayerController playerController;
    private FileSystemController fileSystemController = new FileSystemController();
    private DatabaseController databaseController = new DatabaseController();

    public Boolean validateUsername(String userName){ //check username from database
        List<Map> result = null;
        try{
            // read data from player
            result = databaseController.readFromDatabase("Player", userName);
            Log.e("SignupController: ", result.toString() + "result");
        } catch (Exception e) { //collection is invalid
            e.printStackTrace();
            Log.e("SignupController: ", e.toString());
        }
        return result.isEmpty();
    }

    public PlayerController addNewPlayer(Context context, String newUsername, @Nullable String email, @Nullable String phone) throws Exception {
        Boolean result = validateUsername(newUsername);
        if (result){ // if user name is valid, create a new Player

            HashMap<String, String> contact = new HashMap<>();
            contact.put("email", email);
            contact.put("phone", phone);
            Player newPlayer = new Player(newUsername, null, contact, 0,0);
            PlayerController playerController = new PlayerController(newPlayer); //gives current player to PlayerController

            //update local fileSystem
//            fileSystemController.writeToFile(context, "name", newUsername);
            return playerController;
        }

        else{
            throw new Exception("Username is not unique!");
        }
    }

    /**
     * load data from local based on the pass in filename, file "firstRun" and file "name" indicates current user name
     * @param filename indicates which file is reading of from local
     * @return
     */
    public String load(Context context, String filename){
        String data = fileSystemController.readToFile(context, filename);
        return data;
    }

    public void write(Context context, String filename){
//        fileSystemController.writeToFile(context, filename, "firstRun");
    }
}
