package carranza.com.uattendance;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Francisco on 5/9/2015.
 */
public class UserModel {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String dni;
    private String password;
    private String photo;
    private String rol;

    private static UserModel user;

    public UserModel(String data) throws JSONException {
        JSONObject attributes = new JSONObject(data);

        id = attributes.getInt("id");
        name = attributes.getString("name");
        surname = attributes.getString("surname");
        email = attributes.getString("email");
        dni = attributes.getString("dni");
        password = attributes.getString("password");
        photo = attributes.getString("photo");
        rol = attributes.getString("rol");
    }

    public static UserModel getUser() {
        return user;
    }

    public static void setUser(UserModel user) {
        UserModel.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
