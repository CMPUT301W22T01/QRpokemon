package com.qrpokemon.qrpokemon.views.leaderboard;

/**
* An item in leaderboard
*/
public class LeaderboardItem {
    final private int rank;
    final private String username;
    final private int highestUnique;
    final private int qrCount;
    final private int totalScore;

    public LeaderboardItem(String username, int rank, int highestUnique, int qrCount, int totalScore) {
        this.username = username;
        this.rank = rank;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
        this.highestUnique = highestUnique;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getHighestUnique() {
        return highestUnique;
    }

    public int getQrQuantity() {
        return qrCount;
    }

    public int getTotalScore() {
        return totalScore;
    }
}