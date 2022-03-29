package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.NotFoundException;
import com.qrpokemon.qrpokemon.activities.qrinventory.QrInventoryActivity;
import com.qrpokemon.qrpokemon.activities.qrscanned.QrScannedController;
import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.DatabaseController;
import com.qrpokemon.qrpokemon.models.FileSystemController;
import com.qrpokemon.qrpokemon.models.PlayerController;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainMenuController implements Observer {
    private static MainMenuController currentInstance;
    private DatabaseController database = DatabaseController.getInstance();
    private FileSystemController fileSystemController = new FileSystemController();
    private QrScannedController qrScannedController = new QrScannedController();
    private Bitmap photoBitmap;
    private String photoContent;
    private String hash = "";
    private PlayerController playerController = PlayerController.getInstance();
    private Context main;

    public static MainMenuController getInstance() {
        if (currentInstance == null)
            currentInstance = new MainMenuController();

        return currentInstance;
    }


    /**
     * load current username from firestore
     *
     * @param context context of current activity
     */
    public void load(Context context) {

        TextView text = ((MainActivity) context).findViewById(R.id.user_textView);
        PlayerController playerController = PlayerController.getInstance();

        try {
            text.setText((String) playerController.getPlayer(null, null, null, null).get("Identifier"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPermission(Context context) {
        qrScannedController.checkPermission(context);
    }

    public void findOtherPlayer(ActivityResult result, Context context) throws NotFoundException, Exception {
        main = context;
        Bundle bundle = result.getData().getExtras();
        photoBitmap = (Bitmap) bundle.get("data");
        photoContent = qrScannedController.analyzeImage(photoBitmap);
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {
                if (dataList.isEmpty()){ //no player found:
                    Toast.makeText(context, "Player not found", Toast.LENGTH_SHORT).show();
                }
                else { // if player found:
                    Map player = dataList.get(0);
                    playerController.addObserver(MainMenuController.this);
                    Log.e("MainMenuController : ", "other User is :" + (String) player.get("Identifier"));
                    playerController.setupPlayer((String) player.get("Identifier"),
                            (ArrayList<String>) player.get("qrInventory"),
                            (HashMap) player.get("contact"),
                            ((Long) player.get("qrCount")).intValue(),
                            ((Long)player.get("totalScore")).intValue(),
                            ((Long)player.get("highestUnique")).intValue(),
                            (String) player.get("id"));

                }

            }
        };


        List<Map> dataList = new ArrayList<>();
        playerController.getPlayer(databaseCallback, dataList,photoContent,"Identifier");
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap temp = new HashMap();
        try {
            temp = playerController.getPlayer(null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerController.deleteObserver(MainMenuController.this);
        Log.e("MainMenuController : ", "other User is :" + observable.toString());
        Intent intent = new Intent(main, QrInventoryActivity.class);
        main.startActivity(intent);
    }
}





