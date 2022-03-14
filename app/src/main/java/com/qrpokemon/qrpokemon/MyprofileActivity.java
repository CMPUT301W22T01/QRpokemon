package com.qrpokemon.qrpokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyprofileActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String name,phone,email;
    private TextView tv_name,tv_phone,tv_email;
    private Button bt_back;
    private ImageView iv_back;
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

        name = sp.getString("name","");
        phone = sp.getString("phone","");
        email = sp.getString("email","");
        tv_email.setText(email);
        tv_name.setText(name);
        tv_phone.setText(phone);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
