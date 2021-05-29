package app.dinhcuong.diplay.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song implements Parcelable {

    //Song from local
    private String title;
    private String artist;
    private String duration;
    private String album;
    private String path;

    public Song(String title, String artist, String duration, String album, String path) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    //Song from online
    @SerializedName("id_song")
    @Expose
    private String idSong;
    @SerializedName("id_album")
    @Expose
    private String idAlbum;
    @SerializedName("id_genre")
    @Expose
    private String idGenre;
    @SerializedName("id_playlist")
    @Expose
    private String idPlaylist;
    @SerializedName("id_singer")
    @Expose
    private String idSinger;
    @SerializedName("name_song")
    @Expose
    private String nameSong;
    @SerializedName("image_song")
    @Expose
    private String imageSong;
    @SerializedName("link_song")
    @Expose
    private String linkSong;
    @SerializedName("likes_song")
    @Expose
    private String likesSong;
    @SerializedName("name_singer")
    @Expose
    private String nameSinger;
    @SerializedName("lyric_song")
    @Expose
    private String lyricSong;


    protected Song(Parcel in) {
        idSong = in.readString();
        idAlbum = in.readString();
        idGenre = in.readString();
        idPlaylist = in.readString();
        idSinger = in.readString();
        nameSong = in.readString();
        imageSong = in.readString();
        linkSong = in.readString();
        likesSong = in.readString();
        nameSinger = in.readString();
        lyricSong = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getIdSinger() {
        return idSinger;
    }

    public void setIdSinger(String idSinger) {
        this.idSinger = idSinger;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getImageSong() {
        return imageSong;
    }

    public void setImageSong(String imageSong) {
        this.imageSong = imageSong;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public String getLikesSong() {
        return likesSong;
    }

    public void setLikesSong(String likesSong) {
        this.likesSong = likesSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getLyricSong() {
        return lyricSong;
    }

    public void setLyricSong(String lyricSong) {
        this.lyricSong = lyricSong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idSong);
        dest.writeString(idAlbum);
        dest.writeString(idGenre);
        dest.writeString(idPlaylist);
        dest.writeString(idSinger);
        dest.writeString(nameSong);
        dest.writeString(imageSong);
        dest.writeString(linkSong);
        dest.writeString(likesSong);
        dest.writeString(nameSinger);
        dest.writeString(lyricSong);
    }
}