package com.qrpokemon.qrpokemon;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "FirestoreSearchActivity";
    private static final String PLAYER = "Player";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private ArrayAdapter<SearchItem> mAdapter;
    private Button backButton;
    private Button searchButton;
    private ArrayList<SearchItem> searchList;
    private SearchController searchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        ListView listView = findViewById(R.id.search_listview);
        backButton = findViewById(R.id.button);
        searchButton = findViewById(R.id.search_button);

        // Create and fill our list with player data
        searchList = new ArrayList<SearchItem>();
        searchController = SearchController.getInstance();
//        searchController.getSearch(this, searchList);



        Query query = mDb.collection(PLAYER)
                .orderBy("createdTime", Query.Direction.ASCENDING);

        mAdapter = new SearchList(this, new ArrayList<SearchItem>());
        listView.setAdapter(mAdapter);

        EditText searchBox = findViewById(R.id.sv);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_content = searchBox.getText().toString();
                Log.e("SearchActivity: ", "Seaching for " + search_content);
                searchController.getSearch(getApplicationContext(), searchList, search_content, mAdapter);
//                mAdapter.notifyDataSetChanged();
            }
        });
//        searchBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                Log.d(TAG, "SearchBox has changed to: " + s.toString());
////                Query query;
////                if (s.toString().isEmpty()) {
////                    query = mDb.collection(PLAYER)
////                            .orderBy("Identifier", Query.Direction.ASCENDING);
////                } else {
////                    query = mDb.collection(PLAYER)
////                            .whereEqualTo("Identifier", s.toString())
////                            .orderBy("Identifier", Query.Direction.ASCENDING);
////                }
//                searchController.getSearch(getApplicationContext(), searchList, s.toString());
//
//                mAdapter.notifyDataSetChanged();
//            }
//        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Return to calling activity
            }
        });

    }

}