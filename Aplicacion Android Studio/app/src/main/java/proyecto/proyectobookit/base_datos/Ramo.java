package proyecto.proyectobookit.base_datos;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Carlos on 21-02-2015.
 */
public class Ramo {

    private String Nombre,Sigla,Unidad_Academica,Id_ramo;

    public static void cargarDatos(Ramo auxRamo, String datos) {
        try {
            Log.d("Informacion Ramo: ", datos);
            JSONObject jo = new JSONObject(datos);

            try {auxRamo.setId_ramo(jo.getString("id"));} catch (Exception e) {}
            try {auxRamo.setNombre(jo.getString("name"));} catch (Exception e) {}
            try {auxRamo.setSigla(jo.getString("initials"));} catch (Exception e) {}
            try {auxRamo.setUnidad_Academica(jo.getString("branch"));} catch (Exception e) {}

        } catch (Exception e) {
            Log.d(e.toString(),"");
        }
    }


    public Ramo(String titulo,String sigla,String unidad_Academica,String id_ramo){
        Unidad_Academica = unidad_Academica;
        Sigla = sigla;
        Nombre = titulo;
        this.Id_ramo =id_ramo;
    }

    //Getters and Setters
    public String getNombre() {
        return Nombre;
    }

    public String getSigla() {
        return Sigla;
    }

    public String getUnidad_Academica() {
        return Unidad_Academica;
    }

    public String getId_ramo() {
        return Id_ramo;
    }

    public void setNombre(String titulo) {
        Nombre = titulo;
    }

    public void setId_ramo(String id_ramo) {
        Id_ramo = id_ramo;
    }

    public void setSigla(String sigla) {
        Sigla = sigla;
    }

    public void setUnidad_Academica(String unidad_Academica) {
        Unidad_Academica = unidad_Academica;
    }
}
