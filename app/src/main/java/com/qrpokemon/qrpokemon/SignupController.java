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

    /**
     * The username will be checked first
     * It asks PlayerController to create a current Player class
     * It asks PlayerController to add new player to database
     * @param context
     * @param newUsername
     * @param email
     * @param phone
     * @throws Exception if collection not in correct range
     */
    public void addNewPlayer(Context context, String newUsername, @Nullable String email, @Nullable String phone) throws Exception {
        List<Map> result = new ArrayList<Map>();
        try{
            DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                //this runs after datalist is collected from Database class
                @Override
                public void run(List<Map> dataList) {
                    if (dataList.isEmpty()){
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("email", email);
                        contact.put("phone", phone);

                        //Get playerController class there is only one PlayerController class
                        PlayerController playerController = PlayerController.getInstance();

                        try {
                            // add player on firestore
                            playerController.savePlayerData(0,0, new ArrayList<String>(), contact, true);

                            //Create current Player class in PlayerController class
                            playerController.setupPlayer(newUsername, new ArrayList<String>(), contact, 0,0);
                        } catch (Exception e) {
                            Log.e("SignupController: ", e.toString());
                        }
//                        Log.e("Database: ", "addNewPlayer") ;
                    } else{
                        Toast.makeText(context, "Username is not unique!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            // this will run first
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

    /**
     * write data to local FileSystem based on the filename
     * @param context
     * @param filename
     */
    public void write(Context context, String filename){
//        fileSystemController.writeToFile(context, filename, "firstRun");
    }
}
