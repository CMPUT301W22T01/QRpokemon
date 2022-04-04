package com.qrpokemon.qrpokemon.views.profile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qrpokemon.qrpokemon.R;
import com.qrpokemon.qrpokemon.controllers.ProfileController;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String name,phone,email;
    private TextView tv_name,tv_phone,tv_email;
    private ImageView iv_QR;
    private Button generateBt;
    private ProfileController profileController;
    private FloatingActionButton backButton;

    /**
     * Init Textview and button objects for the profile
     * @param savedInstanceState current instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile_activity);
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        backButton = findViewById(R.id.profile_back);
        iv_QR = findViewById(R.id.QR_profile_Iv);
        generateBt = findViewById(R.id.bt_generate);

//        name = sp.getString("name","");
//        phone = sp.getString("phone","");
//        email = sp.getString("email","");

        profileController = new ProfileController(this);

        name = profileController.getPlayerName();
        phone = profileController.getPlayerPhone();
        email = profileController.getPlayerEmail();

        tv_email.setText(email);
        tv_name.setText(name);
        tv_phone.setText(phone);
        backButton.setOnClickListener(this);
        generateBt.setOnClickListener(this);


    }

    /**
     * Check on which button user have entered
     * User can either choose to:
     *      go back to main menu
     *      generate a unique qr code for himself
     * @param view ProfileActivity's view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_back:
                finish();
                break;
            case R.id.bt_generate:
                Bitmap bitmap = profileController.generatePlayerQr();
                iv_QR.setImageBitmap(bitmap);
                break;
        }
    }
}
