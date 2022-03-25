package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
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
    private SignupController(){}
    private PlayerController playerController = PlayerController.getInstance();

    public static SignupController getInstance() {
        if (currentInstance == null)
            currentInstance = new SignupController();

        return currentInstance;
    }

    /**
     * The username will be checked first
     * It asks PlayerController to create a current Player class
     * It asks PlayerController to add new player to database
     * @param context from Activity passed in
     * @param newUsername New player's name
     * @param email player entered email
     * @param phone player entered phone
     * @throws Exception if collection not in correct range
     */
    public void addNewPlayer(Context context, String newUsername, @Nullable String email, @Nullable String phone) throws Exception {
        List<Map> result = new ArrayList<Map>();

        try{
            DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                //this runs after datalist is collected from DatabaseController class
                @Override
                public void run(List<Map> dataList) {
                    if (dataList.isEmpty()){
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("email", email);
                        contact.put("phone", phone);



                        try {
                            //Create current Player class in PlayerController class
                            playerController.setupPlayer(newUsername, new ArrayList<String>(), contact, 0,0);

                            // add player on firestore
                            playerController.savePlayerData(0,0, new ArrayList<String>(), contact, true);
                            write(context, "name", newUsername);
                            //TODO: delete/comment out this line at due date
//                            fileSystemController.deleteFile(context);
                        } catch (Exception e) {
                            Log.e("SignupController: ", e.toString());
                        }
//                        Log.e("DatabaseController: ", "addNewPlayer") ;
                    } else{
                        EditText email = (EditText) ((Activity) context).findViewById(R.id.et_email);
                        Toast.makeText(context, "Username is not unique!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            // this will run first
            playerController.getPlayer(databaseCallback, result, newUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param context Activity be passed in
     * @param filename ndicates which file is reading of from local
     * @param flag read from local or firestore database
     * @return String of player name
     */
    public String load(Context context, String filename, Boolean flag){
        String data = null;
        if (flag){ // read local files
            data = fileSystemController.readToFile(context, filename);
        } else { // load local user
            List<Map> result = new ArrayList<Map>();
            try{
                DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                    //this runs after datalist is collected from Database class
                    @Override
                    public void run(List<Map> dataList) {
                        if (!dataList.isEmpty()){
                            HashMap<String, String> contact = new HashMap<>();
                            contact = (HashMap<String, String>) dataList.get(0).get("contact");

                            //Get playerController class there is only one PlayerController class
                            PlayerController playerController = PlayerController.getInstance();
                            playerController.setupPlayer((String) dataList.get(0).get("Identifier"),
                                    (ArrayList<String>) dataList.get(0).get("qrInventory"),
                                    contact,
                                    ((Long) dataList.get(0).get("qrCount")).intValue(),
                                    ((Long) dataList.get(0).get("totalScore")).intValue());
                        }
                    }
                };
                // this will run first
                playerController.getPlayer(databaseCallback, result, fileSystemController.readToFile(context, filename));
                Log.e("SignupController: ", "Current player is : " );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    public String load(Context context, String filename){
        return load(context, filename, true);
    }

    /**
     * write data to local FileSystem based on the filename
     * @param context Activity be passed in
     * @param filename Filename to be written in
     * @param username player's username
     */
    public void write(Context context, String filename, @Nullable String username){
        if (filename.equals("name"))
            fileSystemController.writeToFile(context, filename, username);
        else
            fileSystemController.writeToFile(context, filename, "firstRun");
    }
}