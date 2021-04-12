package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist implements Serializable {

    @SerializedName("id_playlist")
    @Expose
    private String idPlaylist;
    @SerializedName("name_playlist")
    @Expose
    private String namePlaylist;
    @SerializedName("image_playlist")
    @Expose
    private String imagePlaylist;

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public String getImagePlaylist() {
        return imagePlaylist;
    }

    public void setImagePlaylist(String imagePlaylist) {
        this.imagePlaylist = imagePlaylist;
    }

}