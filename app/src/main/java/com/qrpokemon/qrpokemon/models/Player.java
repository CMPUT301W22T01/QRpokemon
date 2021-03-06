package com.qrpokemon.qrpokemon.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String username;
    private ArrayList<String> qrInventory;
    private HashMap contactInfo;
    private Integer qrCount;
    private Integer totalScore;
    private String id;
    private Integer highestUnique;
    private Boolean owner;

    public Player(String username, ArrayList<String> qrInventory, HashMap contactInfo, Integer qrCount, Integer totalScore, String id, Integer highestUnique, Boolean owner) {
        this.username = username;
        this.id = id;
        this.qrInventory = qrInventory;
        this.contactInfo = contactInfo;
        this.highestUnique = highestUnique;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHighestUnique() {
        return highestUnique;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public void setHighestUnique(Integer highestUnique) {
        this.highestUnique = highestUnique;
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
