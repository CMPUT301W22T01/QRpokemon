package com.qrpokemon.qrpokemon.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.qrpokemon.qrpokemon.views.leaderboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardController {
    static protected LeaderboardController controllerInstance;

    protected LeaderboardController() {}  // Being protected allows us to Mock it

    public static LeaderboardController getInstance() {
        if (controllerInstance == null)
            controllerInstance = new LeaderboardController();

        return controllerInstance;
    }

    /**
     * Calls the database to return player data sorted by sortMethod
     * @param context The LeaderboardActivity context
     * @param list The list to be sorted
     * @param sortMethod method to be sorted with
     */
    public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
        String sortField;
        switch(sortMethod) {
            case 0:
                sortField = "totalScore";
                break;
            case 1:
                sortField = "highestUnique";
                break;
            case 2:
                sortField = "qrCount";
                break;
            default:
                sortField = null;
        }

        DatabaseProxy databaseProxy = DatabaseProxy.getInstance();
        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our leaderboard list
                for (int i = 0; i < playerList.size(); i++) {
                    Map<String, Object> player = playerList.get(i);
                    list.add(new LeaderboardItem(
                            // Guaranteed to exist so we can safely cast
                            (String) player.get("Identifier"), i+1,
                            (int)(long) player.get("highestUnique"),  // Cannot cast Long (not long) to int
                            (int)(long) player.get("qrCount"),
                            (int)(long) player.get("totalScore")
                    ));
                }

                // Update leaderboard views
                LeaderboardController.this.updateRank(context, list);
                list.notifyListUpdate();  // Tell subscribers the list is updated
            }
        };

        try {
            // Fetch data and update our leaderboard list
            list.clear();
            databaseProxy.getData(callback, new ArrayList<>(), "Player",
                    null, sortField,"Identifier");
        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Database call failed");
            Log.e("Leaderboard Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

    public void updateRank(Context context, LeaderboardList list) {

        // Try to get the user's player data
        HashMap<String, Object> player;
        try {
            PlayerController playerController = PlayerController.getInstance();
            player = playerController.getPlayer(null, null);

            // Fetch user data
            int rank = 0;  // Placeholder value so linter doesn't complain
            String username = (String) player.get("Identifier");
            int highestUnique = (int) player.get("highestUnique");
            int qrCount = (int) player.get("qrCount");
            int score = (int) player.get("totalScore");

            // We're logged in so our username exists
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(username)) {
                    rank = i+1;
                    break;
                }
            }
            // Set the appropriate text views
            ((LeaderboardActivity) context).setPersonalRank(rank, username, highestUnique, qrCount, score);

        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Failed to update rank");
            Log.e("Leaderboard Controller: ", exception.toString());
            ((Activity) context).finish();
        }
    }
}
