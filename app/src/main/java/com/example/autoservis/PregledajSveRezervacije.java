package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import models.AutoservisAPI;
import models.Rezervacija;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//pregledaj Gotove Servise
public class PregledajSveRezervacije extends AppCompatActivity {

    private List<Rezervacija> sveRezervacijeList;
    private RecyclerView recyclerView;
    private AdapterSveRezervacije adapterSveRezervacije;
    private RecyclerView.LayoutManager layoutManager;
    private TextView upozorenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregledaj_sve_rezervacije);

        recyclerView = findViewById(R.id.recycleView);
        upozorenje=findViewById(R.id.upozorenje);
        sveRezervacije();

    }

    private void sveRezervacije(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();



        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Rezervacija>> sveRezervacije = autoservisAPI.getRezervacija();

        sveRezervacije.enqueue(new Callback<List<Rezervacija>>() {
            @Override
            public void onResponse(Call<List<Rezervacija>> call, Response<List<Rezervacija>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(PregledajSveRezervacije.this, "Greška! Ne možemo učitati servise iz baze podataka\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                } else {
                    sveRezervacijeList = response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterSveRezervacije = new AdapterSveRezervacije(sveRezervacijeList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterSveRezervacije);

                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() == 0) {

                        upozorenje.setText("U bazi podataka ne postoje servisi!");
                    }

                    adapterSveRezervacije.setOnClickListener(new AdapterSveRezervacije.OnItemClickLisener() {
                        @Override
                        public void onItemClick(int position) {
                            Rezervacija rezervacija = sveRezervacijeList.get(position);
                            Intent intent = new Intent(PregledajSveRezervacije.this, RezervacijaDetalji.class);
                            intent.putExtra("trenutnaRezervacija", rezervacija);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Rezervacija>> call, Throwable t) {
                    Toast.makeText(PregledajSveRezervacije.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();


                }
        });
    }
}