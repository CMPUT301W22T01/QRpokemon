package com.qrpokemon.qrpokemon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String username;
    private ArrayList<String> qrInventory;
    private HashMap contactInfo;
    private Integer qrCount;
    private Integer totalScore;

    public Player(String username, ArrayList<String> qrInventory, HashMap contactInfo, Integer qrCount, Integer totalScore) {
        this.username = username;
        this.qrInventory = qrInventory;
        this.contactInfo = contactInfo;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getQrInventory() {
        return qrInventory;
    }

    public void setQrInventory(ArrayList<String> qrInventory) {
        this.qrInventory = qrInventory;
    }

    public HashMap getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(HashMap contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Integer getQrCount() {
        return qrCount;
    }

    public void setQrCount(Integer qrCount) {
        this.qrCount = qrCount;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
