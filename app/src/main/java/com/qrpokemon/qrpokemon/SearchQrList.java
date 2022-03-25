package com.qrpokemon.qrpokemon;
import android.content.Context;
import android.graphics.Bitmap;
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

public class SearchQrList extends ArrayAdapter<String> {

    private ArrayList<String> qrList;
    private Context context;

    public SearchQrList(@NonNull Context context, ArrayList<String> qrList) {
        super(context, 0, qrList);
        this.qrList = qrList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_list, parent, false);
        }

        String qr = qrList.get(position);
        TextView userName = view.findViewById(R.id.tv);
        ImageView qrcode = view.findViewById(R.id.imageView);


//        userName.setText(searchItem.getIdentifier());

        MultiFormatWriter writer = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix matrix = writer.encode(qr, BarcodeFormat.QR_CODE,
                    350, 350);
            //Initialize the barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();

            //Initialize the Bitmap
            bitmap = encoder.createBitmap(matrix);

            //Initialize input manager
            InputMethodManager manager = (InputMethodManager) context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );

        } catch (WriterException e) {
            e.printStackTrace();
        }

        qrcode.setImageBitmap(bitmap);


        return view;
    }

}


