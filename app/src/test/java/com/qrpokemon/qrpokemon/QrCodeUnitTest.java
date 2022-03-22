package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class QrCodeUnitTest {

    private ArrayList<String> comments = new ArrayList<>();

    private QrCode mockQrCode(){

        comments.add("good");
        comments.add("bad");
        comments.add("so bad");

        return new QrCode("abc", 100, "28ave", comments);
    }
    @Test
    void testGetAndSetIndentifier(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getIndentifier().equals("abc"));

        qrcode.setIndentifier("def");
        assertTrue(qrcode.getIndentifier().equals("def"));
    }
    @Test
    void testGetAndSetScore(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getScore().equals(100));

        qrcode.setScore(1200);
        assertTrue(qrcode.getScore().equals(1200));
    }
    @Test
    void testGetAndSetLocation(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getLocation().equals("28ave"));

        qrcode.setLocation("42ave");
        assertTrue(qrcode.getLocation().equals("42ave"));
    }
    @Test
    void testGetAndSetComments(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getComments().equals(comments));

        qrcode.setComments(comments);
        assertTrue(qrcode.getComments().equals(comments));
    }
}