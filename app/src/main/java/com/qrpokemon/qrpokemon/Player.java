package com.qrpokemon.qrpokemon;

import java.util.HashMap;

public class Player {
    private String username;
    private String[] qrInventory;
    private HashMap contactInfo;
    private Integer qrCount;
    private Integer totalScore;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getQrInventory() {
        return qrInventory;
    }

    public void setQrInventory(String[] qrInventory) {
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
