package com.qrpokemon.qrpokemon.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.qrpokemon.qrpokemon.views.search.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchController {

    static private SearchController controllerInstance;


    private SearchController() {}

    public static SearchController getInstance() {
        if (controllerInstance == null)
            controllerInstance = new SearchController();

        return controllerInstance;
    }

    /**
     * Gets the player list from database and update arrayAdapter
     * @param context The SearchActivity context
     * @param userName The user input
     * @param qMyAdapter The arrayAdapter we need to update
     */
    public void getPlayerSearch(Context context, String userName, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseProxy databaseProxy = DatabaseProxy.getInstance();

        if(!userName.isEmpty()) {
            qMyAdapter.clear();
        }
        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> playerList) {
                // Add each player to our Search list
                //Log.e("SearchController: ", "Inside run");
                if(!userName.isEmpty()) {
                    qMyAdapter.clear();
                }
                if(!playerList.isEmpty() && !userName.isEmpty()) {
                    // clear previous search result when searching for a new thing

                    for (Map player : playerList){

                        ArrayList<String> currentQrList = new ArrayList<String>();
                        if (player.get("Identifier").toString().contains(userName)){
                            Log.e("SearchController: ", "Player found: " + player.get("Identifier").toString());
                            HashMap contactInfo = new HashMap();

                            try {
                                Log.e("SearchController: ", "Player found email: " + player.get("contact").toString());
                                contactInfo = (HashMap) player.get("contact");
                            } catch (Exception e){
                                contactInfo.put("email", "no email");
                                contactInfo.put("phone", "no phone number");
                            }
//                            Log.e("SearchController: ", "Player found email: " + player.get("contact").toString());
//                            Log.e("SearchController: ", "Player found phone: " + player.get("phone").toString());
                            if (contactInfo.get("email") == null){
                                contactInfo.put("email", "no email");
                            }

                            if (contactInfo.get("phone") == null){
                                contactInfo.put("phone", "no phone number");
                            }

                            qMyAdapter.add(new SearchItem(
                                    (String) player.get("Identifier"),
                                    (String) contactInfo.get("email"),
                                    (String) contactInfo.get("phone"),
                                    currentQrList
                            ));

                            contactInfo.put("email", null);
                            contactInfo.put("phone", null);
                            qMyAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        };

        try {
            databaseProxy.getData(callback, temp, "Player", null);
        } catch (Exception exception) {
            Log.e("Search Controller: ", "Database call failed");
            Log.e("Search Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }

    /**
     * Gets the location list from database and update arrayAdapter
     * @param context The SearchActivity context
     * @param location The user input
     * @param qMyAdapter The arrayAdapter we need to update
     */
    public void getLocationSearch(Context context, String location, ArrayAdapter<SearchItem> qMyAdapter) {
        List<Map> temp = new ArrayList<>();  // Store our db results temporarily
        DatabaseProxy databaseProxy = DatabaseProxy.getInstance();

        if(!location.isEmpty()) {
            qMyAdapter.clear();
        }

        DatabaseCallback callback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> locationList) {
                // Add each location to our Search list
                if(!locationList.isEmpty() && !location.isEmpty()) {
//                    if(!location.isEmpty()) {
//                        qMyAdapter.clear();
//                    }
                    for (Map currentLocation : locationList){

                        if (currentLocation.get("Identifier").toString().contains(location)){
                            Log.e("SearchController: ", "currentLocation found: " + currentLocation.toString());


                            ArrayList<String> currentQrList = new ArrayList<String>();

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
            databaseProxy.getData(callback, temp, "LocationIndex", null);
        } catch (Exception exception) {
            Log.e("Search Controller: ", "Database call failed");
            Log.e("Search Controller: ", exception.toString());
            ((Activity) context).finish();  // Quit the activity on error
        }
    }




}
