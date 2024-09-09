package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import models.AutoservisAPI;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistracijaActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile
            ("^" + "(?=.*[0-9])"+
                    "(?=.*[a-zA-Z])"+
                    ".{6,20}"+
                    "$");


    EditText ime;
    EditText prezime;
    EditText broj;
    EditText username;
    EditText password;
    EditText password_repeat;
    Button registriraj_se;
    TextView prijavi_se;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        ime=(EditText) findViewById(R.id.ime);
        prezime= (EditText) findViewById(R.id.prezime);
        username=(EditText) findViewById(R.id.username);
        broj=(EditText) findViewById(R.id.broj);
        password=(EditText) findViewById(R.id.edittext_password);
        password_repeat = (EditText) findViewById(R.id.edittext_password_repeat);
        registriraj_se = (Button) findViewById(R.id.btn_registriraj_se);
        prijavi_se = (TextView) findViewById(R.id.textview_prijavi_se);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);


        ImageView imageViewShowHidePass = findViewById(R.id.imageView_show_hide_pass);
        ImageView imageViewShowHidePass1 = findViewById(R.id.imageView_show_hide_pass1);
        imageViewShowHidePass.setImageResource(R.drawable.password_toggle_off);
        imageViewShowHidePass1.setImageResource(R.drawable.password_toggle_off);
        imageViewShowHidePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.password_toggle_off);
                }else{
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.password_toggle_on);
                }
            }
        });

        imageViewShowHidePass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password_repeat.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    password_repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePass1.setImageResource(R.drawable.password_toggle_off);
                }else{
                    password_repeat.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass1.setImageResource(R.drawable.password_toggle_on);
                }

            }
        });



        prijavi_se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegistracijaActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        registriraj_se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValjanoIme() | !ValjanoPrezime() | !ValjaniEmail() | !ValjaniPassword() | !ValjaniBroj()){
                } else{
                    User user =null;
                    try {
                        user = new User(ime.getText().toString().trim(), prezime.getText().toString().trim(),broj.getText().toString().trim(),
                                username.getText().toString().trim().toLowerCase(), LoginActivity.enkripcija(password.getText().toString()),"USER" );

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    RegistracijaUsera(user);
                }
            }
        });
    }

    private void  RegistracijaUsera(User user){
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        AutoservisAPI autoservisAPI = retrofit.create(AutoservisAPI.class);
        Call<Void> addUser = autoservisAPI.add(user);

        addUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    Toast.makeText(RegistracijaActivity.this,"Korisnik sa unešenom e-mail adresom već postoji.",Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                        }
                    },500);
                }else{
                    findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    Toast.makeText(RegistracijaActivity.this,"Uspješno ste se registrirali. Preusmjerit čemo vas na prijavu!",Toast.LENGTH_LONG).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent= new Intent(RegistracijaActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    },1500);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                Toast.makeText(RegistracijaActivity.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije...", Toast.LENGTH_LONG).show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    }
                },500);
            }
        });
    }
    private boolean ValjanoIme() {

        String inputIme = ime.getText().toString().trim();
        if (inputIme.isEmpty()) {
            ime.setError("Ime ne može biti prazno");
            return false;
        } else if (inputIme.length() < 3) {
            ime.setError("Ime ne može biti manje od 3 slova");
            return false;

        } else {
            //    ime.setText(null);
            return true;
        }
    }
    private boolean ValjanoPrezime() {

        String inputPrezime = prezime.getText().toString().trim();
        if (inputPrezime.isEmpty()) {
            prezime.setError("Prezime ne može biti prazno");
            return false;
        } else if (inputPrezime.length() < 3) {
            prezime.setError("Prezime ne može biti manje od 3 slova");
            return false;

        } else {

            return true;
        }
    }
    private boolean ValjaniEmail() {

        String inputEmail = username.getText().toString().trim();
        if (inputEmail.isEmpty()) {
            username.setError("E-mail ne može biti prazno");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            username.setError("Format E-mail adrese nije valjan");
            return false;

        } else {

            return true;
        }
    }
    private boolean ValjaniPassword() {
        String inputPw = password.getText().toString();
        String inputPonovljeniPW = password_repeat.getText().toString();
        if (inputPw.isEmpty()) {
            password.setError("Lozinka ne može biti prazno");
            password_repeat.setError("Lozinka ne može biti prazno");
            return false;
        } else if (inputPw.length() < 6) {
            password.setError("Lozinka je prekratka");
            return false;
        } else if (inputPw.length() > 20) {
            password.setError("Lozinka je preduga");
            return false;

        } else if (!PASSWORD_PATTERN.matcher(inputPw).matches()) {
            password.setError("Lozinka ne sadrži broj");
            return false;

        } else if (!inputPw.equals(inputPonovljeniPW)) {
            password.setError("Lozinke se ne podudaraju");
            password_repeat.setError("Lozinke se ne podudaraju");
            return false;

        } else {
            //   password.setError(null);
            return true;
        }

    }
    private boolean ValjaniBroj() {
        String inputBroj = broj.getText().toString();
        if (inputBroj.isEmpty()) {
            broj.setError("Broj telefona ne može biti prazni");
            return false;
        } else {
            return true;
        }
    }
}