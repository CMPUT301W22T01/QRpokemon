package com.qrpokemon.qrpokemon.activities.qrinventory;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.PlayerController;
import com.qrpokemon.qrpokemon.models.QrCodeController;
import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrInventoryController {

    private Integer totalScore = 0;
    private HashMap<String, Object> data = new HashMap<>();
    private PlayerController player = PlayerController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();
    private QrCodeController qrCodeController = QrCodeController.getInstance();

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
    public HashMap<String, Object> getPlayerInfo (String currentPlayer, Boolean otherPlayer, @Nullable Context context) throws Exception {
        if (otherPlayer){ // if this qrInventory currently serving for other player, fetch original player before head back
            DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                @Override
                public void run(List<Map> dataList) {
                    Map currentPlayer = dataList.get(0);
                    playerController.setupPlayer((String) currentPlayer.get("Identifier"),
                            (ArrayList<String>) currentPlayer.get("qrInventory"),
                            (HashMap) currentPlayer.get("contact"),
                            ((Long) currentPlayer.get("qrCount")).intValue(),
                            ((Long)currentPlayer.get("totalScore")).intValue(),
                            (String) currentPlayer.get("id"));
                }
            };
            playerController.getPlayer(databaseCallback,new ArrayList<>(), Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID),"DeviceId");
        }
        else {
            HashMap<String, Object> temp;
            temp = playerController.getPlayer(null,null,null,null);
            return temp;
        }
        return new HashMap<>();
    } public HashMap<String,Object> getPlayerInfo (String currentPlayer) throws Exception { // if qrInventory serves for current player
        return getPlayerInfo(currentPlayer, false, null);
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
            qrCodeController.getQR(databaseCallback, result, qrHashCodes.get(i));
//            databaseController.getData(databaseCallback, result, "QrCode", qrHashCodes.get(i));
        }

        return data;
    }

    public HashMap<String, Object> getAllComments (DatabaseCallback databaseCallback, String hashName, List<Map> result) throws Exception {
            qrCodeController.getQR(databaseCallback, result, hashName);
//        databaseController.getData(databaseCallback, result, "QrCode", hashName);

        return data;
    }

    public void updateQR (String qrHash, @Nullable Integer score, @Nullable ArrayList<String> location, @Nullable HashMap<String, Object> comments, @Nullable HashMap<String,String> bitmap) throws Exception {
        qrCodeController.saveQr(qrHash,score,location,comments,bitmap);
    }

}

