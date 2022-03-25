package com.qrpokemon.qrpokemon.activities.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class LeaderboardList extends Observable {
    final private ArrayList<LeaderboardItem> list;

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

    public LeaderboardItem get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }
}