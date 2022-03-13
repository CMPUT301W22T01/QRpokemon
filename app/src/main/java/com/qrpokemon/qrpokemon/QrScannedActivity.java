package com.qrpokemon.qrpokemon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class QrScannedActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView photo;
    private Switch photoSwitch;
    private Switch locationSwitch;
    private Button scanBt;
    private Boolean photoSwitchState;
    private Boolean locationSwitchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_code_found);

        photo = findViewById(R.id.code_not_found_imageView);
        photoSwitch = findViewById(R.id.photo_switch_code_not_found);
        locationSwitch = findViewById(R.id.location_switch_code_not_found);
        scanBt = findViewById(R.id.scan_Button_no_code_found);

        photoSwitchState = photoSwitch.isChecked();
        locationSwitchState = locationSwitch.isChecked();

        scanBt.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_Button_no_code_found:  // press scan button and scan the code
                break;
        }
    }
}