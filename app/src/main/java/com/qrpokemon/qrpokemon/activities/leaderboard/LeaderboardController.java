package com.qrpokemon.qrpokemon.activities.leaderboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.qrpokemon.qrpokemon.DatabaseCallback;
import com.qrpokemon.qrpokemon.DatabaseController;
import com.qrpokemon.qrpokemon.PlayerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardController {
    static private LeaderboardController controllerInstance;
    private int myScore;
    private String sortMethod;
    private PlayerController playerController;

    private LeaderboardController() {}

    public static LeaderboardController getInstance() {
        if (controllerInstance == null)
            controllerInstance = new LeaderboardController();

        return controllerInstance;
    }

    /**
     * Gets and fills a list of Leaderboard rankings (unsorted)
     * @param context The LeaderboardActivity context
     * @param list A list of leaderboard items (will be cleared)
     */
    public void getLeaderboard(Context context, LeaderboardList list) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseController databaseController = DatabaseController.getInstance();

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our leaderboard list
                for (Map<String, Object> player : playerList) {
                    list.add(new LeaderboardItem(
                                // Types are guaranteed so we can safely cast
                                (String) player.get("Identifier"),
                                (long) player.get("qrCount"),
                                (long) player.get("totalScore")
                    ));
                }

                list.sort(0);  // Use default sort method
                list.notifyListUpdate();  // Tell subscribers the list is updated
            }
        };

        try {
            databaseController.getData(callback, temp, "Player", null);
        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Database call failed");
            Log.e("Leaderboard Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

    /**
     * Sorts data based on what is selected in Leaderboard Activity
     * @param context The LeaderboardActivity context
     * @param list The list to be sorted
     * @param sortMethod method to be sorted with
     */
    public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
        list.sort(sortMethod);
    }
}
