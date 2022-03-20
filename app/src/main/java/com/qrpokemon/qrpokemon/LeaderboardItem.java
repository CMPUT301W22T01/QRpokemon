package com.qrpokemon.qrpokemon;

/**
* An item in leaderboard
*/
class LeaderboardItem {
    private int rank = 0;  // TODO: Get from database or calculate
    private String username;
    private int highestScore = 0;  // TODO: Get from database or calculate
    private long qrCount;
    private long totalScore;

    public LeaderboardItem(String username, long qrCount, long totalScore) {
        this.username = username;
        this.qrCount = qrCount;
        this.totalScore = totalScore;
    }

    // TODO: Remove unused getters
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