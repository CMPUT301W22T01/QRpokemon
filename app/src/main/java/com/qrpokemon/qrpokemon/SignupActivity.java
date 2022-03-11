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
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        fileSystemController.writeToFile(this,"first run", "true");


        et_email = findViewById(R.id.et_email);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        bt_submit = findViewById(R.id.bt_submit);
        tv_have = findViewById(R.id.tv_have);

        bt_submit.setOnClickListener(this);
        tv_have.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_have:
                Intent intent = new Intent(SignupActivity.this, SignupActivity.class);
                startActivity(intent);
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
                editor.commit();
                Intent intent1 = new Intent(SignupActivity.this, MyprofileActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
