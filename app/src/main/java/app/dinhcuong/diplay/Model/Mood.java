package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mood {

@SerializedName("id_mood")
@Expose
private String idMood;
@SerializedName("name_mood")
@Expose
private String nameMood;
@SerializedName("image_mood")
@Expose
private String imageMood;

public String getIdMood() {
return idMood;
}

public void setIdMood(String idMood) {
this.idMood = idMood;
}

public String getNameMood() {
return nameMood;
}

public void setNameMood(String nameMood) {
this.nameMood = nameMood;
}

public String getImageMood() {
return imageMood;
}

public void setImageMood(String imageMood) {
this.imageMood = imageMood;
}

}