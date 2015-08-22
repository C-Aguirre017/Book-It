package proyecto.proyectobookit.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Ramo;
import proyecto.proyectobookit.base_datos.Usuario;

/**
 * Created by carlos on 22-08-15.
 */
public class AsyncMetodos {

    public static List<Pin> convertirJSON_Pin(String result,Context mContext){

        List<Pin> tablaAuxiliar = new ArrayList<Pin>();
        result = "{ \"pins\":" + result +   "}" ;
        try {
            JSONObject json = new JSONObject(result);
            JSONArray articles = json.getJSONArray("pins");

            for (int i = 0; i < articles.length(); i++) {

                Pin Aux = new Pin();
                Pin.cargarDatos(articles.getJSONObject(i).toString(), Aux);

                //Usuarios
                try {
                    String usuarios = articles.getJSONObject(i).getString("user");
                    usuarios = "{ \"usuarios\":[" + usuarios + "]}";
                    JSONObject json_usuarios = new JSONObject(usuarios);
                    JSONArray articles_usuarios = json_usuarios.getJSONArray("usuarios");

                    Usuario.cargarDatos(Aux.getUsuario_Pin(),articles_usuarios.getJSONObject(0).toString());
                }
                catch (Exception e){
                    Toast.makeText(mContext, "Error al crear el Pin en Usuarios on GetMarker()", Toast.LENGTH_LONG).show();
                }

                //Ramos
                try {
                    String ramos = articles.getJSONObject(i).getString("course");
                    ramos = "{ \"ramos\":[" + ramos + "]}";
                    JSONObject json_ramos = new JSONObject(ramos);
                    JSONArray articles_ramos = json_ramos.getJSONArray("ramos");

                    Ramo.cargarDatos(Aux.getRamo_Pin(),articles_ramos.getJSONObject(0).toString());

                }catch (Exception e){
                    Toast.makeText(mContext, "Error al crear el Pin en Ramos on GetMarker()", Toast.LENGTH_LONG).show();
                }


                tablaAuxiliar.add(Aux);
            }
        } catch (Exception e) {
            Toast.makeText(mContext,"Error al crear el Pin en onPostExecute_GetMarker()", Toast.LENGTH_LONG).show();
        }
        return tablaAuxiliar;
    }
}
