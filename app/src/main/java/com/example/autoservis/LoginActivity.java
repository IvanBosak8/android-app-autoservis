package com.example.autoservis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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

public class LoginActivity extends AppCompatActivity {

    public static final String SERVER = "http://192.168.18.2:8090";

    private EditText text_Username;
    private EditText textPassword;
    private Button btn_Login;
    private TextView reg;
    private TextView restartaj_lozinku;
    private String email, password;
    private TextView recenzije;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_Username = findViewById(R.id.edittext_username);
        textPassword = findViewById(R.id.textPassword);
        btn_Login = findViewById(R.id.btn_login);
        reg = findViewById(R.id.textView4);
        restartaj_lozinku = findViewById(R.id.textview_zaboravili_ste_lozinku);
        recenzije=findViewById(R.id.recenzije);
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);


        ImageView imageViewShowHidePass = findViewById(R.id.imageView_show_hide_pass);
        imageViewShowHidePass.setImageResource(R.drawable.password_toggle_off);
        imageViewShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    textPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.password_toggle_off);
                }else{
                    textPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.password_toggle_on);
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Registracija_intent = new Intent(LoginActivity.this, RegistracijaActivity.class);
                startActivity(Registracija_intent);
            }
        });

        restartaj_lozinku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Lozinka_intent = new Intent(LoginActivity.this, LozinkaActivity.class);
                startActivity(Lozinka_intent);
            }
        });

        recenzije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Lozinka_intent = new Intent(LoginActivity.this, Recenzije.class);
                startActivity(Lozinka_intent);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValjaniEmail() | !ValjaniPassword()) {
                } else {

                    email = text_Username.getText().toString().trim().toLowerCase();
                    password = textPassword.getText().toString();

                    findViewById(R.id.loading).setVisibility(View.VISIBLE);

                    User korisnik = null;
                    try {
                        korisnik = new User(email, enkripcija(password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    provjeriLogin(korisnik);
                }
            }
        });
    }
    protected static String enkripcija(String password) throws Exception{
        SecretKeySpec key = generirajKljuc();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] value = c.doFinal(password.getBytes());
        return Base64.encodeToString(value, Base64.DEFAULT);

    }

    private static SecretKeySpec generirajKljuc() throws Exception{
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = "Autoservis123#".getBytes(StandardCharsets.UTF_8);
        messageDigest.update(bytes, 0, bytes.length);
        byte[] key = messageDigest.digest();
        return new SecretKeySpec(key, "AES");
    }

    private void provjeriLogin(User user) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LoginActivity.SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();

        AutoservisAPI autosrvisAPI = retrofit.create(AutoservisAPI.class);
        Call<User> provjera = autosrvisAPI.login(user);

        provjera.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    User trenutni = response.body();
                    Intent intent;

                    if (trenutni.getUloge().equals("USER")) {

                        intent = new Intent(LoginActivity.this, UserHomeUI.class);
                        intent.putExtra("trenutniUser", trenutni);

                    } else {

                        intent = new Intent(LoginActivity.this, AdminHomeUI.class);
                        intent.putExtra("trenutniUser", trenutni);
                    }
                    startActivity(intent);
                } else {
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "E-mail ili lozinka nisu točni.\nPokušajte ponovo...", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Greška u komunikaciji sa serverom. Pokušajte ponovo kasnije.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean ValjaniEmail() {
        String inputEmail = text_Username.getText().toString().trim();
        if (inputEmail.isEmpty()) {
            text_Username.setError("E-mail polje ne može biti prazno");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            text_Username.setError("Format E-mail adrese nije valjan");
            return false;

        } else {
            text_Username.setError(null);
            return true;
        }
    }


    private boolean ValjaniPassword() {

        String inputPw = textPassword.getText().toString();
        if (inputPw.isEmpty()) {
            textPassword.setError("Polje Lozinka ne može biti prazno");
            return false;
        } else {
            textPassword.setError(null);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
    }
}