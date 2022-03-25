package com.qrpokemon.qrpokemon.activities.leaderboard;

/**
* An item in leaderboard
*/
public class LeaderboardItem {
    final private int rank;
    final private String username;
    final private long highestScore;  // Firestore stores numbers as long
    final private long qrCount;
    final private long totalScore;

    public LeaderboardItem(String username, int rank, long highestScore, long qrCount, long totalScore) {
        this.username = username;
        this.rank = rank;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
        this.highestScore = highestScore;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public long getHighestScore() {
        return highestScore;
    }

    public long getQrQuantity() {
        return qrCount;
    }

    public long getTotalScore() {
        return totalScore;
    }
}