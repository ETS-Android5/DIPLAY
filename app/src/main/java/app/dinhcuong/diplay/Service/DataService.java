package app.dinhcuong.diplay.Service;

import android.widget.EditText;

import java.util.List;

import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Genre;
import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Singer;
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

    @GET("handler_singer.php")
    Call<List<Singer>> getDataSinger();

    @FormUrlEncoded
    @POST("handler_song_likes.php")
    Call<String> handlerLikeForSong(@Field("id_user") String id_user, @Field("id_song") String id_song, @Field("number_of_like") String number_of_like, @Field("action") String action);

    //Playlist

    @FormUrlEncoded
    @POST("handler_song.php")
    Call<List<Song>> getSongPlaylist(@Field("id_playlist") String id_playlist);

    @FormUrlEncoded
    @POST("handler_playlist_follows.php")
    Call<String> handlerFollowForPlaylist(@Field("id_user") String id_user, @Field("id_playlist") String id_playlist, @Field("number_of_follow") String number_of_follow, @Field("action") String action);

    //Handler playlist created
    @FormUrlEncoded
    @POST("handler_library_playlist_create.php")
    Call<String> handlerCreatePlaylist(@Field("id_user_created") String id_user_created, @Field("name_playlist") String name_playlist, @Field("image_playlist") String image_playlist);

    @FormUrlEncoded
    @POST("handler_library_playlist_create.php")
    Call<String> handlerCreatePlaylistRemove(@Field("id_user") String id_user, @Field("id_playlist") String id_playlist);

    @FormUrlEncoded
    @POST("handler_library_playlist.php")
    Call<List<Playlist>> getDataLibraryPlaylist(@Field("id_user") String id_user, @Field("action") String action);

    //Album
    @FormUrlEncoded
    @POST("handler_song.php")
    Call<List<Song>> getSongAlbum(@Field("id_album") String id_album);

    @FormUrlEncoded
    @POST("handler_album_follows.php")
    Call<String> handlerFollowForAlbum(@Field("id_user") String id_user, @Field("id_album") String id_album, @Field("number_of_follow") String number_of_follow, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_library_album.php")
    Call<List<Album>> getDataLibraryAlbum(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("handler_library_singer.php")
    Call<List<Singer>> getDataLibrarySinger(@Field("id_user") String id_user);


    @FormUrlEncoded
    @POST("handler_login.php")
    Call<User> handlerLogin(@Field("email_user") String email_user, @Field("password_user") String password_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_register.php")
    Call<String> handlerRegister(@Field("name_user") String name_user, @Field("email_user") String email_user, @Field("password_user") String password_user);

    //Search
    @FormUrlEncoded
    @POST("handler_search.php")
    Call<List<Song>> handlerSearchSong(@Field("keyword") String keyword, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_search.php")
    Call<List<Playlist>> handlerSearchPlaylist(@Field("keyword") String keyword, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_search.php")
    Call<List<Album>> handlerSearchAlbum(@Field("keyword") String keyword, @Field("position") String position);

    //Add song to playlist
    @FormUrlEncoded
    @POST("handler_playlist_add_song.php")
    Call<String> handlerAddForPlaylistCreated(@Field("id_user") String id_user, @Field("id_playlist") String id_playlist, @Field("id_song") String id_song, @Field("action") String action);



    //Album
    @FormUrlEncoded
    @POST("handler_song.php")
    Call<List<Song>> getSongPlaylistCreated(@Field("id_playlist") String id_playlist, @Field("id_user") String id_user);


    //Get data playlist like songs
    @FormUrlEncoded
    @POST("handler_song_likes.php")
    Call<String> handlerGetNumberOfLikeSongs(@Field("id_user") String id_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_song_likes.php")
    Call<List<Song>> getLikedSongsPlaylist(@Field("id_user") String id_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_singer_follows.php")
    Call<String> handlerFollowForSinger(@Field("id_user") String id_user, @Field("id_singer") String id_singer, @Field("number_of_follow") String number_of_follow, @Field("action") String action);


    @FormUrlEncoded
    @POST("handler_singer_data.php")
    Call<List<Album>> handlerGetDataAlbumForSinger(@Field("id_singer") String id_singer, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_singer_data.php")
    Call<List<Song>> handlerGetDataSongForSinger(@Field("id_singer") String id_singer, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_mood_detail.php")
    Call<List<Playlist>> handlerGetDataPlaylistsOfMood(@Field("id_mood") String id_mood,@Field("position") String position);

    @FormUrlEncoded
    @POST("handler_mood_detail.php")
    Call<List<Album>> handlerGetDataAlbumsOfMood(@Field("id_mood") String id_mood, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_genre_detail.php")
    Call<List<Playlist>> handlerGetDataPlaylistsOfGenre(@Field("id_genre") String id_genre,@Field("position") String position);

    @FormUrlEncoded
    @POST("handler_genre_detail.php")
    Call<List<Album>> handlerGetDataAlbumsOfGenre(@Field("id_genre") String id_genre, @Field("position") String position);

    @FormUrlEncoded
    @POST("handler_user.php")
    Call<String> handlerDeleteUser(@Field("id_user") String id_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_user.php")
    Call<String> handlerEditInfo(@Field("id_user") String id_user, @Field("name_user") String name_user, @Field("email_user") String email_user, @Field("action") String action);

    @FormUrlEncoded
    @POST("handler_user.php")
    Call<String> handlerChangePassword(@Field("id_user") String id_user, @Field("password_user") String password_user, @Field("action") String action);


}
