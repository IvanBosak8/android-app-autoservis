package models;

import com.example.autoservis.Zaposlenici;

import java.io.Serializable;

public class Zaposlenik implements Serializable {
    private int id;
    private String ime;
    private String prezime;
    private String broj;
    private String email;

    public int getId() {
        return id;
    }

    public Zaposlenik( String ime, String prezime, String broj, String email){
        this.ime=ime;
        this.prezime=prezime;
        this.broj=broj;
        this.email=email;

    }

    public Zaposlenik(int id,String ime, String prezime, String broj, String email){
        this.id=id;
        this.ime=ime;
        this.prezime=prezime;
        this.broj=broj;
        this.email=email;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
