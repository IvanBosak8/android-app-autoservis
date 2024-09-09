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
import models.Recenzija;
import models.Rezervacija;
import models.User;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Recenzije extends AppCompatActivity {

    private List<Recenzija> recenzijeList;

    private RecyclerView recyclerView;

    private AdapterRecenzije adapterRecenzije;

    private RecyclerView.LayoutManager layoutManager;

    private User trenutniUser;

    private TextView upozorenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzije);
        recyclerView=findViewById(R.id.recycleView);
        upozorenje=findViewById(R.id.upozorenje);

        recenzije();
    }
    private void recenzije() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Recenzija>> recenzija = autoservisAPI.getRecenzija();

//        trenutniUser = (User) getIntent().getSerializableExtra("trenutniUser");
//        assert trenutniUser != null;

        recenzija.enqueue(new Callback<List<Recenzija>>() {
            @Override
            public void onResponse(Call<List<Recenzija>> call, Response<List<Recenzija>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(Recenzije.this, "Greška! Ne možemo učitati servise iz baze podataka!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                } else {
                    recenzijeList = response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterRecenzije = new AdapterRecenzije(recenzijeList);
                    layoutManager= new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterRecenzije);
                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){

                        upozorenje.setText("U bazi podataka nema Recenzija!");
                    }
                    adapterRecenzije.setOnItemClickLisener(new AdapterRecenzije.OnItemClickLisener() {
                        @Override
                        public void OnItemClick(int position) {

                            Recenzija recenzija1 =recenzijeList.get(position);
                            Intent intent = new Intent(Recenzije.this,RecenzijeDetalji.class);
                            intent.putExtra("trenutnaRecenzija",recenzija1);
//                            intent.putExtra("trenutniUser",trenutniUser);
                            startActivity(intent);
                        }
                    });{

                    }

                }

            }

            @Override
            public void onFailure(Call<List<Recenzija>> call, Throwable t) {
                Toast.makeText(Recenzije.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}