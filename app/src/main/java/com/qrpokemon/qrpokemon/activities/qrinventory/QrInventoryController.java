package com.qrpokemon.qrpokemon.activities.qrinventory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qrpokemon.qrpokemon.DatabaseCallback;
import com.qrpokemon.qrpokemon.DatabaseController;
import com.qrpokemon.qrpokemon.PlayerController;
import com.qrpokemon.qrpokemon.R;

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
     * Get and return all the data of "currentPlayer" from local file.
     * @param currentPlayer The identifier of the current player
     * @return all the information of the current player's document, which is a hashMap
     * @throws Exception if collection is incorrect or player doesn't found
     */
    public HashMap<String, Object> getPlayerInfo (String currentPlayer) throws Exception {

        HashMap<String, Object> temp;
        temp = playerController.getPlayer(null,null,null,null);
        return temp;
    }

    /**
     * Get a series of hash codes of qrcode, which is stored in argument "qrHashCodes", then put
     * all the documents of those hash codes from collection "QrCode".
     * @param context       current activity
     * @param qrHashCodes   an ArrayList which has all the qrCodes owned by the current player
     * @param currentPlayer String of the current player
     * @return              the document of a qrHash
     * @throws Exception if collection name is incorrect
     */
    public HashMap<String, Object> getAndSetQrCodeData(Context context, ArrayList<String> qrHashCodes, ArrayAdapter<String> qrInventory, String currentPlayer) throws Exception {

        totalScore = 0;
        List<Map> result = new ArrayList<Map>();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {

            @Override
            public void run(List<Map> dataList) {

                if (!dataList.isEmpty()) {
//                    Log.e(TAG, "Function 'getAndSetQrCodeData' found:" + dataList.toString());
                    if (dataList != null ) {
                        data.put((String) dataList.get(dataList.size()-1).get("Identifier"), dataList);
                        ListView temp = ((Activity) context).findViewById(R.id.QR_inventory_list);

                        HashMap tMap = (HashMap) dataList.get(dataList.size()-1).get("Bitmap");
                        String bitmap = (String) tMap.get(currentPlayer);

                        qrInventory.add(String.valueOf(dataList.get(dataList.size()-1).get("Score"))
                                + " "
                                + (String) (dataList.get(dataList.size()-1).get("Identifier"))
                                + " "
                                + bitmap);

                        totalScore += Integer.valueOf(String.valueOf( dataList.get(dataList.size()-1).get("Score")));

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
        }

        return data;
    }

    /**
     * This function receive a (String) 'hashName', then store all the comments of that qrCode into
     * an HashMap 'commentsOfCurQrcode'
     * @param context               current activity
     * @param hashName              which qrCode we interest
     * @param commentsOfCurQrcode   where we store all those comments of a qrCode
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getAllComments (Context context, String hashName, HashMap<String, ArrayList<String>> commentsOfCurQrcode) throws Exception {
        List<Map> result = new ArrayList<>();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {

                if (!dataList.isEmpty()){
//                    Log.e(TAG, "Function 'getAllComments' found:" + dataList.toString());

                    HashMap<String, ArrayList<String>> tMap;
                    String[] tKeys;
                    ArrayList<String> keys = new ArrayList<>();

                    try {
                        // Get all keys (ArrayList<String>) and store them in ArrayList keys
                        tMap = (HashMap<String, ArrayList<String>>) dataList.get(0).get("Comments");
                        tKeys = tMap.keySet().toArray(new String[0]);
                        for (String k : tKeys) {
                            keys.add(k);
                        }

                        // Store all the comments of qrCode into HashMap 'commentsOfCurQrcode'
                        for (int i = 0; i < tMap.size(); i++) {
                            Log.e(TAG, "loop: " + keys.get(i) + " " + tMap.get(keys.get(i)));
                            commentsOfCurQrcode.put(keys.get(i), tMap.get(keys.get(i)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        databaseController.getData(databaseCallback, result, "QrCode", hashName);

        return data;
    }

}

