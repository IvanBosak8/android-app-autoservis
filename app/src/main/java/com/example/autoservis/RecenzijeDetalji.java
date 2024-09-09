package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import models.Recenzija;
import models.Rezervacija;
import models.User;

public class RecenzijeDetalji extends AppCompatActivity {
    private Button izadi;
    private TextView recenzija1;
    private TextView ocjena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzije_detalji);
        izadi=findViewById(R.id.izađi);
        ocjena=findViewById(R.id.ocjena);
        recenzija1=findViewById(R.id.recenzija);

        Recenzija recenzija=(Recenzija) getIntent().getSerializableExtra("trenutnaRecenzija");

        assert recenzija!=null;

        recenzija1.setText(recenzija.getRecenzija());
        ocjena.setText(String.format(("%s/5"), recenzija.getOcjena()));
        String sOcjena= String.valueOf(ocjena);



        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}