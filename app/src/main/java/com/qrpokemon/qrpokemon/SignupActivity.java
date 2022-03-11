package com.qrpokemon.qrpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {
    private EditText et_name,et_phone,et_email;
    private String name,phone,email;
    private Button bt_submit;
    private TextView tv_have;
    private FileSystemController fileSystemController;

    /**
     * Init Textview and button objects and check if the app is in the first run
     * If user is registered successfully, then the app won't display this Activity ever
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        if(load("firstRun") == null) { // program is in first run:
            fileSystemController.writeToFile(this,"firstRun", "firstRun");
        }
        else { //app has run before
            if (load("name") != null){ //if a user has been successfully created
                Intent intent = new Intent(SignupActivity.this, MyprofileActivity.class);
                startActivity(intent);
                finish(); // we won't be back once user has correctly registered the app
            }
        }

        et_email = findViewById(R.id.et_email);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        bt_submit = findViewById(R.id.bt_submit);
        tv_have = findViewById(R.id.tv_have);

        bt_submit.setOnClickListener(this);
        tv_have.setOnClickListener(this);
    }

    /**
     * Check on which button user have entered,
     * From here, user can either login from new device
     * or register with filled in message, username cannot be null or identical to others in database
     * User can go to MainMenuActivity once registration is done.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_have:
                Intent signup_intent = new Intent(SignupActivity.this, SignupActivity.class);
                startActivity(signup_intent);
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
                    Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.equals("")){
                    Toast.makeText(this, "Phone is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                fileSystemController.writeToFile(this, "name", name);

                //TODO: Change the Activity to MainMenuActivity once it is ready.
                Intent intent = new Intent(SignupActivity.this, MyprofileActivity.class);
                startActivity(intent);
                break;
        }
    }

    public String load(String filename){
        String data = fileSystemController.readToFile(this,filename);
        return data;
    }
}
