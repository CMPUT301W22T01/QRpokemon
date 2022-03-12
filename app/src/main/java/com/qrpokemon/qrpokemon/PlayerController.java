package com.qrpokemon.qrpokemon;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.common.data.DataBufferObserverSet;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerController {
    private Player currentPlayer;

    PlayerController(Player currentPlayer) throws Exception {
        this.currentPlayer = currentPlayer;
        savePlayerData(0,
                0,
                new ArrayList<String>());
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
     * update count, totalScore and qrInventory of current user.
     * @param count
     * @param totalScore
     * @param qrInventory
     */
    public void savePlayerData(@Nullable Integer count,
                               @Nullable  Integer totalScore,
                               @Nullable  ArrayList<String> qrInventory) throws Exception {
        Database database = new Database();
        HashMap<String, Object> info = new HashMap<>();

        //update player's data
        if (count != null){
            this.currentPlayer.setQrCount(count);
        }
        if (totalScore != null){
            this.currentPlayer.setTotalScore(totalScore);
        }
        if (qrInventory != null){
            this.currentPlayer.setQrInventory(qrInventory);
        }

        info.put("Identifier", this.currentPlayer.getUsername());
        info.put("qrInventory", this.currentPlayer.getQrInventory());
        info.put("qrCount",this.currentPlayer.getQrCount());
        info.put("totalScore", this.currentPlayer.getTotalScore());
        info.put("contact", this.currentPlayer.getContactInfo());
        database.writeData("Player", this.currentPlayer.getUsername() ,info ,true);
    }
}
