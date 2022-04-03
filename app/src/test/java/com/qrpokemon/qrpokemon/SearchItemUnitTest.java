package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertTrue;

import com.qrpokemon.qrpokemon.views.search.SearchItem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class SearchItemUnitTest {
    private ArrayList<String> qrList = new ArrayList<>();
    private SearchItem mockSearchItem(){
        qrList.add("abc");
        qrList.add("bcd");
        qrList.add("efg");

        return new SearchItem("Yu","Yu@gmail.com","123456789",qrList);
    }
    @Test
    void testGetIdentifier(){
        SearchItem searchItem = mockSearchItem();
        assertTrue(searchItem.getIdentifier().equals("Yu"));
    }
    @Test
    void testGetEmail(){
        SearchItem searchItem = mockSearchItem();
        assertTrue(searchItem.getEmail().equals("Yu@gmail.com"));
    }
    @Test
    void testGetPhone(){
        SearchItem searchItem = mockSearchItem();
        assertTrue(searchItem.getPhone().equals("123456789"));
    }
    @Test
    void testGetQrList(){
        SearchItem searchItem = mockSearchItem();
        assertTrue(searchItem.getQrList().equals(qrList));
    }
}
