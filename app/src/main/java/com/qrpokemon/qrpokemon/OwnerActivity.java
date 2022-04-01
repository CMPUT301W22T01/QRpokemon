package com.qrpokemon.qrpokemon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.qrpokemon.qrpokemon.controllers.PlayerController;

public class OwnerActivity extends AppCompatActivity {
    private PlayerController playerController = PlayerController.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
