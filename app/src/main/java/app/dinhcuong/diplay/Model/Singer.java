package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Singer implements Serializable {

    @SerializedName("id_singer")
    @Expose
    private String idSinger;
    @SerializedName("name_singer")
    @Expose
    private String nameSinger;
    @SerializedName("image_singer")
    @Expose
    private String imageSinger;
    @SerializedName("follows_singer")
    @Expose
    private String followsSinger;
    @SerializedName("size_singer")
    @Expose
    private Integer sizeSinger;
    @SerializedName("size_album_singer")
    @Expose
    private Integer sizeAlbumSinger;

    public String getIdSinger() {
        return idSinger;
    }

    public void setIdSinger(String idSinger) {
        this.idSinger = idSinger;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getImageSinger() {
        return imageSinger;
    }

    public void setImageSinger(String imageSinger) {
        this.imageSinger = imageSinger;
    }

    public String getFollowsSinger() {
        return followsSinger;
    }

    public void setFollowsSinger(String followsSinger) {
        this.followsSinger = followsSinger;
    }

    public Integer getSizeSinger() {
        return sizeSinger;
    }

    public void setSizeSinger(Integer sizeSinger) {
        this.sizeSinger = sizeSinger;
    }

    public Integer getSizeAlbumSinger() {
        return sizeAlbumSinger;
    }

    public void setSizeAlbumSinger(Integer sizeAlbumSinger) {
        this.sizeAlbumSinger = sizeAlbumSinger;
    }

}