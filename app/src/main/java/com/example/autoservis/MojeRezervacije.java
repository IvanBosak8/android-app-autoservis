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
// Moji Servisi
public class MojeRezervacije extends AppCompatActivity {

    private List<Rezervacija> mojeRezervacijeList;
    private RecyclerView recyclerView;
    private AdapterMojeRezervacije adapterMojeRezervacije;
    private  RecyclerView.LayoutManager layoutManager;
    private User trenutniUser;
    private TextView upozorenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_rezervacije);
        recyclerView=findViewById(R.id.recycleView);
        upozorenje=findViewById(R.id.upozorenje);

        trenutniUser =(User) getIntent().getSerializableExtra("trenutniUser");

        assert trenutniUser!=null;
        mojeRezervacije(trenutniUser.getID());
    }

    private void mojeRezervacije(int id){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

        Call<List<Rezervacija>> mojeRezervacije = autoservisAPI.getRezervacijaByUser(id);

        mojeRezervacije.enqueue(new Callback<List<Rezervacija>>() {
            @Override
            public void onResponse(Call<List<Rezervacija>> call, Response<List<Rezervacija>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MojeRezervacije.this, "Greška! Ne možemo učitati vaše servise.\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                } else {

                    mojeRezervacijeList = response.body();

                    recyclerView.setHasFixedSize(true);
                    adapterMojeRezervacije = new AdapterMojeRezervacije(mojeRezervacijeList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapterMojeRezervacije);

                    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()==0){

                        upozorenje.setText("Nemate servise!");
                    }

                    adapterMojeRezervacije.setOnClickLisener(new AdapterMojeRezervacije.OnItemClickLisener(){

                        @Override
                        public void OnItemClick(int position) {
                            Rezervacija rezervacija= mojeRezervacijeList.get(position);
                            Intent intent= new Intent(MojeRezervacije.this,RezervacijaDetalji.class);
                            intent.putExtra("trenutnaRezervacija",rezervacija);
                            intent.putExtra("trenutniUser",trenutniUser);
                            startActivity(intent);
                        }



                    });
                }
            }

            @Override
            public void onFailure(Call<List<Rezervacija>> call, Throwable t) {
                Toast.makeText(MojeRezervacije.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}