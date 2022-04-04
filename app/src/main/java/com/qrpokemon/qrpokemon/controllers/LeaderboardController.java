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

        PlayerController playerController = PlayerController.getInstance();
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
            playerController.getAllPlayers(callback, new ArrayList<>(), sortField);
        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Database call failed");
            Log.e("Leaderboard Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

    /**
     * Get the player's ranking and call LeaderboardActivity to set the views
     * @param context An Android context
     * @param list
     */
    public void updateRank(Context context, LeaderboardList list) {
        try {
            // Fetch user data
            PlayerController playerController = PlayerController.getInstance();
            HashMap<String, Object> player = playerController.getPlayer(null, null);
            String username = (String) player.get("Identifier");

            for (int i = 0; i < list.size(); i++) {
                // We're logged in so our username should exist
                if (list.get(i).getUsername().equals(username)) {
                    // Get our rank and ensure we have the latest player data by fetching ourselves
                    // from the leaderboard
                    int highestUnique = list.get(i).getHighestUnique();
                    int qrCount = list.get(i).getQrQuantity();
                    int totalScore = list.get(i).getTotalScore();

                    // Set the appropriate text views
                    ((LeaderboardActivity) context).setPersonalRank(i, username, highestUnique, qrCount, totalScore);
                    return;
                }
            }

            // We couldn't find our player :(
            throw new Exception("Current player missing in database");
        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Failed to update rank");
            Log.e("Leaderboard Controller: ", exception.toString());
            exception.printStackTrace();
            ((Activity) context).finish();
        }
    }
}
