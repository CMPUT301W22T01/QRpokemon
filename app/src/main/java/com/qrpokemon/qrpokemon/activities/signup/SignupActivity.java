package com.qrpokemon.qrpokemon.activities.signup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.PlayerController;
import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener,Observer {
    private EditText et_name,et_phone,et_email;
    private String name,phone,email;
    private Button bt_submit;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private TextView tv_have;
    private PlayerController playerController;
    private SignupController signupController = SignupController.getInstance();

    /**
     * Init Textview and button objects and check if the app is in the first run
     * If user is registered successfully, then the app won't display this Activity ever
     * @param savedInstanceState saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DatabaseCallback databaseCallback = new DatabaseCallback(this) {
            @Override
            public void run(List<Map> dataList) {
                if (!dataList.isEmpty()){// user found, login automatically
                    Log.e("SignupActivity: ",dataList.toString());
                    signupController.write((String) dataList.get(0).get("Identifier"),
                                        (ArrayList<String>) dataList.get(0).get("qrInventory"),
                                        (HashMap<String, String>) dataList.get(0).get("contact"),
                                        ((Long) dataList.get(0).get("qrCount")).intValue(),
                                        ((Long) dataList.get(0).get("totalScore")).intValue(),
                                        ((Long) dataList.get(0).get("highestUnique")).intValue(),
                                        Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); //init Player class locally via PlayerController

                    Log.e("SignupAcitivity: get player ","" + ((String) dataList.get(0).get("Identifier")));
                    finish();
                } else {
                    setContentView(R.layout.signup_activity);
                    et_email = findViewById(R.id.et_email);
                    et_name = findViewById(R.id.et_name);
                    et_phone = findViewById(R.id.et_phone);
                    bt_submit = findViewById(R.id.bt_submit);
                    tv_have = findViewById(R.id.tv_have);

                    bt_submit.setOnClickListener(SignupActivity.this);
                    tv_have.setOnClickListener(SignupActivity.this);
                }
            }
        };
        super.onCreate(savedInstanceState);
        try {
            signupController.load(databaseCallback, new ArrayList<>(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SignupActivity Error: ", e.toString());
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) { //restore session is over
                playerController = PlayerController.getInstance();
                try {
                    if (playerController.getPlayer(null, null, null, null).get("Identifier") != null) {
                        finish();
                    } else {
                        Log.e("SignupActivity: ", "User/QR not found, login failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SignupActivity: ", e.toString());
                }

            }
        });

    }

    /**
     * Check on which button user have entered
     * User can either choose to:
     *      login from new device
     *      register with filled in message: username cannot be null or identical to others in database
     * User can leave null to contact info
     * User goes to MainMenuActivity once registration is done.
     * @param view SignupActivity's view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_have:
                Intent signup_intent = new Intent(SignupActivity.this, RestoreSession.class);
                activityResultLauncher.launch(signup_intent);
                break;

            case R.id.bt_submit:
                name = et_name.getText().toString().trim();
                email = et_email.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                if(name.equals("")){
                    Toast.makeText(this, "Name is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.equals("")){
//                    Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
                    email = null;
                }
                if(phone.equals("")){
//                    Toast.makeText(this, "Phone number is empty!", Toast.LENGTH_SHORT).show();
                    phone = null;
                }

                try{
                    playerController = PlayerController.getInstance();
                    playerController.addObserver(this);
                    signupController.addNewPlayer(this, name, email, phone, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                } catch (Exception e) {
                    Log.e("TrackRecordActivity", String.valueOf(e));
                }
                break;
        }
    }

    /**
     * update will be called once a player has been successfully added
     * @param observable observable object
     * @param o object being observed
     */
    @Override
    public void update(Observable observable, Object o) {
        finish();
    }
}
