package com.qrpokemon.qrpokemon;
import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

        import java.util.ArrayList;

public class SearchList extends ArrayAdapter<SearchItem> {

    private ArrayList<SearchItem> searchItems;
    private Context context;

    public SearchList(@NonNull Context context, ArrayList<SearchItem> searchItems) {
        super(context, 0, searchItems);
        this.searchItems = searchItems;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_list, parent, false);
        }

        SearchItem searchItem = searchItems.get(position);
        TextView userName = view.findViewById(R.id.tv);


        userName.setText(searchItem.getUsername());


        return view;
    }
}