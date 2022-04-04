package com.qrpokemon.qrpokemon;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.qrpokemon.qrpokemon.views.qrinventory.QrInventoryShowCommentsCustomAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class QrInventoryShowCommentsCustomAdapterUnitTest {
    public ArrayList<String> mockPlayerList = new ArrayList<>(), arrayList;
    public HashMap<String, ArrayList<String>> mockCommentsList = new HashMap<>();

    public void setUp(){
        char ch = 0;
        String str = new String();
        arrayList = new ArrayList<>();
        arrayList.add("123123");
        arrayList.add("321321");
        for(int i = 0; i < 20; i++){
            ch = (char) (i + 65);
            str = "" + ch;
            mockPlayerList.add(i, str);
            mockCommentsList.put(str, arrayList);
        }
    }

    @Test
    void testGetGroupCount(){
        setUp();
        QrInventoryShowCommentsCustomAdapter adapter = new QrInventoryShowCommentsCustomAdapter(mockPlayerList, mockCommentsList);
        System.out.println("Size of mockPlayerList is " + mockPlayerList.size());
        assertTrue( 20 == adapter.getGroupCount());
    }

    @Test
    void testGetGroupChildCount(){
        setUp();
        QrInventoryShowCommentsCustomAdapter adapter = new QrInventoryShowCommentsCustomAdapter(mockPlayerList, mockCommentsList);
        assertTrue(2 == adapter.getChildrenCount(0));
        assertTrue(2 == adapter.getChildrenCount(1));
    }

    @Test
    void testGetGroup(){
        setUp();
        QrInventoryShowCommentsCustomAdapter adapter = new QrInventoryShowCommentsCustomAdapter(mockPlayerList, mockCommentsList);
        assertEquals("A", adapter.getGroup(0));
        assertEquals("B", adapter.getGroup(1));
    }

    @Test
    void testGetChild(){
        setUp();
        QrInventoryShowCommentsCustomAdapter adapter = new QrInventoryShowCommentsCustomAdapter(mockPlayerList, mockCommentsList);
        assertEquals("123123", adapter.getChild(0, 0));
        assertEquals("321321", adapter.getChild(0, 1));
    }
}

