package com.qrpokemon.qrpokemon;

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

    public void sort(int sortMethod) {
        // TODO: Add other sorting methods
        // TODO: Use String dropdown text?
        switch (sortMethod) {
            case 0:
                list.sort(new LeaderboardCompareTotalScore());
                break;
            //case 1:
            //    break;
            //case 2:
            //    break;
            //case 3:
            //    break;
        }

        notifyListUpdate();
    }
}

// NOTE: Replace if Database can do the sorting
class LeaderboardCompareTotalScore implements Comparator<LeaderboardItem> {
    public int compare(LeaderboardItem first, LeaderboardItem second) {
        return (int) (second.getTotalScore() - first.getTotalScore());
    }
}