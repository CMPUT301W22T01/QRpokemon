package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.qrpokemon.qrpokemon.activities.signup.SignupActivity;
import com.qrpokemon.qrpokemon.activities.signup.SignupController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenuController {
    private static MainMenuController currentInstance;
    private DatabaseController database = DatabaseController.getInstance();
    private FileSystemController fileSystemController = new FileSystemController();

    public static MainMenuController getInstance() {
        if (currentInstance == null)
            currentInstance = new MainMenuController();

        return currentInstance;
    }

    /**
     * load current username from firestore
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
}
