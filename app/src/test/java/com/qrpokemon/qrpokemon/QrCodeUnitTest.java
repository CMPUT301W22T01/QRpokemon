package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class QrCodeUnitTest {

    private HashMap<String, ArrayList<String>> comments = new HashMap<>();

    private QrCode mockQrCode(){
        ArrayList<String> comment = new ArrayList<>();
        comment.add("good");
        comment.add("bad");
        comment.add("so bad");
        comments.put("user1",comment);
        ArrayList<String> location = new ArrayList<>();
        location.add("28ave");
        return new QrCode("abc", 100,location , comments,null);
    }
    @Test
    void testGetAndSetIdentifier(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getIdentifier().equals("abc"));

        qrcode.setIdentifier("def");
        assertTrue(qrcode.getIdentifier().equals("def"));
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
        assertTrue(qrcode.getLocation().get(0).equals("28ave"));

        ArrayList<String> location = new ArrayList<>();
        location.add("42ave");
        qrcode.setLocation(location);
        assertTrue(qrcode.getLocation().get(0).equals("42ave"));
    }
    @Test
    void testGetAndSetComments(){
        QrCode qrcode = mockQrCode();
        assertTrue(qrcode.getComments().equals(comments));

        qrcode.setComments(comments);
        assertTrue(qrcode.getComments().equals(comments));
    }

    @Test
    void testGetAndSetBitmap(){
        QrCode qrcode = mockQrCode();
        assertNull(qrcode.getBitmap());

        qrcode.setBitmap(null);
        assertNull(qrcode.getBitmap());

    }
}