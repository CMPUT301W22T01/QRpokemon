package com.qrpokemon.qrpokemon.activities.qrinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.qrpokemon.qrpokemon.R;

public class QrInventoryAddCommentFragment extends DialogFragment {
    private EditText editComment;
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener{
        void addComment(String comment) throws Exception;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "exception");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.qr_inventory_comment_fragment, null);
        editComment = view.findViewById(R.id.edit_comment);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Enter you comment here: ")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String comment = editComment.getText().toString();

                        try {
                            listener.addComment(comment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
    }
}
