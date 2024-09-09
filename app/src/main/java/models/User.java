package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private int ID;
    @SerializedName("broj")
    private String  Broj;
    @SerializedName("ime")
    private String Ime;
    @SerializedName("prezime")
    private String Prezime;
    @SerializedName("email")
    private String Email;
    @SerializedName("lozinka")
    private String Lozinka;
    @SerializedName("uloge")
    private String Uloge;

    public User(String ime, String prezime, String broj, String Email, String lozinka){
        this.Ime=ime;
        this.Prezime=prezime;
        this.Email=Email;
        this.Broj=broj;
        this.Lozinka=lozinka;
    }
    public User(String ime, String prezime, String broj, String Email, String lozinka,String uloge){
        this.Ime=ime;
        this.Prezime=prezime;
        this.Email=Email;
        this.Broj=broj;
        this.Lozinka=lozinka;
        this.Uloge=uloge;
    }
    public User(){}

    public User(String Email, String lozinka){
        this.Email=Email;
        this.Lozinka=lozinka;
    }

    public User(int ID, String ime, String prezime){
        this.ID=ID;
        this.Ime=ime;
        this.Prezime=prezime;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBroj() {
        return Broj;
    }

    public void setBroj(String broj) {
        Broj = broj;
    }

    public String getIme() {
        return Ime;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public String getPrezime() {
        return Prezime;
    }

    public void setPrezime(String prezime) {
        Prezime = prezime;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLozinka() {
        return Lozinka;
    }

    public void setLozinka(String lozinka) {
        Lozinka = lozinka;
    }

    public String getUloge() {
        return Uloge;
    }

    public void setUloge(String uloge) {
        Uloge = uloge;
    }
}
