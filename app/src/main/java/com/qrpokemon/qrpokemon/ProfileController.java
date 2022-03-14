package com.qrpokemon.qrpokemon;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;



public class ProfileController {




    //Get playerController class there is only one PlayerController class
    private PlayerController currentPlayerController;
    static HashMap currentPlayer;
    static HashMap playerContact;

    public ProfileController() {
        this.currentPlayerController = PlayerController.getInstance();

        try {
            currentPlayer = currentPlayerController.getPlayer(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerContact = (HashMap) currentPlayer.get("contact");

    }



    public static String getPlayerName(){
        String playerName = currentPlayer.get("Identifier").toString();
        return playerName;
    }

    public static String getPlayerEmail(){
        String playerEmail = playerContact.get("email").toString();
        return playerEmail;
    }

    public static String getPlayerPhone(){
        String playerPhone = playerContact.get("phone").toString();
        return playerPhone;
    }

//    public Bitmap generatePlayerQr(){
//
//    }

}
