package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
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
     * load current player from local
     * @param context context of current activity
     */
    public void load(Context context){
        String data = null;
             // load local user
            List<Map> result = new ArrayList<Map>();
            try{
                DatabaseCallback databaseCallback = new DatabaseCallback(context) {
                //this runs after datalist is collected from Database class
                @Override
                public void run(List<Map> dataList) {
                    if (!dataList.isEmpty()) {
                        //Display current player's username on top of the activity
                        TextView text = ((MainActivity) context).findViewById(R.id.user_textView);
                        Log.e("Main menuController: ", "Current player is : " +dataList.toString());
                        text.setText((String) dataList.get(0).get("Identifier"));
                    }
                }
            };
            // read player's data from firestore
            database.getData(databaseCallback, result, "Player", fileSystemController.readToFile(context, "name"));
            Log.e("Main menuController: ", "Current player is : " +fileSystemController.readToFile(context, "name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
