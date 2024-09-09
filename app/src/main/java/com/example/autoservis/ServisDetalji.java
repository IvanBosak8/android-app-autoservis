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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import models.AutoservisAPI;
import models.Rezervacija;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServisDetalji extends AppCompatActivity implements DialogPotvrdi.DialogLisener, DialogOdbaci.DialogLisener {
    private boolean pritisnuti_back_btn;
    private Button izadi;
    private Button posalji;
    private Button odbaci;
    private EditText dan;
    private EditText imePrezime;
    private EditText rezervacijaBroj;
    private EditText broj;
    private TextView auto;
    private TextView godinaProizvodnje;
    private TextView brojSasije;
    private TextView popravak;
    private TextView datumPrimanja;
    private TextView datumZavrsetka;
    private EditText radniSati;
    private TextView cijena;
    private EditText cijenaDijelova;
    private TextView cijenaRada;
    private Button datumPrimanjaButton;
    private Button datumZavrsetkaButton;
    private Rezervacija trenutnaRezervacija;
    private Rezervacija rezervacija;
    private int redniBrojRezervacija=0;
    String[] item = {"Petar Gajski", "David Maltarski", "Luka Vugrinec"};
    AutoCompleteTextView radnik;
    ArrayAdapter<String> adapterItem;
    private Integer zaposlenik;


    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis_detalji);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        izadi=findViewById(R.id.izađi);
        posalji=findViewById(R.id.posalji);
        odbaci=findViewById(R.id.odbaci);
        dan=findViewById(R.id.dan);
        imePrezime=findViewById(R.id.ime_i_prezime);
        rezervacijaBroj=findViewById(R.id.brojRezervacije);
        broj=findViewById(R.id.broj);
        auto=findViewById(R.id.auto);
        godinaProizvodnje=findViewById(R.id.godina_proizvodnje);
        brojSasije=findViewById(R.id.broj_sasije);
        popravak=findViewById(R.id.popravak);
        datumPrimanja=findViewById(R.id.datum_primanja);
        datumZavrsetka=findViewById(R.id.datum_zavrsetka);
        radniSati=findViewById(R.id.radni_sati);
        cijena=findViewById(R.id.cijena);
        datumPrimanjaButton=findViewById(R.id.datumPrimanjaButton);
        datumZavrsetkaButton=findViewById(R.id.datumZavrsetkaButton);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
        dan.addTextChangedListener(textWatcher);
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
                Toast.makeText(ServisDetalji.this,"Radnik: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        trenutnaRezervacija =(Rezervacija)getIntent().getSerializableExtra("trenutnaRezervacija");
        if (trenutnaRezervacija==null) throw new AssertionError();

        datumPrimanja.setText(trenutnaRezervacija.getDatumPrimanja());
        auto.setText(trenutnaRezervacija.getAuto());
        godinaProizvodnje.setText(String.valueOf(trenutnaRezervacija.getGodinaProizvodnje()));
        brojSasije.setText(trenutnaRezervacija.getBrojSasije());
        popravak.setText(trenutnaRezervacija.getPopravak());
        if (trenutnaRezervacija.getDatumZavrsetka().equals("1.1.2023")){
            datumZavrsetka.setText("");
        }else{
            datumZavrsetka.setText(trenutnaRezervacija.getDatumZavrsetka());
        }
//        if (trenutnaRezervacija.getRadnik().equals("radnik")){
//           radnik.setText("");
//        }

       radniSati.setText(String.valueOf(trenutnaRezervacija.getRadniSati()));
        if(radniSati.length() == 1){
            radniSati.setText("");
        }
        dan.setText(trenutnaRezervacija.getDan());
        imePrezime.setText(String.format("%s %s",trenutnaRezervacija.getUser().getIme(),trenutnaRezervacija.getUser().getPrezime()));
        imePrezime.addTextChangedListener(textWatcher);
        broj.setText(trenutnaRezervacija.getUser().getBroj());
        broj.addTextChangedListener(textWatcher);
        rezervacijaBroj.setText(String.valueOf(trenutnaRezervacija.getBrojRezervacije()));
        rezervacijaBroj.addTextChangedListener(textWatcher);
        datumZavrsetka.addTextChangedListener(textWatcher);
        radnik.addTextChangedListener(textWatcher);
        radniSati.addTextChangedListener(textWatcher);
        cijena.addTextChangedListener(textWatcher);
        dan.addTextChangedListener(textWatcher);
        cijenaRada.addTextChangedListener(textWatcher);
        cijenaDijelova.addTextChangedListener(textWatcher);


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


        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPotvrdi dialog = new DialogPotvrdi();
                dialog.show(getSupportFragmentManager(), "Dialog Potvrdi");

            }
        });

        odbaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogOdbaci dialogOdbaci = new DialogOdbaci();
                dialogOdbaci.show(getSupportFragmentManager(), "Dialog Odbaci");
            }
        });

    }

    @Override
    public void odbaciRezervaciju() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Call<Void> odbaciRezervaciju=autoservisAPI.deleteRezervacija(trenutnaRezervacija.getId());
        odbaciRezervaciju.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ServisDetalji.this,"Greška! Servis nije odbačen!\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ServisDetalji.this,"Servis je uspješno odbačen!\nPreusmjerit ćemo vas...", Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Servis.instance.finish();
                            finish();
                        }
                    },2500);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ServisDetalji.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void potvrdiRezervaciju() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        String imeIPrezime=imePrezime.getText().toString();
        String ime="";
        String prezime="";

        if (imeIPrezime.length() !=0){
            if (!imeIPrezime.contains(" ")){
                ime=imeIPrezime;
            }else{
                String[] odvoji = imeIPrezime.split(" ");
                ime=odvoji[0];
                prezime= odvoji[1];
            }
        }

        rezervacija= new Rezervacija(trenutnaRezervacija.getId(), Integer.parseInt(rezervacijaBroj.getText().toString()),dan.getText().toString(),
                ime,prezime,broj.getText().toString(),datumPrimanja.getText().toString(),datumZavrsetka.getText().toString(),
                auto.getText().toString(),Integer.parseInt(godinaProizvodnje.getText().toString()),brojSasije.getText().toString(),
                popravak.getText().toString(),Integer.parseInt(radniSati.getText().toString()), zaposlenik,
                Double.parseDouble(cijena.getText().toString()),Double.parseDouble(cijenaDijelova.getText().toString()),Double.parseDouble(cijenaRada.getText().toString()),"ne");

        Call<Void> updateRezervacija=autoservisAPI.updateRezervacija(trenutnaRezervacija.getUser().getID(),rezervacija);
        updateRezervacija.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ServisDetalji.this,"Greška! Servis nije poslan.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(ServisDetalji.this, "Servis je uspješno poslan!\nPreusmjerit ćemo vas...", Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Servis.instance.finish();
                            finish();
                        }
                    },3000);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ServisDetalji.this, "Greška u komunikaciji sa serverom!\nPokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }
        });
    }
private void prikaziKalendarZavrsetka(){
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
    new DatePickerDialog(ServisDetalji.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
}
    private void prikaziKalendarPrimanja(){
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
        new DatePickerDialog(ServisDetalji.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private TextWatcher textWatcher=new TextWatcher() {
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
            String sDatumZavrsetka = datumZavrsetka.getText().toString().trim();
            String sRadnik = radnik.getText().toString().trim();
            String sRadniSati = radniSati.getText().toString().trim();
            String sCijena = cijena.getText().toString().trim();
            String sRezervacijaBroj = rezervacijaBroj.getText().toString().trim();
            String sDan = dan.getText().toString().trim();
            String sRad=cijenaRada.getText().toString().trim();
            String sCijenaDjelova=cijenaDijelova.getText().toString().trim();



            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                Date d1 = sdf.parse(sDatumPrimanja);
                Date d2 = sdf.parse(sDatumZavrsetka);
                assert d1 != null;
                assert d2 != null;
                long razlika = d2.getTime() - d1.getTime();

                if (razlika < 0) {
                    datumPrimanja.setText("pogrešan unos");
                    datumZavrsetka.setText("pogrešan unos");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!sRadniSati.isEmpty()){
//                double sati = Double.parseDouble(sRadniSati);
//                double radovi = 0 ;
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
                zaposlenik =2;
            }
            if (sRadnik.equals("Luka Vugrinec")){
                zaposlenik = 3;
            }


            posalji.setEnabled(!sDatumPrimanja.isEmpty() && !sDatumZavrsetka.isEmpty()
                    && !sRadnik.isEmpty() && !sRadniSati.isEmpty() && !sRezervacijaBroj.isEmpty() && !sDan.isEmpty()
                    && !sCijenaDjelova.isEmpty());
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