package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import models.Rezervacija;
import models.User;

public class UserHomeUI extends AppCompatActivity {

    private CardView nova_Rezervacija;
    private CardView odjava;
    private CardView nedovrsene_rezervacije;
    private TextView pozdrav;
    private CardView mojeRezervacije;
    private CardView recenzija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_ui);

        nova_Rezervacija = findViewById(R.id.nova_Rezervacija);
        mojeRezervacije = findViewById(R.id.mojeRezervacije);
        odjava = findViewById(R.id.odjava);
        pozdrav = findViewById(R.id.pozdravUser);
        nedovrsene_rezervacije=findViewById(R.id.nedovsene_rezervaije);
        recenzija=findViewById(R.id.recenzija);


        User trenutniUser = (User) getIntent().getSerializableExtra("trenutniUser");

        assert trenutniUser != null;
        pozdrav.setText(String.format("Pozdrav, %s %s!", trenutniUser.getIme(), trenutniUser.getPrezime()));

        nova_Rezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeUI.this, RezervacijaUpisPodatakaActivity.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }
        });

        mojeRezervacije.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeUI.this, MojeRezervacije.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }

        });

        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UserHomeUI.this, LoginActivity.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }
        });
        nedovrsene_rezervacije.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeUI.this,NedovrseneRezervacije.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }

        });
        recenzija.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeUI.this,RecenzijaUpis.class);
                intent.putExtra("trenutniUser", trenutniUser);
                startActivity(intent);
            }

        });

    }
    @Override
    public void onBackPressed(){}
}