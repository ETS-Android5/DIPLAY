package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

@SerializedName("id_slider")
@Expose
private String idSlider;
@SerializedName("image_slider")
@Expose
private String imageSlider;
@SerializedName("info_slider")
@Expose
private String infoSlider;
@SerializedName("id_song")
@Expose
private String idSong;

public String getIdSlider() {
return idSlider;
}

public void setIdSlider(String idSlider) {
this.idSlider = idSlider;
}

public String getImageSlider() {
return imageSlider;
}

public void setImageSlider(String imageSlider) {
this.imageSlider = imageSlider;
}

public String getInfoSlider() {
return infoSlider;
}

public void setInfoSlider(String infoSlider) {
this.infoSlider = infoSlider;
}

public String getIdSong() {
return idSong;
}

public void setIdSong(String idSong) {
this.idSong = idSong;
}

}