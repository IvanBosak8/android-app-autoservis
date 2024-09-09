package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recenzija implements Serializable {

    private int id;
    private float ocjena;

    private String recenzija;

    @SerializedName("korisnik")
    private User user;

    public Recenzija( float ocjena, String recenzija){
        this.ocjena=ocjena;
        this.recenzija=recenzija;

    }

    public Recenzija(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecenzija() {
        return recenzija;
    }

    public void setRecenzija(String recenzija) {
        this.recenzija = recenzija;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getOcjena() {
        return ocjena;
    }

    public void setOcjena(float ocjena) {
        this.ocjena = ocjena;
    }
}
