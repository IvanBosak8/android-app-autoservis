package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

public class RezervacijaUpisPodatakaActivity extends AppCompatActivity implements DialogPosalji.DialogLisener{

    private boolean pritisnuti_back_btn;
    private Button izadi;
    private TextView dan;
    private Button posalji;
    private TextView imePrezime;
    private TextView broj;
    private TextView auto;
    private TextView godinaProizvodnje;
    private TextView brojSasije;
    private  TextView popravak;
    private TextView datumPrimanja;
    private Button datumButton;
    private User trenutniUser;
    private Zaposlenik trenutniRadnik;
    private Rezervacija rezervacija;
    private int redniBrojRezervacija=0;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija_upis_podataka);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        izadi=findViewById(R.id.izađi);
        posalji=findViewById(R.id.posalji);
        imePrezime=findViewById(R.id.ime_i_prezime);
        dan=findViewById(R.id.dan);
        broj=findViewById(R.id.broj);
        auto=findViewById(R.id.auto);
        godinaProizvodnje=findViewById(R.id.godina_proizvodnje);
        brojSasije=findViewById(R.id.broj_sasije);
        popravak=findViewById(R.id.popravak);
        datumPrimanja=findViewById(R.id.datum_primanja);
        datumButton=findViewById(R.id.datum_button);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        SimpleDateFormat sdf= new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String trenutniDan=sdf.format(new Date());
        dan.setText(trenutniDan);

        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser!=null;

        imePrezime.setText(String.format("%s %s", trenutniUser.getIme(),trenutniUser.getPrezime()));
        broj.setText(trenutniUser.getBroj());
        datumPrimanja.addTextChangedListener(textWatcher);
        auto.addTextChangedListener(textWatcher);
        godinaProizvodnje.addTextChangedListener(textWatcher);
        brojSasije.addTextChangedListener(textWatcher);
        popravak.addTextChangedListener(textWatcher);

        Call<Integer> brojRezervacije= autoservisAPI.getBrojRezervacije();
        brojRezervacije.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){
                    redniBrojRezervacija=0;
                }else{
                    assert response.body() !=null;
                    redniBrojRezervacija=response.body()+1;

                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(RezervacijaUpisPodatakaActivity.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();

            }
        });

        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datumPrimanja.addTextChangedListener(textWatcher);
                prikaziKalendar();
            }
        });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPosalji dialog=new DialogPosalji();
                dialog.show(getSupportFragmentManager(),"Dialog");
            }
        });

    }


    public void posaljiRezervaciju() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        rezervacija = new Rezervacija(redniBrojRezervacija,dan.getText().toString(),  trenutniUser.getIme(), trenutniUser.getPrezime(),
                broj.getText().toString(), datumPrimanja.getText().toString(),"1.1.2023", auto.getText().toString(),
                 Integer.parseInt(godinaProizvodnje.getText().toString()), brojSasije.getText().toString(), popravak.getText().toString(),1,1,1,1,1,"nema");


        Call<Void> rezervacijaCall = autoservisAPI.novaRezervacija(trenutniUser.getID(), rezervacija);

        rezervacijaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RezervacijaUpisPodatakaActivity.this, "Greška! Rezervacija nije poslana.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(RezervacijaUpisPodatakaActivity.this, "Rezervacija je poslana!\nPreusmjerit ćemo vas na Početni zaslon.", Toast.LENGTH_LONG).show();
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);

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
                Toast.makeText(RezervacijaUpisPodatakaActivity.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }


        });
    }

    private void prikaziKalendar(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

                datumPrimanja.setText(sdf.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(RezervacijaUpisPodatakaActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    private TextWatcher textWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String sAuto=auto.getText().toString().trim();
            String sGodinaProizvodnje=godinaProizvodnje.getText().toString().trim();
            String sBrojSasije=brojSasije.getText().toString().trim();
            String sPopravak=popravak.getText().toString().trim();
            String sDatumPrimanja=datumPrimanja.getText().toString().trim();
            boolean dorbaProizvodnja=false;
            boolean dobraSasija=false;


            if (!sBrojSasije.isEmpty()) {
                if (!(sBrojSasije.length() == 17)) {
                    brojSasije.setError("Broj sasije mora sadrzavat 17 karaktera");
                } else {
                    brojSasije.getText().toString().trim();
                    brojSasije.setError(null);
                    dobraSasija = true;
                }
            }
            if(!sGodinaProizvodnje.isEmpty()) {
                if (Integer.parseInt(sGodinaProizvodnje) > 1980 && Integer.parseInt(sGodinaProizvodnje) < 2024) {
                    godinaProizvodnje.getText().toString().trim();
                    dorbaProizvodnja = true;

                } else {
                    godinaProizvodnje.setError("Godina proizvodnje mora biti između 1980 i 2024");
                }
            }


            posalji.setEnabled(!sAuto.isEmpty() && !sGodinaProizvodnje.isEmpty() && !sBrojSasije.isEmpty() && !sPopravak.isEmpty() && !sDatumPrimanja.isEmpty()&& dobraSasija && dorbaProizvodnja);
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