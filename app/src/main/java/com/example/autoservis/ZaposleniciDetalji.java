package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import models.AutoservisAPI;
import models.Rezervacija;
import models.User;
import models.Zaposlenik;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZaposleniciDetalji extends AppCompatActivity implements DialogIzbrisi.DialogLisener, DialogAzuriraj.DialogLisener {

    private Button izadi;
    private Button azuriraj;
    private Button izbrisi;
    private EditText ime;
    private EditText prezime;
    private EditText broj;
    private EditText email;
    private Zaposlenik trenutniZaposlenik;
    private User trenutniUser;
    private Zaposlenik zaposlenik;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaposlenici_detalji);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        izadi=findViewById(R.id.izađi);
        azuriraj=findViewById(R.id.azuriraj);
        izbrisi=findViewById(R.id.izbrisi);
        ime=findViewById(R.id.ime);
        prezime=findViewById(R.id.prezime);
        broj=findViewById(R.id.broj);
        email=findViewById(R.id.email);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        User trenutniUser = (User)getIntent().getSerializableExtra("trenutniUser");
        Zaposlenik zaposlenik = (Zaposlenik)getIntent().getSerializableExtra("trenutniZaposlenik");
        assert zaposlenik !=null;
        trenutniZaposlenik=(Zaposlenik) getIntent().getSerializableExtra("trenutniZaposlenik");
        if (trenutniZaposlenik==null) throw new AssertionError();

        ime.setText(zaposlenik.getIme());
        prezime.setText(zaposlenik.getPrezime());
        broj.setText(zaposlenik.getBroj());
        email.setText(zaposlenik.getEmail());

        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        izbrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogIzbrisi dialogizrisi = new DialogIzbrisi();
                dialogizrisi.show(getSupportFragmentManager(), "Dialog Izbrisi");
            }
        });

        azuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAzuriraj dialogazuriraj = new DialogAzuriraj();
                dialogazuriraj.show(getSupportFragmentManager(), "Dialog Ažuriraj");
            }
        });
    }

    @Override
    public void izbrisiZaposlenika() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Call<Void> izbrisiZaposlenika=autoservisAPI.izbrisiZaposlenika(trenutniZaposlenik.getId());

        izbrisiZaposlenika.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ZaposleniciDetalji.this,"Greška! Zaposlenik nije obrisan.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ZaposleniciDetalji.this,"Zaposlenik je uspješno obrisan!\nPreusmjerit ćemo vas...", Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Zaposlenici.instance.finish();
                            finish();
                        }
                    },2500);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ZaposleniciDetalji.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void azurirajZaposlenika() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        zaposlenik=new Zaposlenik(trenutniZaposlenik.getId(), ime.getText().toString(), prezime.getText().toString(), broj.getText().toString(), email.getText().toString());


        Call<Void> azurirajZaposlenika=autoservisAPI.updateZaposlenik(trenutniZaposlenik.getId(), zaposlenik);
        azurirajZaposlenika.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ZaposleniciDetalji.this,"Greška! Zaposlenik nije ažuriran.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ZaposleniciDetalji.this, "Zaposlenik je uspješno ažuriran!\nPreusmjerit ćemo vas...", Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Zaposlenici.instance.finish();
                            finish();
                        }
                    },3000);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ZaposleniciDetalji.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });
    }
}