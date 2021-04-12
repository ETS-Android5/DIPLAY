package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre {

@SerializedName("id_genre")
@Expose
private String idGenre;
@SerializedName("name_genre")
@Expose
private String nameGenre;
@SerializedName("image_genre")
@Expose
private String imageGenre;
@SerializedName("id_mood")
@Expose
private String idMood;

public String getIdGenre() {
return idGenre;
}

public void setIdGenre(String idGenre) {
this.idGenre = idGenre;
}

public String getNameGenre() {
return nameGenre;
}

public void setNameGenre(String nameGenre) {
this.nameGenre = nameGenre;
}

public String getImageGenre() {
return imageGenre;
}

public void setImageGenre(String imageGenre) {
this.imageGenre = imageGenre;
}

public String getIdMood() {
return idMood;
}

public void setIdMood(String idMood) {
this.idMood = idMood;
}

}