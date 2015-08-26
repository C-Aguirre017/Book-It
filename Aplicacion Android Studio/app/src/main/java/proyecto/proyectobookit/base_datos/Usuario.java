package proyecto.proyectobookit.base_datos;

import android.util.Log;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.model.GraphUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;

/**
 * Created by Carlos on 22-02-2015.
 */

public class Usuario {

    private static Usuario usuarioActual = null;
    private Universidad universidad = new Universidad();

    private String nombre, email, carrera, role, token, telefono, biografia;
    private String id_usuario;
    private String fbUid;
    private Session fbSession;
    private GraphUser gUser;


    // METODOS ESTATICOS

    public static void cargarDatos(Usuario auxUsuario, String datos) {
        try {
            Log.d("Informacion Usuario: ", datos);
            JSONObject jo = new JSONObject(datos);

            try {auxUsuario.setId_usuario(jo.getString("id"));} catch (Exception e) {}
            try {auxUsuario.setEmail(jo.getString("email"));} catch (Exception e) {}
            try {auxUsuario.setNombre(jo.getString("name"));} catch (Exception e){}
            try {auxUsuario.setCarrera(jo.getString("profession"));} catch (Exception e) {}
            try {auxUsuario.setTelefono(jo.getString("phone"));} catch (Exception e) {}
            try {auxUsuario.setBiografia(jo.getString("biography"));} catch (Exception e) {}
            try {auxUsuario.setFbUid(jo.getString("uid"));} catch (Exception e) {}

            //University
            try {
                String auxUniversity = jo.getString("university");
                auxUniversity = "{ \"university\":[" + auxUniversity+ "]}";
                JSONObject json_universidad = new JSONObject(auxUniversity);
                JSONArray articles_university = json_universidad.getJSONArray("university");

                Universidad.cargarDatos(auxUsuario.getUniversidad(), articles_university.getJSONObject(0).toString());
            }
            catch (Exception e){}

        } catch (Exception e) {
            Log.d(e.toString(),"");
        }
        String Termino = "Si";
    }

    // GETERS SETERS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFbUid() {
        return fbUid;
    }

    public void setFbUid(String fbUid) {
        this.fbUid = fbUid;
    }

    public static Usuario getUsuarioActual() {
        if (usuarioActual == null) {
            usuarioActual = new Usuario(); }
        return usuarioActual;
    }

    public Session getFbSession() {
        return fbSession;
    }

    public void setFbSession(Session fbSession) {
        this.fbSession = fbSession;
    }

    public GraphUser getgUser() {
        return gUser;
    }

    public void setgUser(GraphUser gUser) {
        this.gUser = gUser;
        this.email = (String)gUser.getProperty("email");
        this.nombre = gUser.getFirstName() + " " + gUser.getLastName();
        this.setFbUid(gUser.getId());
    }

    public String getBiografia() {
        return this.biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Universidad getUniversidad() {
        return universidad;
    }

}
