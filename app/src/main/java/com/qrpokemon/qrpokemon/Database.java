package com.qrpokemon.qrpokemon;

import android.util.Log;

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

    Database() throws Exception {
        db = FirebaseFirestore.getInstance();

        if (db == null) {
            Log.e("Database", "Database failed to initialize");
            throw new Exception("Database failed to initialize");

        }
    }

    public void checkValidCollection(String collection) throws Exception {
        for (int i = 0; i < collections.length; i++) {
            if (collection == collections[i]) {
                return;
            }
        }

        throw new Exception("Invalid Database Collection");
    }

    /**
     * Get an array of documents from the database
     * @param collection
     * @param objectName
     * @return List<Map>
     * @throws Exception
     */
    public List<Map> getData(String collection, String objectName) throws Exception {

        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);

        if (objectName == null) {
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    // No specific object needed so return all objects
                        list = queryDocumentSnapshots.getDocuments();
                }
            });
        }
        else {
            // Get object with Identifier objectName
            collectionReference.whereEqualTo("Identifier", objectName).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list = queryDocumentSnapshots.getDocuments();
                        }
                    }
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
     * @param collection
     * @param objectName
     * @param data
     * @param overwrite
     * @throws Exception
     */
    public void writeData(String collection, String objectName, HashMap data, Boolean overwrite) throws Exception {
        // Check for valid collection reference
        checkValidCollection(collection);

        CollectionReference collectionReference = db.collection(collection);

        if (overwrite)
            collectionReference.document(objectName).update(data);  //  Player username & User attributes
        else
            collectionReference.document(objectName).set(data);  //  Player username & User attributes
    }

    public void writeData(String collection, String objectName, HashMap data) throws Exception {
        writeData(collection, objectName, data, false);
    }

    /**
     * Delete a document from a collection
     * @param collection
     * @param objectName
     * @throws Exception
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
