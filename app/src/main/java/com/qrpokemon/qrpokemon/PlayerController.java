package com.qrpokemon.qrpokemon;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class PlayerController extends Observable {
    private static PlayerController currentInstance;
    private Player currentPlayer = null;

    private PlayerController() {}

    public static PlayerController getInstance() {
        if (currentInstance == null)
            currentInstance = new PlayerController();

        return currentInstance;
    }

    /**
     * takes currentPlayer's data and put them into one HashMap
     * @param username unique user's name. Can be null if read off from current player
     * @return a HashMap consists of username, qrInventory, contactInfo, qrCount, totalScore
     */
    public HashMap getPlayer(@Nullable String username) throws Exception {
        HashMap<String, Object> info = new HashMap<>();
        if (username == null){
            //a temporary object to convert other data type into ArrayList
            ArrayList<String> temp = new ArrayList<>();

            info.put("Identifier", this.currentPlayer.getUsername());
            info.put("qrInventory", this.currentPlayer.getQrInventory());
            info.put("qrCount",this.currentPlayer.getQrCount());
            info.put("totalScore", this.currentPlayer.getTotalScore());
            info.put("contact", this.currentPlayer.getContactInfo());
        }
        else{
            DatabaseController databaseController = new DatabaseController();
            info = (HashMap<String, Object>) databaseController.readFromDatabase("Player", username).get(0);
        }
        return info;
    }

    /**
     * create a new player
     * @param username is guaranteed to be unique, it notifies Observer once player is created
     * @param qrInventory
     * @param contact
     * @param qrCount
     * @param totalScore
     */
    public void setupPlayer(String username,
                            @Nullable ArrayList<String> qrInventory,
                            @Nullable HashMap contact,
                            @Nullable Integer qrCount,
                            @Nullable Integer totalScore) {
        if (this.currentPlayer == null){
            this.currentPlayer = new Player(username, qrInventory, contact, qrCount, totalScore);
            setChanged();
            notifyObservers(null);
        }
    }

    /**
     * Update/Create a player's data based on the addIdentifier boolean flag
     * @param qrCount
     * @param totalScore
     * @param qrInventory an Arraylist of qrHashes
     * @param contact contact info for player
     * @param addIdentifier if true create a new player in database, update a user otherwise
     * @throws Exception if collection is invalid
     */
    public void savePlayerData(@Nullable Integer qrCount,
                               @Nullable Integer totalScore,
                               @Nullable ArrayList<String> qrInventory,
                               @Nullable HashMap contact,
                               Boolean addIdentifier) throws Exception {
        Database database = Database.getInstance();
        HashMap<String, Object> info = new HashMap<>();

        //update player's data.
        // Don't update parameter if null
        if (qrCount != null){
            this.currentPlayer.setQrCount(qrCount);
            info.put("qrCount",this.currentPlayer.getQrCount());
        }
        if (totalScore != null){
            this.currentPlayer.setTotalScore(totalScore);
            info.put("totalScore", this.currentPlayer.getTotalScore());
        }
        if (qrInventory != null){
            this.currentPlayer.setQrInventory(qrInventory);
            info.put("qrInventory", this.currentPlayer.getQrInventory());
        }

        if (contact != null){
            this.currentPlayer.setContactInfo(contact);
            info.put("contact", this.currentPlayer.getContactInfo());
        }

        if (addIdentifier)
            info.put("Identifier", this.currentPlayer.getUsername());

        database.writeData("Player", this.currentPlayer.getUsername() ,info ,true);
    }
    // boolean flag is false in default, updating user only.
    public void savePlayerData(@Nullable Integer qrCount,
                               @Nullable Integer totalScore,
                               @Nullable ArrayList<String> qrInventory,
                               @Nullable HashMap contact) throws Exception {
        savePlayerData(qrCount, totalScore, qrInventory, contact, false);
    }
}