package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album {

    @SerializedName("id_album")
    @Expose
    private String idAlbum;
    @SerializedName("name_album")
    @Expose
    private String nameAlbum;
    @SerializedName("image_album")
    @Expose
    private String imageAlbum;
    @SerializedName("id_singer")
    @Expose
    private String idSinger;
    @SerializedName("likes_album")
    @Expose
    private String likesAlbum;

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getImageAlbum() {
        return imageAlbum;
    }

    public void setImageAlbum(String imageAlbum) {
        this.imageAlbum = imageAlbum;
    }

    public String getIdSinger() {
        return idSinger;
    }

    public void setIdSinger(String idSinger) {
        this.idSinger = idSinger;
    }

    public String getLikesAlbum() {
        return likesAlbum;
    }

    public void setLikesAlbum(String likesAlbum) {
        this.likesAlbum = likesAlbum;
    }

}