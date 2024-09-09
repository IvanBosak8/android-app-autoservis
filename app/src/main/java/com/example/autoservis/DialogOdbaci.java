package com.example.autoservis;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class DialogOdbaci extends AppCompatDialogFragment {
    private DialogOdbaci.DialogLisener lisener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),R.style.DialogTema);
        builder.setMessage("Jeste li sigurni da želite odbaciti rezervaciju?")
                .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lisener.odbaciRezervaciju();
                    }
                }).setNegativeButton("NE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public interface DialogLisener{
        void odbaciRezervaciju();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            lisener=(DialogOdbaci.DialogLisener) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString()
                    +"nisi implementirato DialogLisener");
        }

    }
}