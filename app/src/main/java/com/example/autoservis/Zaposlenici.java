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
import models.User;
import models.Zaposlenik;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Zaposlenici extends AppCompatActivity {

    protected static Zaposlenici instance;
    private List<Zaposlenik> zaposlenikList;
    private RecyclerView recyclerView;
    private AdapterZaposlenici adapterZaposlenici;
    private RecyclerView.LayoutManager layoutManager;
    private User trenutniUser;
    private TextView upozorenje;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaposlenici);
        instance=this;
        recyclerView=findViewById(R.id.recycleView);
        upozorenje=findViewById(R.id.upozorenje);

        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");

        assert trenutniUser != null;
        zaposlenici(trenutniUser.getID());

    }

    private void zaposlenici(int id){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Zaposlenik>> zaposlenici = autoservisAPI.getZaposlenik();

        zaposlenici.enqueue(new Callback<List<Zaposlenik>>() {
            @Override
            public void onResponse(Call<List<Zaposlenik>> call, Response<List<Zaposlenik>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Zaposlenici.this, "Greška! Ne možemo učitati Zaposlenike iz baze podatak!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                }else{
                    zaposlenikList=response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterZaposlenici= new AdapterZaposlenici(zaposlenikList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterZaposlenici);

                    if(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){
                        upozorenje.setText("Trenutno nema zaposlenika!");
                    }
                    adapterZaposlenici.setOnClickLisener(new AdapterZaposlenici.OnItemClickLisener() {
                        @Override
                        public void onItemClick(int position) {
                            Zaposlenik zaposlenici = zaposlenikList.get(position);
                            Intent intent = new Intent(Zaposlenici.this, ZaposleniciDetalji.class);
                            intent.putExtra("trenutniZaposlenik",zaposlenici);
                            intent.putExtra("trenutniUser",trenutniUser);
                            startActivity(intent);

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<List<Zaposlenik>> call, Throwable t) {
                Toast.makeText(Zaposlenici.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}