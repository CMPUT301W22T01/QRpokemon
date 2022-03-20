package com.qrpokemon.qrpokemon.activities.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class LeaderboardList extends Observable {
    private ArrayList<LeaderboardItem> list;

    LeaderboardList() {
        list = new ArrayList<>();
    }

    public void add(LeaderboardItem leaderboardItem) {
        list.add(leaderboardItem);
    }

    public void notifyListUpdate() {
        setChanged();
        notifyObservers();
    }

    public List<LeaderboardItem> getList() {
        return list;
    }

    // TODO: Move sorting to LeaderboardController which will call Firestore
    public void sort(int sortMethod) {
        // TODO: Use String dropdown text instead of int?
        switch (sortMethod) {
            case 0:
                list.sort(new LeaderboardCompareTotalScore());
                break;
            case 1:
                list.sort(new LeaderboardCompareHighestUnique());
                break;
            case 2:
                list.sort(new LeaderboardCompareMostScanned());
                break;
        }

        notifyListUpdate();
    }

    // TODO: Replace with Firestore sorting query in Controller
    static class LeaderboardCompareTotalScore implements Comparator<LeaderboardItem> {
        public int compare(LeaderboardItem first, LeaderboardItem second) {
            return (int) (second.getTotalScore() - first.getTotalScore());
        }
    }

    static class LeaderboardCompareHighestUnique implements Comparator<LeaderboardItem> {
        public int compare(LeaderboardItem first, LeaderboardItem second) {
            return (int) (second.getTotalScore() - first.getTotalScore());
        }
    }

    static class LeaderboardCompareMostScanned implements Comparator<LeaderboardItem> {
        public int compare(LeaderboardItem first, LeaderboardItem second) {
            return (int) (second.getTotalScore() - first.getTotalScore());
        }
    }
}