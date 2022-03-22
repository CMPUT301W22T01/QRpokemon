package com.qrpokemon.qrpokemon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrInventoryController {

    private Integer totalScore = 0;
    private HashMap<String, Object> data = new HashMap<>();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private PlayerController player = PlayerController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();

    private static QrInventoryController currentInstance;
    final private String TAG = "QrInventoryController";

    private QrInventoryController(){}

    public static QrInventoryController getInstance() {
        if (currentInstance == null)
            currentInstance = new QrInventoryController();

        return currentInstance;
    }


    /**
     * Get and return all the data from collection "Player", document "currentPlayer"
     * @param currentPlayer The identifier of the current player
     * @return all the information of the current player's document, which is a hashMap
     * @throws Exception if collection is incorrect or player doesn't found
     */
    public HashMap<String, Object> getPlayerInfo (String currentPlayer) throws Exception {

        HashMap<String, Object> temp;
        temp = playerController.getPlayer(currentPlayer);
        return temp;
    }

    /**
     * Get a series of hash codes of qrcode, which is stored in argument "qrHashCodes", then put
     * all the documents of those hash codes from collection "QrCode".
     * @param context       current activity
     * @param qrHashCodes   an ArrayList which has all the qrCodes owned by the current player
     * @return              the document of a qrHash
     * @throws Exception if collection name is incorrect
     */
    public HashMap<String, Object> getAndSetQrCodeData(Context context, ArrayList<String> qrHashCodes, ArrayAdapter<String> qrInventory) throws Exception {

        totalScore = 0;
        List<Map> result = new ArrayList<Map>();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {

            @Override
            public void run(List<Map> dataList) {

                if (!dataList.isEmpty()) {
                    Log.e(TAG, "Function 'getAndSetQrCodeData' found:" + dataList.toString());
                    if (dataList != null ) {
                        data.put((String) dataList.get(dataList.size()-1).get("Identifier"), dataList);
                        ListView temp = ((Activity) context).findViewById(R.id.QR_inventory_list);

                        qrInventory.add(String.valueOf(dataList.get(dataList.size()-1).get("score"))
                                + " "
                                + ((String) (dataList.get(dataList.size()-1).get("Identifier"))));

                        Log.e(TAG, "Hash '"
                                + (String.valueOf(dataList.get(dataList.size()-1).get("score")))
                                + " "
                                + (String) (dataList.get(dataList.size()-1).get("Identifier"))
                                + "' has added to datalist");

                        totalScore += Integer.valueOf(String.valueOf( dataList.get(dataList.size()-1).get("score")));
                        Log.e(TAG, "Current score: " + totalScore.toString());

                        // set the value of total score
                        TextView tvScore = ((Activity) context).findViewById(R.id.tv_total_score);
                        tvScore.setText(totalScore.toString());

                        temp.setAdapter(qrInventory);

                    }
                }
            }
        };
        for (int i = 0; i < qrHashCodes.size(); i++) {
            databaseController.getData(databaseCallback, result, "QrCode", qrHashCodes.get(i));
            Log.e(TAG, "Loop now get hash: " + qrHashCodes.get(i));
        }

        return data;
    }

    /**
     * This function receive a (String) 'hashName', then store all the comments of that qrCode into
     * an arrayList 'commentsOfCurQrcode'
     * @param context               current activity
     * @param hashName              which qrCode we interest
     * @param commentsOfCurQrcode   where we store all those comments
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getAllComments (Context context, String hashName, ArrayList<String> commentsOfCurQrcode) throws Exception {
        List<Map> result = new ArrayList<>();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {

                if (!dataList.isEmpty()){
                    Log.e(TAG, "Function 'getAllComments' found:" + dataList.toString());

                    ArrayList<String> tList;
                    tList = (ArrayList<String>) dataList.get(0).get("comments");

                    for (int i = 0; i < tList.size(); i++) {
                        commentsOfCurQrcode.add(tList.get(i));
                    }
                }
            }
        };
        databaseController.getData(databaseCallback, result, "QrCode", hashName);

        return data;
    }

}

