package proyecto.proyectobookit.base_datos;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by carlos on 23-08-15.
 */
public class Solicitud {

    private String id,information,reached;
    private Usuario usuario = new Usuario();;

    public static void cargarDatos(Solicitud auxSolicitud, String datos) {
        try {
            Log.d("Informacion Solicitud: ", datos);
            JSONObject jo = new JSONObject(datos);

            try {auxSolicitud.setId(jo.getString("id"));} catch (Exception e) {}
            try {auxSolicitud.setInformation(jo.getString("information"));} catch (Exception e) {}
            try {auxSolicitud.setReached(jo.getString("reached"));} catch (Exception e) {}

        } catch (Exception e) {
            Log.d(e.toString(),"");
        }
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getReached() {
        return reached;
    }

    public void setReached(String reached) {
        this.reached = reached;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
