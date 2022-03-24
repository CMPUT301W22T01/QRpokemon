package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchController {

    static private com.qrpokemon.qrpokemon.SearchController controllerInstance;


    private SearchController() {}

    public static com.qrpokemon.qrpokemon.SearchController getInstance() {
        if (controllerInstance == null)
            controllerInstance = new com.qrpokemon.qrpokemon.SearchController();

        return controllerInstance;
    }

    /**
     * Gets and fills a list of Leaderboard rankings (unsorted)
     * @param context The LeaderboardActivity context
     * @param list A list of leaderboard items (will be cleared)
     */
    public void getPlayerSearch(Context context, ArrayList<SearchItem> list, String userName, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseController databaseController = DatabaseController.getInstance();

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our Search list
                if(!playerList.isEmpty() && !userName.isEmpty()) {
                    if(!userName.isEmpty()) {
                        qMyAdapter.clear();
                    }
                    for (Map player : playerList){

                        if (player.get("Identifier").toString().contains(userName)){
                            Log.e("SearchController: ", "Player found: " + player.get("Identifier").toString());

                            qMyAdapter.add(new SearchItem(
                                    (String) player.get("Identifier"),
                                    (String) player.get("email"),
                                    (String) player.get("phone")
                            ));

                            qMyAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        };

        try {
            databaseController.getData(callback, temp, "Player", null);
        } catch (Exception exception) {
            Log.e("Search Controller: ", "Database call failed");
            Log.e("Search Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

    public void getLocationSearch(Context context, ArrayList<SearchItem> list, String location, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseController databaseController = DatabaseController.getInstance();

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our Search list
                if(!playerList.isEmpty() && !location.isEmpty()) {
                    if(!location.isEmpty()) {
                        qMyAdapter.clear();
                    }
                    for (Map player : playerList){

                        if (player.get("Identifier").toString().contains(location)){
                            Log.e("SearchController: ", "Player found: " + player.get("Identifier").toString());

                            qMyAdapter.add(new SearchItem(
                                    (String) player.get("Identifier"),
                                    null,
                                    null
                            ));

                            qMyAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        };

        try {
            databaseController.getData(callback, temp, "LocationIndex", null);
        } catch (Exception exception) {
            Log.e("Search Controller: ", "Database call failed");
            Log.e("Search Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

}
