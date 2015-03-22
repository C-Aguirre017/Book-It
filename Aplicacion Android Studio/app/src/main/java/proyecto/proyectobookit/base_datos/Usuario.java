package proyecto.proyectobookit.base_datos;

import com.facebook.Session;
import com.facebook.model.GraphUser;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Carlos on 22-02-2015.
 */
public class Usuario {

    private static Usuario usuarioActual = null;

    private String nombre, email, carrera, role, token, telefono;
    private String id_usuario;
    private String fbUid;
    private Session fbSession;
    private GraphUser gUser;

    // METODOS ESTATICOS

    public static void cargarDatos(Usuario u, String datos) {
        try {
            JSONObject jo = new JSONObject(datos);
            u.setId_usuario(jo.getString("id"));
            u.setEmail(jo.getString("email"));
            u.setNombre(jo.getString("nombre"));
            u.setCarrera(jo.getString("carrera"));
            u.setRole(jo.getString("role"));
            u.setTelefono(jo.getString("telefono"));
        } catch (Exception e) {

        }
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
        if (usuarioActual == null) { usuarioActual = new Usuario(); }
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
        this.setFbUid(gUser.getId());
    }
}
