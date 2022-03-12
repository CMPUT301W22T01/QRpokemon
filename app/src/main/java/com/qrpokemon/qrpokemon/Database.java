package com.qrpokemon.qrpokemon;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Database dbInstance;
    final private FirebaseFirestore db;
    final private String[] collections = {"Player", "QrCode", "LocationIndex"};

    private Database() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Implements Database as a Singleton
     * @return Database
     */
    public static Database getInstance() {
        if (dbInstance == null)
            dbInstance = new Database();

        return dbInstance;
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
     * @param callback   A DatabaseCallback used to run code after data has been fetched
     * @param list       A list which will have fetched data appended to it
     * @param collection The collection to search in.
     *                   May be one of [Player, QrCode, Location].
     * @param objectName An object within the collection.
     *                   May be null to return the collection.
     * @throws Exception Throws an exception if collection is invalid.
     */
    public void getData(DatabaseCallback callback, List<Map> list, String collection,
                        String objectName) throws Exception {

        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);
        Task task = objectName == null ? collectionReference.get()
                : collectionReference.whereEqualTo("Identifier", objectName).get();

        task.addOnCompleteListener((OnCompleteListener<QuerySnapshot>) runningTask -> {
            if (runningTask.isSuccessful()) {
                // TODO: Use documentSnapshot.toObject() with HashMap to reconstruct objects?
                for (QueryDocumentSnapshot document : runningTask.getResult())
                    list.add(document.getData());

                if (callback != null)
                    callback.run(list);

            } else {
                Log.e("Database: ", "Failed to add document");
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
