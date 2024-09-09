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
// pregledaj Rezervacije
public class PregledRezervacije extends AppCompatActivity {

    protected static PregledRezervacije instance;
    private List<Rezervacija> nedefiniraneRezervacijeList;
    protected static RecyclerView recyclerView;
    private AdapterNedefinirano adapterNedefinirano;
    private RecyclerView.LayoutManager layoutManager;
    private TextView upozorenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregled_rezervacije);
        instance=this;
        recyclerView = findViewById(R.id.recycleView1);
        upozorenje=findViewById(R.id.upozorenje);
        nedefiniraneRezervacija();

    }

    private void nedefiniraneRezervacija(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Rezervacija>> nedefinirano =autoservisAPI.getNedefiniraneRezervacije();

        nedefinirano.enqueue(new Callback<List<Rezervacija>>() {
            @Override
            public void onResponse(Call<List<Rezervacija>> call, Response<List<Rezervacija>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PregledRezervacije.this, "Greška! Ne možemo učitati rezervacije iz baze podataka\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();

                }else{
                    nedefiniraneRezervacijeList=response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterNedefinirano = new AdapterNedefinirano(nedefiniraneRezervacijeList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterNedefinirano);

                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){
                        upozorenje.setText("U bazi podataka ne postoje Rezervacije!");
                    }
                    adapterNedefinirano.setOnCliclLisener(new AdapterNedefinirano.OnItemClickLisiner() {
                        @Override
                        public void onItemClick(int position) {
                            Rezervacija rezervacija = nedefiniraneRezervacijeList.get(position);
                            Intent intent = new Intent(PregledRezervacije.this, PregledRezervacijeDetalji.class);
                            intent.putExtra("trenutnaRezervacija", rezervacija);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Rezervacija>> call, Throwable t) {
                Toast.makeText(PregledRezervacije.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}