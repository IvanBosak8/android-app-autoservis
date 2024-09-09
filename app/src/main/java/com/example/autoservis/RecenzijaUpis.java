package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import android.widget.RatingBar;
import android.widget.Toast;


public class RecenzijaUpis extends AppCompatActivity implements DialogRecenzija.DialogLisener {

    private boolean pritisnuti_back_btn;
    private Button izadi;
    private RatingBar ocjena;
    private Button posalji;
    private TextView recenzija1;
    private User trenutniUser;
    private Recenzija recenzija;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzija_upis);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        ocjena.setRating(0);
//        ocjena.setStepSize(1);
        izadi=findViewById(R.id.izađi);
        posalji=findViewById(R.id.posalji);
        recenzija1=findViewById(R.id.recenzija);
        ocjena=findViewById(R.id.ocjena);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

//        ocjena.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String s = String.valueOf(ocjena.getRating());
//                Toast.makeText(getApplicationContext(),s+"Zvijezdica",Toast.LENGTH_SHORT).show();
//            }
//        });


        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser!=null;

        recenzija1.addTextChangedListener(textWatcher);



        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRecenzija dialog=new DialogRecenzija();
                dialog.show(getSupportFragmentManager(),"Dialog");
            }
        });
    }

    public void posaljiRecenziju() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        recenzija = new Recenzija(ocjena.getRating(), recenzija1.getText().toString());


        Call<Void> recenzijaCall = autoservisAPI.novaRecenzija(trenutniUser.getID(), recenzija);

        recenzijaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RecenzijaUpis.this, "Greška! Recenzija nije poslana.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecenzijaUpis.this, "Recenzija je poslana!\nPreusmjerit ćemo vas na Početni zaslon.", Toast.LENGTH_LONG).show();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2500);
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecenzijaUpis.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }


        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String sRecenzija = recenzija1.getText().toString().trim();
            String sOcjena= String.valueOf(ocjena);
            posalji.setEnabled(!sRecenzija.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onBackPressed() {
        if (pritisnuti_back_btn) {
            super.onBackPressed();
            return;
        }

        this.pritisnuti_back_btn = true;
        Toast.makeText(this, "Pritisni BACK button ponovo za izlazak", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                pritisnuti_back_btn = false;
            }
        }, 3000);
    }
}