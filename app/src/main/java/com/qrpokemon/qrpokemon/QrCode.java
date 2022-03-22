package com.qrpokemon.qrpokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrCode {

    private String indentifier;
    private Integer score;
    private String location;
    private ArrayList<String> comments;

    /**
     *
     * @param indentifier "identifier" is the hashcode
     * @param score       "score" is the score of a qrCode if it is not a profile qrCode
     * @param location    "location" is the place where location where it can be found
     * @param comments    "comment" is the comments left by the current player or someone else
     */
    public QrCode (String indentifier, Integer score, String location, ArrayList<String> comments) {
        this.indentifier = indentifier;
        this.score = score;
        this.location = location;
        this.comments = comments;
    }

    public String getIndentifier() {
        return indentifier;
    }

    public void setIndentifier(String indentifier) {
        this.indentifier = indentifier;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}