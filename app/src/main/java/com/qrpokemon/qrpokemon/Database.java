package com.qrpokemon.qrpokemon;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    final private FirebaseFirestore db;
    final private String[] collections = {"Player", "QrCode", "LocationIndex"};
    private List <DocumentSnapshot> list;

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
    public List<Map> getData(String collection, String objectName) throws Exception {

        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);

        if (objectName == null) {
            // No specific object needed so return all objects
            collectionReference.get().addOnSuccessListener(queryDocumentSnapshots ->
                list = queryDocumentSnapshots.getDocuments()
            );
        }
        else {
            // Get object with Identifier objectName
            collectionReference.whereEqualTo("Identifier", objectName).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                            list = queryDocumentSnapshots.getDocuments()
                    );
        }

        ArrayList<Map> returnList = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                returnList.set(i, list.get(i).getData());
            }
        }

        return returnList;
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
    public void writeData(String collection, String objectName, HashMap data,
                          Boolean overwrite) throws Exception {
        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);
        if (overwrite)
            collectionReference.document(objectName).update(data);
        else
            collectionReference.document(objectName).set(data);
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
