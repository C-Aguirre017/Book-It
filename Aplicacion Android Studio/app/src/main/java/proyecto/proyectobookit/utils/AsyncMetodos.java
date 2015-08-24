package proyecto.proyectobookit.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Ramo;
import proyecto.proyectobookit.base_datos.Solicitud;
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

    public static void mostrarError(String result,Context mContext){
        result = "{ \"error\":" + result +   "}" ;
        try {
            JSONObject json = new JSONObject(result);
            JSONArray articles = json.getJSONArray("error");
            Toast.makeText(mContext,articles.getJSONObject(0).getString("errors"), Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }

    }

    public static List<Solicitud> convertirJSON_Application(String result, Context mContext) {

        List<Solicitud> tablaAuxiliar = new ArrayList<Solicitud>();
        result = "{ \"application\":" + result +   "}" ;
        try {
            JSONObject json = new JSONObject(result);
            JSONArray articles = json.getJSONArray("application");

            for (int i = 0; i < articles.length(); i++) {

                Solicitud Aux = new Solicitud();
                Solicitud.cargarDatos(Aux,articles.getJSONObject(i).toString());

                //Usuarios
                try {
                    String usuarios = articles.getJSONObject(i).getString("user");
                    usuarios = "{ \"usuarios\":[" + usuarios + "]}";
                    JSONObject json_usuarios = new JSONObject(usuarios);
                    JSONArray articles_usuarios = json_usuarios.getJSONArray("usuarios");
                    Usuario.cargarDatos(Aux.getUsuario(),articles_usuarios.getJSONObject(0).toString());
                }
                catch (Exception e){
                    Toast.makeText(mContext, "Error al cargar el Usuario en la SOlicitud", Toast.LENGTH_LONG).show();
                }
                tablaAuxiliar.add(Aux);
            }
        } catch (Exception e) {
            Toast.makeText(mContext,"Error al crear las Solicitudes en onPostExecute_GetMarker()", Toast.LENGTH_LONG).show();
        }

        return tablaAuxiliar;
    }
}
