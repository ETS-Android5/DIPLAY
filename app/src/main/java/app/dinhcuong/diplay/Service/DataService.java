package app.dinhcuong.diplay.Service;

import java.util.List;

import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Genre;
import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Slider;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.Model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @GET("handler_slider.php")
    Call<List<Slider>> getDataSlider();


    @GET("handler_mood.php")
    Call<List<Mood>> getDataMood();

    @GET("handler_album.php")
    Call<List<Album>> getDataAlbum();

    @GET("handler_genre.php")
    Call<List<Genre>> getDataGenre();

    @GET("handler_playlist.php")
    Call<List<Playlist>> getDataPlaylist();

    @GET("handler_song_favorite.php")
    Call<List<Song>> getSongFavorite();

    @FormUrlEncoded
    @POST("handler_song.php")
    Call<List<Song>> getSongPlaylist(@Field("id_playlist") String id_playlist);

    @FormUrlEncoded
    @POST("handler_song_likes.php")
    Call<String> handlerLikeForSong(@Field("id_song") String id_song, @Field("number_of_like") String number_of_like, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_login.php")
    Call<User> handlerLogin(@Field("email_user") String email_user, @Field("password_user") String password_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_register.php")
    Call<String> handlerRegister(@Field("name_user") String name_user, @Field("email_user") String email_user, @Field("password_user") String password_user);
}
