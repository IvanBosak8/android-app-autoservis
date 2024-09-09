package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import models.User;

public class AdminHomeUI extends AppCompatActivity {

    private CardView pregledajSveRezervacije;
    private CardView nova_Rezervacija;
    private CardView pregledajRezervacije;
    private CardView zaposlenici;
    private CardView mojeRezervacije;

    private CardView noviZaposlenik;
    private CardView odjava;

    private CardView pregledajServise;
    private TextView pozdrav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_ui);

        nova_Rezervacija = findViewById(R.id.nova_Rezervacija);
        pregledajSveRezervacije = findViewById(R.id.pregledajSveRezervacije);
        mojeRezervacije = findViewById(R.id.mojeRezervacije);
        pregledajRezervacije = findViewById(R.id.pregledaj_rezervacije);
        pozdrav = findViewById(R.id.pozdravUser);
        odjava = findViewById(R.id.odjava);
        zaposlenici = findViewById(R.id.zaposlenici);
        noviZaposlenik=findViewById(R.id.noviZaposlenik);
        pregledajServise=findViewById(R.id.pregledajServise);


        User trenutniUser = (User) getIntent().getSerializableExtra("trenutniUser");

        assert trenutniUser != null;
        pozdrav.setText(String.format("Pozdrav, %s %s!", trenutniUser.getIme(), trenutniUser.getPrezime()));

        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        pregledajSveRezervacije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, PregledajSveRezervacije.class);
                startActivity(intent);
            }
        });

        nova_Rezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, RezervacijaUpisPodatakaAdmin.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }
        });

        pregledajRezervacije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, PregledRezervacije.class);
                startActivity(intent);
            }
        });


        mojeRezervacije.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, MojeRezervacije.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }

        });
        zaposlenici.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, Zaposlenici.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }

        });

        noviZaposlenik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeUI.this, UpisZaposlenika.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }
        });

        pregledajServise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminHomeUI.this, Servis.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }
        });
    }







    @Override
    public void onBackPressed(){}
}