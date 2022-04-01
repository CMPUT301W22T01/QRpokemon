package com.qrpokemon.qrpokemon.controllers;

import android.content.Context;
import android.graphics.Bitmap;
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
    Context mContext;

    public ProfileController(Context mContext) {
        this.mContext = mContext;
        this.currentPlayerController = PlayerController.getInstance();

        try {
            currentPlayer = currentPlayerController.getPlayer(null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerContact = (HashMap) currentPlayer.get("contact");

    }



    /**
     * get the name of the current user
     * @return a String of the user name
     */
    public static String getPlayerName(){
        String playerName = currentPlayer.get("Identifier").toString();
        return playerName;
    }

    /**
     * get the email of the current user
     * @return a String of the user email
     */
    public static String getPlayerEmail(){
        String playerEmail;
        try {
            playerEmail = playerContact.get("email").toString();
        } catch (Exception e) {
            playerEmail = "null";
        }
        return playerEmail;
    }

    /**
     * get the phone number of the current user
     * @return a String of the user phone number
     * @throws Exception if player doens't have a phone number
     */
    public static String getPlayerPhone(){
        String playerPhone;
        try{
            playerPhone = playerContact.get("phone").toString();
        } catch (Exception e) {
            playerPhone = "null";
        }

        return playerPhone;
    }

    /**
     * generate a QR code of the player
     * scanning the code will get the player's username
     * @return a Bitmap as the QR code
     */
    public Bitmap generatePlayerQr(){
        MultiFormatWriter writer=new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix matrix = writer.encode(currentPlayer.get("Identifier").toString(), BarcodeFormat.QR_CODE,
                    350, 350);
            //Initialize the barcode encoder
            BarcodeEncoder encoder=new BarcodeEncoder();

            //Initialize the Bitmap
            bitmap = encoder.createBitmap(matrix);

            //Initialize input manager
            InputMethodManager manager=(InputMethodManager) mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );

        }catch (WriterException e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
