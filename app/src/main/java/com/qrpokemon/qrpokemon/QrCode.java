package com.qrpokemon.qrpokemon;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrCode {

    private String identifier;
    private Integer score;
    private ArrayList<String> location;
    private HashMap<String,ArrayList<String>> comments;
    private HashMap<String,Bitmap> bitmap;

    /**
     *
     * @param identifier "identifier" is the hashcode
     * @param score       "score" is the score of a qrCode if it is not a profile qrCode
     * @param location    "location" is the place where location where it can be found
     * @param comments    "comment" is the comments left by the current player or someone else
     * @param bitmap      "bitmap"is the photo of this qrCode
     */
    public QrCode (String identifier, Integer score, ArrayList<String > location, HashMap<String,ArrayList<String>> comments, HashMap<String,Bitmap> bitmap)  {
        this.identifier = identifier;
        this.score = score;
        this.location = location;
        this.comments = comments;
        this.bitmap = bitmap;
    }

    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public HashMap<String, ArrayList<String>> getComments() {
        return comments;
    }

    public void setComments(HashMap<String,ArrayList<String>> comments) {
        this.comments = comments;
    }

    public HashMap<String, Bitmap> getBitmap() { return bitmap; }

    public void setBitmap(HashMap<String,Bitmap> bitmap) { this.bitmap = bitmap; }
}