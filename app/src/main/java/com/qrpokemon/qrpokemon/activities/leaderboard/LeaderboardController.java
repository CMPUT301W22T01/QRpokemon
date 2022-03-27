package com.qrpokemon.qrpokemon.activities.leaderboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.DatabaseController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardController {
    static private LeaderboardController controllerInstance;

    private LeaderboardController() {}

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
        // TODO: Use binary search to get estimated ranking (array index)
        // TODO: Use String dropdown text instead of int?

        // Figure out which sort method to use
        String sortField;
        switch(sortMethod) {
            case 0:
                sortField = "totalScore";
                break;
            //case 1:
            //    sortField = "highestUnique";
            //    break;
            case 2:
                sortField = "qrCount";
                break;
            default:
                sortField = null;
        }

        DatabaseController databaseController = DatabaseController.getInstance();
        List<Map> tempList = new ArrayList<>();  // Store our db results temporarily
        list.clear();  // Clear our original list before adding new entries

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our leaderboard list
                for (int i = 0; i < playerList.size(); i++) {
                    Map<String, Object> player = playerList.get(i);
                    list.add(new LeaderboardItem(
                            // Types are guaranteed so we can safely cast
                            (String) player.get("Identifier"), i+1, 0,
                            (long) player.get("qrCount"),
                            (long) player.get("totalScore")
                    ));
                }

                list.notifyListUpdate();  // Tell subscribers the list is updated
            }
        };

        try {
            databaseController.getData(callback, tempList, "Player", null, sortField,"Identifier");
        } catch (Exception exception) {
            Log.e("Leaderboard Controller: ", "Database call failed");
            Log.e("Leaderboard Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }
}
