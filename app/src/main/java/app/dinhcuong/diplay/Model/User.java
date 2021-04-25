package app.dinhcuong.diplay.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("id_user")
@Expose
private String idUser;
@SerializedName("name_user")
@Expose
private String nameUser;
@SerializedName("email_user")
@Expose
private String emailUser;
@SerializedName("password_user")
@Expose
private String passwordUser;

public String getIdUser() {
return idUser;
}

public void setIdUser(String idUser) {
this.idUser = idUser;
}

public String getNameUser() {
return nameUser;
}

public void setNameUser(String nameUser) {
this.nameUser = nameUser;
}

public String getEmailUser() {
return emailUser;
}

public void setEmailUser(String emailUser) {
this.emailUser = emailUser;
}

public String getPasswordUser() {
return passwordUser;
}

public void setPasswordUser(String passwordUser) {
this.passwordUser = passwordUser;
}

}