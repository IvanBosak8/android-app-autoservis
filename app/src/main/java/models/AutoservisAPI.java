package models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AutoservisAPI {
    @POST("/login")
    Call<User> login(@Body User user);
    @POST("/user/add")
    Call<Void> add(@Body User user);

    @GET("/user/{userID}rezervacija")
    Call<List<Rezervacija>> getRezervacijaByUser(@Path("userID")int id);
    @GET("/rezervacija")
    Call<List<Rezervacija>> getRezervacija();
    @GET("/zaposlenik")
    Call<List<Zaposlenik>> getZaposlenik();
    @POST("/zaposlenik/add")
    Call<Void> noviZaposlenik(@Body Zaposlenik zaposlenik);
    @DELETE("/zaposlenik/delete/{id}")
    Call<Void>izbrisiZaposlenika(@Path("id")int id);
    @PUT("/zaposlenik/update/{id}")
    Call<Void> updateZaposlenik(@Path("id")int id, @Body Zaposlenik zaposlenik);
    @GET("/posaljiLozinku/{email}")
    Call<Void> getLozinka(@Path("email")String email);
    @GET("/rezervacija/broj")
    Call<Integer> getBrojRezervacije();
    @GET("/rezervacija/nedefinirano")
    Call<List<Rezervacija>> getNedefiniraneRezervacije();
    @POST("/user/{userID}/rezervacija")
    Call<Void> novaRezervacija(@Path("userID") int userID, @Body Rezervacija rezervacija);
    @PUT("/user/{userID}/rezervacija{id}")
    Call<Void> updateRezervacija(@Path("userID")int userID,@Body Rezervacija rezervacija);
    @PUT("/user/{userID}/rezervacijak{id}")
    Call<Void> izbrisiRezervacija(@Path("userID")int userID,@Body Rezervacija rezervacija);
    @DELETE("/user/delete/{id}")
    Call<Void> deleteUser(@Path("userID")int userID);
    @PUT("/user/update")
    Call<Void> updateUser(@Path("userID")int userID,@Body User user);
    @GET("/user/{id}/rezervacija/nedovrseno")
    Call<List<Rezervacija>> getNedovrseneRezervacije(@Path("id")int id);
    @GET("/servis")
    Call<List<Rezervacija>> getServis();
    @PUT("/user/{userID}/rezervacijas{id}")
    Call<Void>updateDatum(@Path("userID")int userID, @Body Rezervacija rezervacija);
    @DELETE("/rezervacija{id}")
    Call<Void> deleteRezervacija(@Path("id")int id);
    @GET("/recenzija")
    Call<List<Recenzija>> getRecenzija();
    @POST("/user/{userID}/recenzije")
    Call<Void> novaRecenzija(@Path("userID") int id, @Body Recenzija recenzija);


}
