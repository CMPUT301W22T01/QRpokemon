package com.qrpokemon.qrpokemon.views.qrinventory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.qrpokemon.qrpokemon.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QrInventoryShowCommentsCustomAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> playerList;
    private HashMap<String, ArrayList<String>> commentsList;

    final private String TAG = "QrInventoryShowCommentsCustomAdapter";

    // constructor
    public QrInventoryShowCommentsCustomAdapter(ArrayList<String> playerList, HashMap<String, ArrayList<String>> commentsList) {
        this.playerList = playerList;
        this.commentsList = commentsList;
    }

    @Override
    public int getGroupCount() {
        return playerList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return commentsList.get(playerList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return playerList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentsList.get(playerList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        // initialize view
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.qr_inventory_show_comments_list_content, viewGroup, false);

        // initialize textview
        TextView playerTV = view.findViewById(R.id.tv_player);
        String sPlayer = String.valueOf(getGroup(i));

        // set text
        playerTV.setText(sPlayer);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        // initialize view
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.qr_inventory_show_comments_list_child, viewGroup, false);

        // initialize textview
        TextView playerTV = view.findViewById(R.id.tv_player_comments);
        String sComments = String.valueOf(getChild(i, i1));

        // set text
        playerTV.setText(sComments);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
