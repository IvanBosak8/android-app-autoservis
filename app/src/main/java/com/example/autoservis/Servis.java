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

//pregledaj nedovrsene servise
public class Servis extends AppCompatActivity {

    protected static Servis instance;
    private List<Rezervacija> servisList;

    private RecyclerView recyclerView;

    private AdapterServis adapterServis;

    private RecyclerView.LayoutManager layoutManager;

    private User trenutniUser;

    private TextView upozorenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis);
        instance=this;
        recyclerView=findViewById(R.id.recycleView);
        upozorenje=findViewById(R.id.upozorenje);


        servis();
    }

    private void servis(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Rezervacija>> servis = autoservisAPI.getServis();

        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser!=null;

        servis.enqueue(new Callback<List<Rezervacija>>() {
            @Override
            public void onResponse(Call<List<Rezervacija>> call, Response<List<Rezervacija>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(Servis.this, "Greška! Ne možemo učitati servise iz baze podataka!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                } else {
                    servisList =response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterServis = new AdapterServis(servisList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterServis);

                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){

                        upozorenje.setText("U bazi podataka nema nedovršenih servisa!");
                    }
                    adapterServis.setOnClickLisener(new AdapterServis.OnItemClickLisener() {
                        @Override
                        public void OnItemClick(int position) {
                            Rezervacija rezervacija= servisList.get(position);
                            Intent intent= new Intent(Servis.this,ServisDetalji.class);
                            intent.putExtra("trenutnaRezervacija",rezervacija);
                            intent.putExtra("trenutniUser",trenutniUser);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Rezervacija>> call, Throwable t) {
                Toast.makeText(Servis.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}