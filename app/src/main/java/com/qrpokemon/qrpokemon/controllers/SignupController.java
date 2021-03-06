package com.qrpokemon.qrpokemon.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

//import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupController {
    private static SignupController currentInstance;
    private SignupController() {}
    private PlayerController playerController = PlayerController.getInstance();

    public static SignupController getInstance() {
        if (currentInstance == null)
            currentInstance = new SignupController();

        return currentInstance;
    }

    /**
     * The device id will be checked first
     * It asks PlayerController to create a current Player class
     * It asks PlayerController to add new player to database
     * @param context from Activity passed in
     * @param newUsername New player's name
     * @param email player entered email
     * @param phone player entered phone
     * @param id player's DeviceId
     * @throws Exception if collection not in correct range
     */
    public void addNewPlayer(Context context, String newUsername, @Nullable String email, @Nullable String phone, String id) throws Exception {
        List<Map> result = new ArrayList<Map>();

        try{
            DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                //this runs after datalist is collected from DatabaseProxy class
                @Override
                public void run(List<Map> dataList) {
                    if (dataList.isEmpty()){
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("email", email);
                        contact.put("phone", phone);

                        try {
                            //Create current Player class in PlayerController class
                            playerController.setupPlayer(newUsername, new ArrayList<String>(), contact, 0,0, 0,false ,id);

                            // add player on firestore
                            playerController.savePlayerData(0,0, new ArrayList<String>(), contact, 0, id,false ,true);
                        } catch (Exception e) {
                            Log.e("SignupController: ", e.toString());
                        }
//                        Log.e("DatabaseProxy: ", "addNewPlayer");
                    } else{
//                        EditText email = (EditText) ((Activity) context).findViewById(R.id.et_email);
                        Toast.makeText(context, "Username is not unique!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            playerController.getPlayer(databaseCallback, result, newUsername, "Identifier");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoke PlayerController to get player with DeviceId given
     * @param databaseCallback Callback function for DatabaseProxy
     * @param result Result List(Map), contains player's info
     * @param id DeviceId
     * @throws Exception if collection name is wrong inside PlayerController class.
     */
    public void load(DatabaseCallback databaseCallback,List<Map> result, String id) throws Exception {
        playerController.getPlayer(databaseCallback, result, id, "DeviceId");
    }

    /**
     * update a player on local and database on startup
     * @param username username of current player
     * @param qrInventory Inventory(ArrayList(String)) of current player
     * @param contact contact info (HashMap) of current player
     * @param qrCount Integer of total count of current player
     * @param totalScore Integer that represents total score current player
     * @param highestUnique Integer that represents highest score of a unique qrcode
     * @param owner A boolean variable indicates if this player is the owner
     * @param id DeviceId of current player
     */
    public void write(String username, ArrayList<String> qrInventory, HashMap<String, String> contact, Integer qrCount, Integer totalScore, Integer highestUnique,Boolean owner ,String id){
        playerController.setupPlayer(username, qrInventory, contact, qrCount, totalScore, highestUnique, owner ,id);
    }
}
