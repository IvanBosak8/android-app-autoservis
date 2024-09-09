package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.ParseException;

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

public class RezervacijaUpisPodatakaAdmin extends AppCompatActivity implements DialogPosalji.DialogLisener {

    private boolean pritisnuti_back_btn;
    private Button izadi;
    private TextView dan;
    private Button posalji;
    private TextView imePrezime;
    private TextView broj;
    private TextView rezervacijaBroj;
    private TextView auto;
    private TextView godinaProizvodnje;
    private TextView brojSasije;
    private TextView popravak;
    private TextView datumPrimanja;
    private TextView datumZavrsetka;
//    private TextView radnik;
    private TextView radniSati;
    private TextView cijena;
    private Button datumPrimanjaButton;
    private Button datumZavrsetkaButton;
    private User trenutniUser;
    private Rezervacija rezervacija;
    private int redniBrojRezervacija = 0;

    private TextView cijenaDijelova;
    private TextView cijenaRada;
    private Integer zaposlenik;

    String[] item = {"Petar Gajski", "David Maltarski", "Luka Vugrinec"};
    AutoCompleteTextView radnik;
    ArrayAdapter<String> adapterItem;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija_upis_podataka_admin);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        izadi = findViewById(R.id.izađi);
        posalji = findViewById(R.id.posalji);
        imePrezime = findViewById(R.id.ime_i_prezime);
        dan = findViewById(R.id.dan);
        broj = findViewById(R.id.broj);
        auto = findViewById(R.id.auto);
        godinaProizvodnje = findViewById(R.id.godina_proizvodnje);
        brojSasije = findViewById(R.id.broj_sasije);
        popravak = findViewById(R.id.popravak);
        datumPrimanja = findViewById(R.id.datum_primanja);
        datumPrimanjaButton = findViewById(R.id.datum_primanja_button);
        datumZavrsetka=findViewById(R.id.datum_zavrsetka);
        datumZavrsetkaButton=findViewById(R.id.datum_zavrsetka_button);
        radniSati=findViewById(R.id.radni_sati);
        cijena=findViewById(R.id.cijena);
        rezervacijaBroj=findViewById(R.id.brojRezervacije);
        cijenaRada=findViewById(R.id.cijenaRadova);
        cijenaDijelova=findViewById(R.id.cijenaDjelova);
        radnik=findViewById(R.id.radnik);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        adapterItem= new ArrayAdapter<String>(this,R.layout.list_item,item);
        radnik.setAdapter(adapterItem);
        radnik.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(RezervacijaUpisPodatakaAdmin.this,"Radnik: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String trenutniDan = sdf.format(new Date());
        dan.setText(trenutniDan);

        trenutniUser = (User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser != null;

        imePrezime.setText(String.format("%s %s", trenutniUser.getIme(), trenutniUser.getPrezime()));
        broj.setText(trenutniUser.getBroj());
        datumPrimanja.addTextChangedListener(textWatcher);
        datumZavrsetka.addTextChangedListener(textWatcher);
        auto.addTextChangedListener(textWatcher);
        godinaProizvodnje.addTextChangedListener(textWatcher);
        brojSasije.addTextChangedListener(textWatcher);
        popravak.addTextChangedListener(textWatcher);
        radnik.addTextChangedListener(textWatcher);
        radniSati.addTextChangedListener(textWatcher);
        cijena.addTextChangedListener(textWatcher);
        rezervacijaBroj.addTextChangedListener(textWatcher);
        cijenaRada.addTextChangedListener(textWatcher);
        cijenaDijelova.addTextChangedListener(textWatcher);



        Call<Integer> brojRezervacije = autoservisAPI.getBrojRezervacije();
        brojRezervacije.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    redniBrojRezervacija = 0;
                    rezervacijaBroj.setText(String.valueOf(redniBrojRezervacija));
                } else {
                    assert response.body() != null;
                    redniBrojRezervacija = response.body() + 1;
                    rezervacijaBroj.setText(String.valueOf(redniBrojRezervacija));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(RezervacijaUpisPodatakaAdmin.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();

            }
        });

        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datumPrimanjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datumPrimanja.addTextChangedListener(textWatcher);
                prikaziKalendarPrimanja();
            }
        });

         datumZavrsetkaButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 datumZavrsetka.addTextChangedListener(textWatcher);
                 prikaziKalendarZavrsetka();
             }
         });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPosalji dialog = new DialogPosalji();
                dialog.show(getSupportFragmentManager(), "Dialog");
            }
        });

    }


    public void posaljiRezervaciju() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        rezervacija = new Rezervacija(trenutniUser.getIme(),trenutniUser.getPrezime(),trenutniUser.getBroj(),
                Double.parseDouble(cijena.getText().toString()),datumPrimanja.getText().toString(),datumZavrsetka.getText().toString(), zaposlenik,Integer.parseInt(radniSati.getText().toString()),auto.getText().toString(),
                Integer.parseInt(godinaProizvodnje.getText().toString()),brojSasije.getText().toString(),popravak.getText().toString(),
                dan.getText().toString(), Integer.parseInt(rezervacijaBroj.getText().toString()),
                Double.parseDouble(cijenaRada.getText().toString()),Double.parseDouble(cijenaDijelova.getText().toString()),"ne");


        Call<Void> rezervacijaCall = autoservisAPI.novaRezervacija(trenutniUser.getID(), rezervacija);

        rezervacijaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RezervacijaUpisPodatakaAdmin.this, "Greška! Rezervacija nije poslana.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(RezervacijaUpisPodatakaAdmin.this, "Rezervacija je poslana!\nPreusmjerit ćemo vas na Početni zaslon.", Toast.LENGTH_LONG).show();


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
                Toast.makeText(RezervacijaUpisPodatakaAdmin.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }


        });
    }

    private void prikaziKalendarPrimanja() {
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

        new DatePickerDialog(RezervacijaUpisPodatakaAdmin.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void prikaziKalendarZavrsetka() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

                datumZavrsetka.setText(sdf.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(RezervacijaUpisPodatakaAdmin.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String sAuto = auto.getText().toString().trim();
            String sGodinaProizvodnje = godinaProizvodnje.getText().toString().trim();
            String sBrojSasije = brojSasije.getText().toString().trim();
            String sPopravak = popravak.getText().toString().trim();
            String sDatumPrimanja = datumPrimanja.getText().toString().trim();
            String sDatumZavrsetka= datumZavrsetka.getText().toString().trim();
            String sRadnik= radnik.getText().toString().trim();
            String sRadniSati= radniSati.getText().toString().trim();
            String sCijena= cijena.getText().toString().trim();
            String sRezervacijaBroj=rezervacijaBroj.getText().toString().trim();
            String sDan=dan.getText().toString().trim();
            String sRad=cijenaRada.getText().toString().trim();
            String sCijenaDjelova=cijenaDijelova.getText().toString().trim();
            boolean dorbaProizvodnja=false;
            boolean dobraSasija=false;


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                Date d1 = sdf.parse(sDatumPrimanja);
                Date d2 = sdf.parse(sDatumZavrsetka);
                assert d1 != null;
                assert d2 != null;
                long razlika = d2.getTime() - d1.getTime();

                if(razlika<0){
                    datumPrimanja.setText("pogrešan unos");
                    datumZavrsetka.setText("pogrešan unos");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!sGodinaProizvodnje.isEmpty()) {
                if (Integer.parseInt(sGodinaProizvodnje) > 1980 && Integer.parseInt(sGodinaProizvodnje) < 2024) {
                    godinaProizvodnje.getText().toString().trim();
                    dorbaProizvodnja = true;

                } else {
                    godinaProizvodnje.setError("Godina proizvodnje mora biti između 1980 i 2024");
                }
            }

            if (!sRadniSati.isEmpty()){
                double radovi= Double.parseDouble(sRadniSati)*20;
                cijenaRada.removeTextChangedListener(this);
                cijenaRada.setText(String.valueOf( radovi));
            }else {
                cijenaRada.removeTextChangedListener(this);
                cijenaRada.setText("");
            }

            if (!sRad.isEmpty()  && !sCijenaDjelova.isEmpty()){
                double ukupnaCijena=Double.parseDouble(sRad)+ Double.parseDouble(sCijenaDjelova);
                cijena.removeTextChangedListener(this);
                cijena.setText(String.valueOf(ukupnaCijena));
            }else {
                cijena.removeTextChangedListener(this);
                cijena.setText("");
            }

            if(sRadnik.equals("Petar Gajski")){
                zaposlenik = 1;
            }
            if (sRadnik.equals("David Maltarski")){
                zaposlenik = 2;
            }
            if (sRadnik.equals("Luka Vugrinec")){
                zaposlenik = 3;
            }
            if(!sBrojSasije.isEmpty()) {
                if (!(sBrojSasije.length() == 17)) {
                    brojSasije.setError("Broj sasije mora sadrzavat 17 karaktera");
                } else {
                    brojSasije.getText().toString().trim();
                    brojSasije.setError(null);
                    dobraSasija = true;
                }
            }


            posalji.setEnabled(!sAuto.isEmpty() && !sGodinaProizvodnje.isEmpty() && !sBrojSasije.isEmpty()
                    && !sPopravak.isEmpty() && !sDatumPrimanja.isEmpty() && !sDatumZavrsetka.isEmpty()
                    && !sRadnik.isEmpty() && !sRadniSati.isEmpty()
                    && !sRezervacijaBroj.isEmpty() && !sDan.isEmpty()
                    && !sCijenaDjelova.isEmpty() && !sRad.isEmpty() && dobraSasija && dorbaProizvodnja);
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
        }, 2000);
    }
}