package com.qrpokemon.qrpokemon;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;

public class Database {
    private FirebaseFirestore db;
    private CollectionReference players, qrCode, locationIndex;

    Database() {
        db = FirebaseFirestore.getInstance();
        players = db.collection("Player");
        qrCode = db.collection("QrCode");
        locationIndex = db.collection("LocationIndex");
    }

    public HashMap getData() {


        return null;
    }

    public void writeData(String collection, HashMap data) {
        players
            .document(collection)  // Player username
            .set(data);  // User attributes
    }
}