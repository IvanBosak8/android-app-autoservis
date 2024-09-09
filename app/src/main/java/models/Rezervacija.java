package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rezervacija implements Serializable {
    private int id;
    private String ime;
    private String radnikIme;
    private String radnikPrezime;
    private int radnikId;
    private String prezime;
    private String broj;
    private String auto;
    private int godinaProizvodnje;
    private String brojSasije;
    private String popravak;
    private double cijena;
    private String datumPrimanja;
    private String datumZavrsetka;
    private String radnik;
    private int radniSati;
    private String dan;
    private int brojRezervacije;
    private double cijenaDijelova;
    private double cijenaRada;
    private String razlog;
    @SerializedName("korisnik")
    private User user;
    @SerializedName("radnici")
    private Zaposlenik zaposlenik;

    public Rezervacija(int brojRezervacije, String dan, String ime, String prezime,
                       String broj, String datumPrimanja,String datumZavrsetka,
                       String auto, int godinaProizvodnje, String brojSasije,
                       String popravak,int radnikID,int radniSati, double cijena,
                       double cijenaDijelova, double cijenaRada,String razlog) {
        this.brojRezervacije =brojRezervacije;
        this.ime=ime;
        this.prezime=prezime;
        this.broj=broj;
        this.auto=auto;
        this.datumZavrsetka =datumZavrsetka;
        this.datumPrimanja=datumPrimanja;
        this.dan =dan;
        this.godinaProizvodnje =godinaProizvodnje;
        this.brojSasije =brojSasije;
        this.popravak=popravak;
        this.zaposlenik= new Zaposlenik(radnikID,"","","","");
        this.radniSati=radniSati;
        this.cijena=cijena;
        this.cijenaDijelova=cijenaDijelova;
        this.cijenaRada=cijenaRada;
        this.razlog=razlog;


        //user upis
    }

    public Rezervacija(int id,int brojRezervacije, String dan,String ime,String prezime,
                       String broj,String datumPrimanja, String datumZavrsetka,
                       String auto,int godinaProizvodnje, String brojSasije,
                       String popravak, int radniSati, int radnikID, double cijena,
                       double cijenaDijelova, double cijenaRada) {
        this.id=id;
        this.brojRezervacije =brojRezervacije;
        this.dan =dan;
        this.ime=ime;
        this.prezime=prezime;
        this.broj=broj;
        this.datumPrimanja =datumPrimanja;
        this.datumZavrsetka =datumZavrsetka;
        this.auto=auto;
        this.godinaProizvodnje =godinaProizvodnje;
        this.brojSasije =brojSasije;
        this.popravak=popravak;
        this.radniSati =radniSati;
        this.zaposlenik= new Zaposlenik(radnikID,"","","","");
        this.cijena =cijena;
        this.cijenaDijelova=cijenaDijelova;
        this.cijenaRada=cijenaRada;
        //servisdetalji

    }

    public Rezervacija(int id, String razlog){
        this.id=id;
        this.razlog=razlog;
        //razlog
    }

    public Rezervacija(String ime, String prezime, String broj,double cijena, String datumPrimanja,
                       String datumZavrsetka, int radnikID, int radniSati, String auto, int godinaProizvodnje,
                       String brojSasije, String popravak,  String dan, int brojRezervacije,double cijenaRada,
                       double cijenaDijelova, String razlog) {
    this.ime=ime;
    this.prezime=prezime;
    this.broj=broj;
    this.cijena =cijena;
    this.datumPrimanja =datumPrimanja;
    this.datumZavrsetka =datumZavrsetka;
    this.zaposlenik= new Zaposlenik(radnikID,"","","","");
    this.radniSati =radniSati;
    this.auto=auto;
    this.godinaProizvodnje =godinaProizvodnje;
    this.brojSasije =brojSasije;
    this.popravak=popravak;
    this.dan =dan;
    this.brojRezervacije =brojRezervacije;
    this.cijenaRada=cijenaRada;
    this.cijenaDijelova=cijenaDijelova;
    this.razlog=razlog;
    //admin upis
    }

    public Rezervacija(int brojRezervacije, String dan, String ime, String prezime,
                       String broj, String datumPrimanja,
                       String auto, int godinaProizvodnje, String brojSasije,
                       String popravak,String radnik) {
        this.brojRezervacije =brojRezervacije;
        this.ime=ime;
        this.prezime=prezime;
        this.broj=broj;
        this.auto=auto;
        this.datumPrimanja=datumPrimanja;
        this.dan =dan;
        this.godinaProizvodnje =godinaProizvodnje;
        this.brojSasije =brojSasije;
        this.popravak=popravak;
        this.radnik =radnik;//user upis
    }

    public Rezervacija(int id,int brojRezervacije, String dan,String ime,String prezime,
                       String broj,String datumPrimanja, String datumZavrsetka,
                       String auto,int godinaProizvodnje, String brojSasije,
                       String popravak, int radniSati, int radnikID, double cijena, double cijenaDijelova, double cijenaRada,String razlog) {
        this.id = id;
        this.brojRezervacije = brojRezervacije;
        this.dan = dan;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.datumPrimanja = datumPrimanja;
        this.datumZavrsetka = datumZavrsetka;
        this.auto = auto;
        this.godinaProizvodnje = godinaProizvodnje;
        this.brojSasije = brojSasije;
        this.popravak = popravak;
        this.radniSati = radniSati;
        this.zaposlenik= new Zaposlenik(radnikID,"","","","");
        this.cijena = cijena;
        this.cijenaDijelova=cijenaDijelova;
        this.cijenaRada=cijenaRada;
        this.razlog=razlog;
    }


    public User getUser(){
        return user;
    }
    public  void setUser(User user){
        this.user=user;
    }

    public Zaposlenik getZaposlenik() {
        return zaposlenik;
    }

    public void setZaposlenik(Zaposlenik zaposlenik) {
        this.zaposlenik = zaposlenik;
    }

    public Rezervacija(){}

//    public Rezervacija(int ID, String ime, String prezime, String broj, String auto,int godinaProizvodnje, String brojSasije,String popravak, double cijena, String datumPrimanja, String datumZavrsetka, String radnik, int radniSati,String dana, User user) {
//        this.ID = ID;
//        this.ime = ime;
//        this.prezime = prezime;
//        this.broj = broj;
//        Auto=auto;
//        GodinaProizvodnje=godinaProizvodnje;
//        BrojSasije=brojSasije;
//        Popravak=popravak;
//        Cijena = cijena;
//        DatumPrimanja = datumPrimanja;
//        DatumZavrsetka = datumZavrsetka;
//        Radnik = radnik;
//        RadniSati = radniSati;
//        Dana= dana;
//        this.user = user;
//    }

    public int getId() {
        return id;
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

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public int getGodinaProizvodnje() {
        return godinaProizvodnje;
    }

    public void setGodinaProizvodnje(int godinaProizvodnje) {
        this.godinaProizvodnje = godinaProizvodnje;
    }

    public String getBrojSasije() {
        return brojSasije;
    }

    public void setBrojSasije(String brojSasije) {
        this.brojSasije = brojSasije;
    }

    public String getPopravak() {
        return popravak;
    }

    public void setPopravak(String popravak) {
        this.popravak = popravak;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public String getDatumPrimanja() {
        return datumPrimanja;
    }

    public void setDatumPrimanja(String datumPrimanja) {
        this.datumPrimanja = datumPrimanja;
    }

    public String getDatumZavrsetka() {
        return datumZavrsetka;
    }

    public void setDatumZavrsetka(String datumZavrsetka) {
        this.datumZavrsetka = datumZavrsetka;
    }

    public String getRadnik() {
        return radnik;
    }

    public void setRadnik(String radnik) {
        this.radnik = radnik;
    }

    public int getRadniSati() {
        return radniSati;
    }

    public void setRadniSati(int radniSati) {
        this.radniSati = radniSati;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }

    public int getBrojRezervacije() {
        return brojRezervacije;
    }

    public void setBrojRezervacije(int brojRezervacije) {
        this.brojRezervacije = brojRezervacije;
    }

    public String getRazlog() {
        return razlog;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }

    public double getCijenaDijelova() {
        return cijenaDijelova;
    }

    public void setCijenaDijelova(double cijenaDijelova) {
        this.cijenaDijelova = cijenaDijelova;
    }

    public double getCijenaRada() {
        return cijenaRada;
    }

    public void setCijenaRada(double cijenaRada) {
        this.cijenaRada = cijenaRada;
    }

    public String getRadnikIme() {
        return radnikIme;
    }

    public void setRadnikIme(String radnikIme) {
        this.radnikIme = radnikIme;
    }

    public String getRadnikPrezime() {
        return radnikPrezime;
    }

    public void setRadnikPrezime(String radnikPrezime) {
        this.radnikPrezime = radnikPrezime;
    }

    public int getRadnikId() {
        return radnikId;
    }

    public void setRadnikId(int radnikId) {
        this.radnikId = radnikId;
    }
}
