package com.qrpokemon.qrpokemon.activities.leaderboard;

/**
* An item in leaderboard
*/
public class LeaderboardItem {
    private int rank = 0;
    private String username;
    private int highestScore = 0;
    private long qrCount;
    private long totalScore;

    public LeaderboardItem(String username, long qrCount, long totalScore) {
        this.username = username;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public long getQrQuantity() {
        return qrCount;
    }

    public long getTotalScore() {
        return totalScore;
    }
}