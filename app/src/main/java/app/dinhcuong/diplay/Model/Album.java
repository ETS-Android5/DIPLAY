package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {

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
    @SerializedName("follows_album")
    @Expose
    private String followsAlbum;
    @SerializedName("size_album")
    @Expose
    private Integer sizeAlbum;
    @SerializedName("name_singer")
    @Expose
    private Object nameSinger;

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

    public String getFollowsAlbum() {
        return followsAlbum;
    }

    public void setFollowsAlbum(String followsAlbum) {
        this.followsAlbum = followsAlbum;
    }

    public Integer getSizeAlbum() {
        return sizeAlbum;
    }

    public void setSizeAlbum(Integer sizeAlbum) {
        this.sizeAlbum = sizeAlbum;
    }

    public Object getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(Object nameSinger) {
        this.nameSinger = nameSinger;
    }

}