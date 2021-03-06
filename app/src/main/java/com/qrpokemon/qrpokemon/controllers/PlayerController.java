package com.qrpokemon.qrpokemon.controllers;

import android.util.Log;

import androidx.annotation.Nullable;

import com.qrpokemon.qrpokemon.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class PlayerController extends Observable {
    private static PlayerController currentInstance;
    private Player currentPlayer = null;
    private String player = "Player";
    private DatabaseProxy databaseProxy = DatabaseProxy.getInstance();
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
     * @throws Exception if collection is incorrect in DatabaseProxy
     */
    public HashMap getPlayer(@Nullable DatabaseCallback databaseCallback, @Nullable List<Map> list, @Nullable String username, @Nullable String IdentifierField) throws Exception {
        HashMap<String, Object> info = new HashMap<>();
        if (username == null) {  // Get the current user
            info.put("Identifier",    this.currentPlayer.getUsername());
            info.put("qrInventory",   this.currentPlayer.getQrInventory());
            info.put("qrCount",       this.currentPlayer.getQrCount());
            info.put("totalScore",    this.currentPlayer.getTotalScore());
            info.put("contact",       this.currentPlayer.getContactInfo());
            info.put("highestUnique", this.currentPlayer.getHighestUnique());
            info.put("DeviceId", this.currentPlayer.getId());
            info.put("Owner", this.currentPlayer.getOwner());

        } else if (IdentifierField == null) { // find player by username
            databaseProxy.getData(databaseCallback, list , player, username);
        } else {
            databaseProxy.getData(databaseCallback, list, player, username, null, IdentifierField);
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
     * @param highestUnique player's higest score for unique qrcode
     * @param owner A boolean whether this player is an owner
     * @param id A String for DeviceId
     */
    public void setupPlayer(String username,
                            @Nullable ArrayList<String> qrInventory,
                            @Nullable HashMap contact,
                            @Nullable Integer qrCount,
                            @Nullable Integer totalScore,
                            @Nullable Integer highestUnique,
                            Boolean owner,
                            String id) {
        if (this.currentPlayer == null){
            this.currentPlayer = new Player(username, qrInventory, contact, qrCount, totalScore, id, highestUnique, owner);
        } else if (username != this.currentPlayer.getUsername()) { //require to switch another player:
            this.currentPlayer = new Player(username, qrInventory, contact, qrCount, totalScore, id, highestUnique, owner);
        }
        Log.e("PlayerController: A player is created with : ", this.currentPlayer.getUsername() + this.currentPlayer.getQrInventory().toString());
        setChanged();
        notifyObservers();

    }

    /**
     * Save other player's data on Firestore
     * @param qrCount player's qrCount
     * @param totalScore player's total score
     * @param qrInventory player's qrInventory
     * @param contact player's contact info
     * @param highestUnique player's highest unique score
     * @param id player's deviceID
     * @param owner if player is an owner or not
     * @param identifier player's username
     * @param overwrite  whether overwrites player's data
     * @throws Exception wrong collection name is going to throw exception
     */
    public void saveOtherPlayerData(Integer qrCount,
                                    Integer totalScore,
                                    ArrayList<String> qrInventory,
                                    HashMap contact,
                                    Integer highestUnique,
                                    String id,
                                    Boolean owner,
                                    String identifier,
                                    Boolean overwrite) throws Exception {
        HashMap<String, Object> info = new HashMap<>();

        info.put("Identifier", identifier);
        info.put("qrCount",qrCount);
        info.put("totalScore", totalScore);
        info.put("qrInventory", qrInventory);
        info.put("contact", contact);
        info.put("highestUnique", highestUnique);
        info.put("DeviceId", id);
        info.put("Owner", owner);

        databaseProxy.writeData("Player", identifier ,info ,overwrite);
    }


    /**
     * Update/Create a player's data based on the addIdentifier boolean flag
     * @param qrCount player's qrCount
     * @param totalScore player's total Score
     * @param qrInventory an Arraylist of qrHashes
     * @param contact contact info for player
     * @param highestUnique highest score from a unique qrcode
     * @param id DeviceId of the given user
     * @param owner a Boolean variable indicates if this is an owner
     * @param addIdentifier if true create a new player in database, update a user otherwise
     * @throws Exception if collection is invalid
     */
    public void savePlayerData(@Nullable Integer qrCount,
                               @Nullable Integer totalScore,
                               @Nullable ArrayList<String> qrInventory,
                               @Nullable HashMap contact,
                               @Nullable Integer highestUnique,
                               @Nullable String id,
                               @Nullable Boolean owner,
                               Boolean addIdentifier) throws Exception {
        HashMap<String, Object> info = new HashMap<>();

        // Update player's data.
        // Don't update parameter if null
        if (qrCount != null) {
            this.currentPlayer.setQrCount(qrCount);
        }

        if (totalScore != null) {
            this.currentPlayer.setTotalScore(totalScore);
        }

        if (qrInventory != null) {
            this.currentPlayer.setQrInventory(qrInventory);
        }

        if (id != null) {
            this.currentPlayer.setId(id);
            info.put("DeviceId",id);
        }

        if (contact != null) {
            this.currentPlayer.setContactInfo(contact);
        }

        if (owner != null){
            this.currentPlayer.setOwner(owner);
        }

        if (highestUnique != null) {
            this.currentPlayer.setHighestUnique(highestUnique);
        }

        info.put("qrCount",this.currentPlayer.getQrCount());
        info.put("totalScore", this.currentPlayer.getTotalScore());
        info.put("qrInventory", this.currentPlayer.getQrInventory());
        info.put("contact", this.currentPlayer.getContactInfo());
        info.put("highestUnique", this.currentPlayer.getHighestUnique());
        info.put("Owner", this.currentPlayer.getOwner());

        if (addIdentifier) {  // adding a new user
            info.put("Identifier", currentPlayer.getUsername());
            databaseProxy.writeData("Player", this.currentPlayer.getUsername() ,info ,true);

        } else { //update a user
            databaseProxy.writeData("Player", this.currentPlayer.getUsername() ,info ,false);
            Log.e("PlayerController: ", this.currentPlayer.getQrInventory().toString());
        }
    }

    /**
     * Grab all players in Player collection from Firestore database
     * @param databaseCallback a call back function once it retries result from Firestore
     * @param result data found form Firestore will be contained in List(Map)
     * @param sortField A string indicates that how the data will be sorted by
     */
    public void getAllPlayers(DatabaseCallback databaseCallback, List<Map> result, @Nullable String sortField){
        try {
            databaseProxy.getData(databaseCallback, result, player, null, sortField, "Identifier");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getAllPlayers(DatabaseCallback databaseCallback, List<Map> result) {
        getAllPlayers(databaseCallback, result, null);
    }

    /**
     * Delete a player with given username
     * @param playerName username indicates which user will be deleted
     * @throws Exception raise exception if collection name is incorrect
     */
    public void deletePlayer(String playerName) throws Exception {
        databaseProxy.deleteData(player, playerName);
    }
}
