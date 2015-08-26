package proyecto.proyectobookit.base_datos;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by carlos on 26-08-15.
 */
public class Universidad {

    private String id, nombre;

    public static void cargarDatos(Universidad auxUniversidad, String datos) {
        try {
            Log.d("Info Universidad:", datos);
            JSONObject jo = new JSONObject(datos);

            try {auxUniversidad.setId(jo.getString("id"));} catch (Exception e) {}
            try {auxUniversidad.setNombre(jo.getString("name"));} catch (Exception e) {}

        } catch (Exception e) {
            Log.d(e.toString(),"");
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
