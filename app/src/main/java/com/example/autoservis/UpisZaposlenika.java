package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

public class UpisZaposlenika extends AppCompatActivity implements DialogPosaljiZaposlenika.DialogLisener {


    private boolean pritisnuti_back_btn;
    private Button izadi;

    private Button posalji;
    private TextView ime;
    private TextView prezime;
    private TextView broj;
    private  TextView email;
    private User trenutniUser;
    private Zaposlenik zaposlenici;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.client(okHttpClient).build();
    AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upis_zaposlenika);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        izadi=findViewById(R.id.izađi);
        posalji=findViewById(R.id.posalji);
        ime=findViewById(R.id.ime);
        prezime=findViewById(R.id.prezime);
        broj=findViewById(R.id.broj);
        email=findViewById(R.id.email);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);


        ime.addTextChangedListener(textWatcher);
        prezime.addTextChangedListener(textWatcher);
        broj.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        trenutniUser=(User) getIntent().getSerializableExtra("trenutniUser");
        assert trenutniUser!=null;

        izadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPosaljiZaposlenika dialog=new DialogPosaljiZaposlenika();
                dialog.show(getSupportFragmentManager(),"Dialog Posalji Zaposlenika");
            }
        });
    }


    public void posaljiZaposlenika() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        zaposlenici = new Zaposlenik(ime.getText().toString(),prezime.getText().toString(),broj.getText().toString(),email.getText().toString());


        Call<Void> rezervacijaCall = autoservisAPI.noviZaposlenik(zaposlenici);

        rezervacijaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(UpisZaposlenika.this, "Greška! zaposlenik nije upisan.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(UpisZaposlenika.this, "Zaposlenik je upisan!\nPreusmjerit ćemo vas na Početni zaslon.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(UpisZaposlenika.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();
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
            String sIme=ime.getText().toString().trim();
            String sPrezime=prezime.getText().toString().trim();
            String sBroj=broj.getText().toString().trim();
            String sEmail=email.getText().toString().trim();
            boolean dobroIme=false;
            boolean dobroPrezime=false;

            if(sIme.length() < 3){
                ime.setError("Ime ne može biti manje od 3 slova");
            }else{
                ime.getText().toString().trim();
                ime.setError(null);
                dobroIme=true;
            }

            if(sPrezime.length() < 3){
                prezime.setError("Prezime ne može biti manje od 3 slova");
            }else{
                prezime.getText().toString().trim();
                prezime.setError(null);
                dobroPrezime=true;
            }
            
            posalji.setEnabled(!sIme.isEmpty() && !sPrezime.isEmpty() && !sBroj.isEmpty() && !sEmail.isEmpty() && dobroIme && dobroPrezime);
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