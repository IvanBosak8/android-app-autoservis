package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import models.Rezervacija;
import models.User;


public class NedovrseneRezervacijeDetalji extends AppCompatActivity {

    private Button izadi;
    private TextView imePrezime;
    private TextView broj;
    private TextView auto;
    private TextView godinaProizvodnje;
    private TextView brojSasije;
    private TextView popravak;
    private TextView datumPrimanja;
    private TextView dan;
    private TextView brojRezervacije;
    private TextView datumZavrsetka;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedovrsene_rezervacije_detalji);
        izadi= findViewById(R.id.izađi);
        imePrezime=findViewById(R.id.ime_i_prezime);
        broj=findViewById(R.id.broj);
        auto=findViewById(R.id.auto);
        godinaProizvodnje=findViewById(R.id.godinaProizvodnje);
        brojSasije=findViewById(R.id.brojSasije);
        popravak=findViewById(R.id.popravak);
        datumPrimanja=findViewById(R.id.datum_primanja);
        dan=findViewById(R.id.dan);
        brojRezervacije=findViewById(R.id.brojRezervacije);
        datumZavrsetka=findViewById(R.id.datum_zavrsetka);


        User trenutniUser=(User)getIntent().getSerializableExtra("trenutniUser");
        Rezervacija rezervacija=(Rezervacija)getIntent().getSerializableExtra("trenutnaRezervacija");

        assert rezervacija!=null;

        dan.setText(rezervacija.getDan());
        if(trenutniUser==null){
            imePrezime.setText(String.format(("%s %s"),rezervacija.getUser().getIme(),rezervacija.getUser().getPrezime()));
            broj.setText(rezervacija.getUser().getBroj());
        }
        else {
            imePrezime.setText(String.format("%s %s",trenutniUser.getIme(),trenutniUser.getPrezime()));
            broj.setText(trenutniUser.getBroj());
        }
        auto.setText(rezervacija.getAuto());
        godinaProizvodnje.setText(String.valueOf(rezervacija.getGodinaProizvodnje()));
        brojSasije.setText(rezervacija.getBrojSasije());
        popravak.setText(rezervacija.getPopravak());
        datumPrimanja.setText(rezervacija.getDatumPrimanja());
        brojRezervacije.setText(String.valueOf(rezervacija.getBrojRezervacije()));
        datumZavrsetka.setText(rezervacija.getDatumZavrsetka());

        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}