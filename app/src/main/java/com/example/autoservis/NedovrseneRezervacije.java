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
import models.User;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NedovrseneRezervacije extends AppCompatActivity {


    private List<Rezervacija> nedovrsenaRezervacija;
    protected static RecyclerView recyclerView;
    private AdapterNedovrseno adapterNedovrseno;
    private RecyclerView.LayoutManager layoutManager;
    private TextView upozorenje;

    private User trenutniUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedovrsene_rezervacije);
        recyclerView = findViewById(R.id.recycleView2);
        upozorenje=findViewById(R.id.upozorenje);
        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser!=null;
        nedovrseno(trenutniUser.getID());



    }

    private void nedovrseno(int id){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);


        Call<List<Rezervacija>> nedovrseno =autoservisAPI.getNedovrseneRezervacije(id);
        nedovrseno.enqueue(new Callback<List<Rezervacija>>() {
            @Override
            public void onResponse(Call<List<Rezervacija>> call, Response<List<Rezervacija>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(NedovrseneRezervacije.this, "Greška! Ne možemo učitati rezervacije iz baze podataka\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();

                }else{
                    nedovrsenaRezervacija =response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterNedovrseno = new AdapterNedovrseno(nedovrsenaRezervacija);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterNedovrseno);

                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){
                        upozorenje.setText("Trenutno nemate rezervacije!");
                    }
                    adapterNedovrseno.setOnCliclLisener(new AdapterNedovrseno.OnItemClickLisener() {
                        @Override
                        public void onItemClick(int position) {
                            Rezervacija rezervacija = nedovrsenaRezervacija.get(position);
                            Intent intent = new Intent(NedovrseneRezervacije.this, NedovrseneRezervacijeDetalji.class);
                            intent.putExtra("trenutnaRezervacija", rezervacija);
                            intent.putExtra("trenutniUser",trenutniUser);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Rezervacija>> call, Throwable t) {
                Toast.makeText(NedovrseneRezervacije.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}