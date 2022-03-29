package com.qrpokemon.qrpokemon.models;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class PlayerController extends Observable {
    private static PlayerController currentInstance;
    public Object savePlayerData;
    private Player currentPlayer = null;
    private String player = "Player";
    private DatabaseController databaseController = DatabaseController.getInstance();
    private PlayerController() {}

    public static PlayerController getInstance() {
        if (currentInstance == null)
            currentInstance = new PlayerController();
        return currentInstance;
    }

    /**
     * Takes in data related to a Player and find them from database/local
     * @param databaseCallback DatabaseCallback passed from Activity calls it
     * @param list A List of HashMaps as a holder for holding results from Firestore
     * @param username username to be found, can be null
     * @param IdentifierField A option check device id specifically, will look for Identifier (e.g. username) if null
     * @return A hashMap which contains local
     * @throws Exception if collection is incorrect in DatabaseController
     */
    public HashMap getPlayer(@Nullable DatabaseCallback databaseCallback, @Nullable List<Map> list, @Nullable String username, @Nullable String IdentifierField) throws Exception {
        HashMap<String, Object> info = new HashMap<>();
        if (username == null) {  // Get the current user
            info.put("Identifier",    this.currentPlayer.getUsername());
            info.put("qrInventory",   this.currentPlayer.getQrInventory());
            info.put("qrCount",       this.currentPlayer.getQrCount());
            info.put("totalScore",    this.currentPlayer.getTotalScore());
            info.put("contact",       this.currentPlayer.getContactInfo());
            info.put("highestUnique", this.currentPlayer.getHighest());

        } else if (IdentifierField == null) { // find player by username
            databaseController.getData(databaseCallback, list , player, username);
        } else {
            databaseController.getData(databaseCallback, list, player, username, null, IdentifierField);
        }
        return info;
    }
    public HashMap getPlayer(DatabaseCallback databaseCallback, String username) throws Exception {
        return getPlayer(databaseCallback, null, username, null);
    }

    /**
     * Load current Player class locally
     * @param username is guaranteed to be unique, it notifies Observer once player is created
     * @param qrInventory a ArrayList object store player's qr code(s)
     * @param contact player's contact info
     * @param qrCount player's qrcount number
     * @param totalScore player's Score so far
     */
    public void setupPlayer(String username,
                            @Nullable ArrayList<String> qrInventory,
                            @Nullable HashMap contact,
                            @Nullable Integer qrCount,
                            @Nullable Integer totalScore,
                            @Nullable Integer highestUnique,
                            String id) {
        if (this.currentPlayer == null){
            this.currentPlayer = new Player(username, qrInventory, contact, qrCount, totalScore, id, highestUnique);
        } else if (username != this.currentPlayer.getUsername()) { //require to switch another player:
            this.currentPlayer = new Player(username, qrInventory, contact, qrCount, totalScore, id, highestUnique);
        }
        Log.e("PlayerController: A player is created with : ", this.currentPlayer.getUsername());
        setChanged();
        notifyObservers();

    }

    /**
     * Update/Create a player's data based on the addIdentifier boolean flag
     * @param qrCount player's qrCount
     * @param totalScore player's total Score
     * @param qrInventory an Arraylist of qrHashes
     * @param contact contact info for player
     * @param addIdentifier if true create a new player in database, update a user otherwise
     * @throws Exception if collection is invalid
     */
    public void savePlayerData(@Nullable Integer qrCount,
                               @Nullable Integer totalScore,
                               @Nullable ArrayList<String> qrInventory,
                               @Nullable HashMap contact,
                               @Nullable Integer highestUnique,
                               @Nullable String id,
                               Boolean addIdentifier) throws Exception {
        DatabaseController databaseController = DatabaseController.getInstance();
        HashMap<String, Object> info = new HashMap<>();

        // Update player's data.
        // Don't update parameter if null
        if (qrCount != null) {
            this.currentPlayer.setQrCount(qrCount);
            info.put("qrCount", currentPlayer.getQrCount());
        }

        if (totalScore != null) {
            this.currentPlayer.setTotalScore(totalScore);
            info.put("totalScore", currentPlayer.getTotalScore());
        }

        if (qrInventory != null) {
            this.currentPlayer.setQrInventory(qrInventory);
            info.put("qrInventory", currentPlayer.getQrInventory());
        }

        if (id != null) {
            this.currentPlayer.setId(id);
            info.put("DeviceId",id);
        }

        if (contact != null) {
            this.currentPlayer.setContactInfo(contact);
            info.put("contact", currentPlayer.getContactInfo());
        }

        if (highestUnique != null) {
            this.currentPlayer.setHighestUnique(highestUnique);
            info.put("highestUnique", currentPlayer.getHighest());
        }

        info.put("qrCount",this.currentPlayer.getQrCount());
        info.put("totalScore", this.currentPlayer.getTotalScore());
        info.put("qrInventory", this.currentPlayer.getQrInventory());
        info.put("contact", this.currentPlayer.getContactInfo());
        info.put("highestUnique", this.currentPlayer.getHighest());

        if (addIdentifier) { //ading a new user
            info.put("Identifier", currentPlayer.getUsername());
            databaseController.writeData("Player", this.currentPlayer.getUsername() ,info ,true);
        } else { //update a user
            Log.e("PlayerController: ", "User at savePlayerData is: "+ this.currentPlayer.getUsername());
            databaseController.writeData("Player", this.currentPlayer.getUsername() ,info ,false);
        }
    }
}
