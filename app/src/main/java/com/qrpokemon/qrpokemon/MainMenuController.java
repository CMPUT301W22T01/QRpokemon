package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;
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

public class MainMenuController  {
    private static MainMenuController currentInstance;
    private DatabaseController database = DatabaseController.getInstance();
    private FileSystemController fileSystemController = new FileSystemController();
    private QrScannedController qrScannedController = new QrScannedController();
    public static final int CAMERA_ACTION_CODE = 101;
    private Bitmap photoBitmap;
    private String photoContent;
    private String hash = "";


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

    public void findOtherPlayer(ActivityResult result, Context context) throws  Exception {
        PlayerController playerController = PlayerController.getInstance();
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
                    playerController.setupPlayer( (String) player.get("Identifier"),
                            (ArrayList<String>) player.get("qrInventory"),
                            (HashMap) player.get("contact"),
                            ((Long) player.get("qrCount")).intValue(),
                            ((Long)player.get("totalScore")).intValue(),
                            (String) player.get("id"));
                    Log.e("MainMenuController : ", "other User is :" +(String) player.get("Identifier"));
                    Intent intent = new Intent(context, QrInventoryActivity.class);
                    context.startActivity(intent);
                }
            }
        };


        List<Map> dataList = new ArrayList<>();
        playerController.getPlayer(databaseCallback, dataList,photoContent,"Identifier");
    }

}





