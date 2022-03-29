package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertTrue;

import com.qrpokemon.qrpokemon.models.Player;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerUnitTest {

    private ArrayList<String> qrInventory = new ArrayList<>();
    private HashMap<String,String> contactInfo = new HashMap<>();

    private Player mockPlayer(){
        qrInventory.add("hash0");
        qrInventory.add("hash1");

        contactInfo.put("email", "abc@ualberta.ca");
        contactInfo.put("phone", "74188965532");

        return new Player("Hatsune", qrInventory, contactInfo, 100, 100, "aaabbb", 100, 1);
    }

    @Test
    void testGetAndSetUserName(){
        Player player = mockPlayer();
        assertTrue(player.getUsername().equals("Hatsune"));

        player.setUsername("abc");
        assertTrue(player.getUsername().equals("abc"));
    }
    @Test
    void testGetAndSetQrCount(){
        Player player = mockPlayer();
        assertTrue(player.getQrCount().equals(100));

        player.setQrCount(123456);
        assertTrue(player.getQrCount().equals(123456));
    }
    @Test
    void testGetAndSetTotalScore(){
        Player player = mockPlayer();
        assertTrue(player.getTotalScore().equals(100));

        player.setTotalScore(1234567);
        assertTrue(player.getTotalScore().equals(1234567));
    }

    @Test
    void testGetAndSetQrInventory(){
        Player player = mockPlayer();
        assertTrue(player.getQrInventory().equals(qrInventory));

        player.setQrInventory(qrInventory);
        assertTrue(player.getQrInventory().equals(qrInventory));
    }

    @Test
    void testGetAndSetContactInfo(){
        Player player = mockPlayer();
        assertTrue(player.getContactInfo().equals(contactInfo));

        player.setContactInfo(contactInfo);
        assertTrue(player.getContactInfo().equals(contactInfo));
    }
}