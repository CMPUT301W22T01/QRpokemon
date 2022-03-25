package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
        TextView userEmail = view.findViewById(R.id.search_email);
        TextView userPhone = view.findViewById(R.id.search_phone);
        ImageView qrcode = view.findViewById(R.id.imageView);


        userName.setText(searchItem.getIdentifier());
        if (searchItem.getEmail() != null && searchItem.getPhone() != null) {
            Log.e("Search Controller: ", "email found");
            userEmail.setText("email: " + searchItem.getEmail());
            userPhone.setText("phone: " + searchItem.getPhone());
            qrcode.setImageResource(R.drawable.profile_avadar);
        }



        return view;
    }


}