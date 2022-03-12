package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SignupController {
    private static SignupController currentInstance;
    private FileSystemController fileSystemController = new FileSystemController();
    private Database database = Database.getInstance();

    private SignupController(){}

    public static SignupController getInstance() {
        if (currentInstance == null)
            currentInstance = new SignupController();

        return currentInstance;
    }


    public void addNewPlayer(Context context, String newUsername, @Nullable String email, @Nullable String phone) throws Exception {
        List<Map> result = new ArrayList<Map>();
        try{
            DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                @Override
                public void run(List<Map> dataList) {
                    if (dataList.isEmpty()){
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("email", email);
                        contact.put("phone", phone);

                        PlayerController playerController = PlayerController.getInstance(); //gives current player to PlayerController

                        try {
                            playerController.setupPlayer(newUsername, new ArrayList<String>(), contact, 0,0);
                            playerController.savePlayerData(0,0, new ArrayList<String>(), contact, true);
                        } catch (Exception e) {
                            Log.e("SignupController: ", e.toString());
                        }
//                        Log.e("Database: ", "addNewPlayer") ;
                    } else{
                        Toast.makeText(context, "Username is not unique!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            database.getData(databaseCallback, result, "Player", newUsername);
        } catch (Exception e) {
            e.printStackTrace();
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
