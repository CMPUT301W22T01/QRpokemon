package com.qrpokemon.qrpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String name,phone,email;
    private TextView tv_name,tv_phone,tv_email;
    private ImageView iv_back, iv_QR;
    private Button generateBt;
    private ProfileController profileController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile_activity);
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        iv_back = findViewById(R.id.iv_back);
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
        iv_back.setOnClickListener(this);
        generateBt.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_generate:
                Bitmap bitmap = profileController.generatePlayerQr();
                iv_QR.setImageBitmap(bitmap);
                break;
        }
    }
}
