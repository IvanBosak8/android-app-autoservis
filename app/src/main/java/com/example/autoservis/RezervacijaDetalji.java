package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import models.Rezervacija;
import models.User;

public class RezervacijaDetalji extends AppCompatActivity {

    private Button izadi;
    private TextView imePrezime;
    private TextView broj;
    private TextView auto;
    private TextView godinaProizvodnje;
    private TextView brojSasije;
    private TextView popravak;
    private TextView datumPrimanja;
    private TextView datumZavrsetka;
    private TextView radniSati;
    private TextView radnik;
    private TextView cijena;
    private TextView dan;
    private TextView brojRezervacije;
    private TextView cijenaRada;
    private TextView cijenaDjelova;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija_detalji);

        izadi= findViewById(R.id.izađi);
        imePrezime=findViewById(R.id.ime_i_prezime);
        broj=findViewById(R.id.broj);
        auto=findViewById(R.id.auto);
        godinaProizvodnje=findViewById(R.id.godinaProizvodnje);
        brojSasije=findViewById(R.id.brojSasije);
        popravak=findViewById(R.id.popravak);
        datumPrimanja=findViewById(R.id.datum_primanja);
        datumZavrsetka=findViewById(R.id.datum_zavrsetka);
        radniSati=findViewById(R.id.radni_sati);
        radnik=findViewById(R.id.radnik);
        cijena=findViewById(R.id.cijena);
        dan=findViewById(R.id.dan);
        brojRezervacije=findViewById(R.id.brojRezervacije);
        cijenaRada=findViewById(R.id.cijenaRada);
        cijenaDjelova=findViewById(R.id.cijenaDjelova);


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
        datumZavrsetka.setText(rezervacija.getDatumZavrsetka());
        radniSati.setText(String.valueOf(rezervacija.getRadniSati()));
        radnik.setText(String.format(("%s %s"),rezervacija.getZaposlenik().getIme(),rezervacija.getZaposlenik().getPrezime()));
        cijenaRada.setText(String.format(("%s €"), rezervacija.getCijenaRada()));
        cijenaDjelova.setText(String.format(("%s €"), rezervacija.getCijenaDijelova()));
        cijena.setText(String.format(("%s €"), rezervacija.getCijena()));
        brojRezervacije.setText(String.valueOf(rezervacija.getBrojRezervacije()));


        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}