package com.qrpokemon.qrpokemon;

import java.util.HashMap;
import java.util.Map;

public class QrCode {

    private String indentifier;
    private Integer score;
    private String location;
    private HashMap<Object,Object> comments;

    public QrCode (String indentifier, Integer score, String location, HashMap<Object,Object> comments) {
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

    public HashMap<Object, Object> getComments() {
        return comments;
    }

    public void setComments(HashMap<Object, Object> comments) {
        this.comments = comments;
    }
}
