package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     */
    public void getPlayerSearch(Context context, String userName, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseController databaseController = DatabaseController.getInstance();

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our Search list
                if(!playerList.isEmpty() && !userName.isEmpty()) {
                    // clear previous search result when searching for a new thing
                    if(!userName.isEmpty()) {
                        qMyAdapter.clear();
                    }
                    for (Map player : playerList){

                        ArrayList<String> currentQrList = new ArrayList<String>();
                        if (player.get("Identifier").toString().contains(userName)){
                            Log.e("SearchController: ", "Player found: " + player.get("Identifier").toString());

                            qMyAdapter.add(new SearchItem(
                                    (String) player.get("Identifier"),
                                    (String) player.get("email"),
                                    (String) player.get("phone"),
                                    currentQrList
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

    public void getLocationSearch(Context context, String location, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseController databaseController = DatabaseController.getInstance();

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> locationList) {
                // Add each location to our Search list
                if(!locationList.isEmpty() && !location.isEmpty()) {
                    if(!location.isEmpty()) {
                        qMyAdapter.clear();
                    }
                    for (Map currentLocation : locationList){

                        if (currentLocation.get("Identifier").toString().contains(location)){
                            Log.e("SearchController: ", "currentLocation found: " + currentLocation.toString());


                            ArrayList<String> currentQrList = new ArrayList<String>();
//                            getQrSearch(context, currentQrList, location);
                            Set<String> keys = currentLocation.keySet();
                            for (String keyName : keys) {
                                if (!keyName.equals("Identifier")) {
                                    String [] qrArray = currentLocation.get(keyName).toString().split(",");
                                    for (String qr : qrArray) {
                                        currentQrList.add(qr);
                                    }
                                }
                            }
                            qMyAdapter.add(new SearchItem(
                                    (String) currentLocation.get("Identifier"),
                                    null,
                                    null,
                                    currentQrList
                            ));
                            Log.e("SearchController: ", "Qr found: " + currentQrList.toString());
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
