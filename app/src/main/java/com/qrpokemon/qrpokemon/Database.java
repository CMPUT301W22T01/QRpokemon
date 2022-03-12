package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Database {
    final private FirebaseFirestore db;
    final private String[] collections = {"Player", "QrCode", "LocationIndex"};
    private List<Map> list;

    Database() {
        db = FirebaseFirestore.getInstance();
    }

    public void checkValidCollection(String collection) throws Exception {
        for (String s : collections) {
            if (collection.equals(s)) {
                return;
            }
        }

        throw new Exception("Invalid Database Collection");
    }

    /**
     * Get an array of documents from the database
     * @param collection The collection to search in.
     *                   May be one of [Player, QrCode, Location].
     * @param objectName An object within the collection.
     *                   May be null to return the collection.
     * @return List<Map>
     * @throws Exception Throws an exception if collection is invalid.
     */
    public void getData(DatabaseCallback callback, List<Map> list, String collection,
                        String objectName) throws Exception {

        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);
        Task task = objectName == null ? collectionReference.get()
                : collectionReference.whereEqualTo("Identifier", objectName).get();

        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // TODO: Use documentSnapshot.toObject() with HashMap to reconstruct objects?
                    for (QueryDocumentSnapshot document : task.getResult())
                        list.add(document.getData());

                    if (callback != null)
                        callback.run(list);

                } else {
                    Log.e("Database: ", "Failed to add document");
                }
            }
        });
    }

    /**
     * Write data to an object in a collection
     * @param collection The collection to search in.
     *                   May be one of [Player, QrCode, Location].
     * @param objectName An object within the collection.
     *                   May be null to return the collection.
     * @param data A map containing the attributes of an object.
     * @param overwrite A boolean flag if the provided data should truncate and overwrite all
     *                  attributes.
     * @throws Exception Throws an exception if collection is invalid.
     */
    public void writeData(String collection, String objectName, HashMap data, Boolean overwrite)
            throws Exception {
        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);

        if (overwrite)
            collectionReference.document(objectName).set(data);
        else
            collectionReference.document(objectName).update(data);
    }

    public void writeData(String collection, String objectName,
                          HashMap data) throws Exception {
        writeData(collection, objectName, data, false);
    }

    /**
     * Delete a document from a collection
     * @param collection The collection to search in.
     *                   May be one of [Player, QrCode, Location].
     * @param objectName An object within the collection. Must be specified.
     * @throws Exception Throws an exception if collection is invalid or objectName is null.
     */
    public void deleteData(String collection, String objectName) throws Exception {
        // Check for valid collection reference
        checkValidCollection(collection);

        if (objectName == null)
            throw new Exception("Parameter objectName cannot be null");

        CollectionReference collectionReference = db.collection(collection);
        collectionReference.document(objectName).delete();
    }
}
