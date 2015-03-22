package proyecto.proyectobookit.model_adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.MetodosUtiles;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.fragment.Mapa;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_ModoLista extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Pin> ListaPines = null;

    private MetodosUtiles M_Utiles = new MetodosUtiles();

    EditText Search;

    public ListViewAdapter_ModoLista(Context context, EditText Search) {
        mContext = context;
        this.Search = Search;
        this.ListaPines = new ArrayList<Pin>(Mapa.Tabla_Pines.values());
        inflater = LayoutInflater.from(mContext);
    }

    public void Actualizar_ColocarPines(String Url){
        ListaPines.clear();
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Url);
            } else {
                new AsyncTask_GetMarker().execute(Url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Colocar() {
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView  NombreRamo;
        TextView Fecha;
        TextView Pago;
        ImageView Imagen;
        Pin Aux_Pin = null;
    }

    @Override
    public int getCount() {
        return ListaPines.size();
    }

    @Override
    public Pin getItem(int position) {
        return ListaPines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_pin, null);
            holder.NombreRamo = (TextView) view.findViewById(R.id.listview_pin_NombreRamo);
            holder.Fecha = (TextView) view.findViewById(R.id.listview_pin_FechayHora);
            holder.Pago = (TextView) view.findViewById(R.id.listview_pin_Pago);
            holder.Imagen = (ImageView) view.findViewById(R.id.listview_pin_icono);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Colocar el valor
        holder.Aux_Pin =ListaPines.get(position);

        if(holder.Aux_Pin !=null) {
            holder.NombreRamo.setText(holder.Aux_Pin.getRamo_Pin().getSigla() + " " + holder.Aux_Pin.getRamo_Pin().getNombre());
            holder.Fecha.setText(holder.Aux_Pin.getHora());
            holder.Pago.setText(holder.Aux_Pin.getPrecio());

            BuscarIcono(holder.Imagen,holder.Aux_Pin);
            // Listen for ListView Item Click
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    M_Utiles.setContext(mContext);
                    M_Utiles.CrearAlertDialog(holder.Aux_Pin);
                }
            });
        }

        return view;
    }

    private void BuscarIcono(ImageView Imagen, Pin Aux) {
        String sigla = Aux.getRamo_Pin().getSigla();
        if(Imagen!= null && !sigla.equals("")){
            if (esInicioSigla(sigla, new String[]{"ACT"})) {
                Imagen.setImageResource(R.drawable.ic_actuacion);
            } else if (esInicioSigla(sigla, new String[]{"AGL"})) { // Agronomia
                Imagen.setImageResource(R.drawable.ic_agronomia);
            } else if (esInicioSigla(sigla, new String[]{"AQH"})) { // Arquitectura
                Imagen.setImageResource(R.drawable.ic_arquitectura);
            } else if (esInicioSigla(sigla, new String[]{"ARQ", "ARO"})) { // Arte
                Imagen.setImageResource(R.drawable.ic_arte);
            } else if (esInicioSigla(sigla, new String[]{"BIO"})) { // Biologia
                Imagen.setImageResource(R.drawable.ic_biologia);
            } else if (esInicioSigla(sigla, new String[]{"DPT"})) { // Deportes
                Imagen.setImageResource(R.drawable.ic_deporte);
            } else if (esInicioSigla(sigla, new String[]{"DEC"})) { // Derecho
                Imagen.setImageResource(R.drawable.ic_derecho);
            } else if (esInicioSigla(sigla, new String[]{"DEE"})) { // Economia
                Imagen.setImageResource(R.drawable.ic_economia);
            } else if (esInicioSigla(sigla, new String[]{"EDU"})) { // Educacion
                Imagen.setImageResource(R.drawable.ic_educacion);
            } else if (esInicioSigla(sigla, new String[]{"ENA"})) { // Enfermeria
                Imagen.setImageResource(R.drawable.ic_enfermeria);
            } else if (esInicioSigla(sigla, new String[]{"FIS"})) { // Fisica
                Imagen.setImageResource(R.drawable.ic_fisica);
            } else if (esInicioSigla(sigla, new String[]{"GEO"})) { // Geografia
                Imagen.setImageResource(R.drawable.ic_geografia);
            }else if (esInicioSigla(sigla, new String[]{"MAT"})) { // Matematicas
                Imagen.setImageResource(R.drawable.ic_matematica);
            } else if (esInicioSigla(sigla, new String[]{"MUC"})) { // Musica
                Imagen.setImageResource(R.drawable.ic_musica);
            } else if (esInicioSigla(sigla, new String[]{"ODO"})) { // Odontologia
                Imagen.setImageResource(R.drawable.ic_odontologia);
            } else if (esInicioSigla(sigla, new String[]{"QI"})) { // Quimica
                Imagen.setImageResource(R.drawable.ic_quimica);
            } else if (esInicioSigla(sigla, new String[]{"CCP"})) { // Medicina
                Imagen.setImageResource(R.drawable.ic_salud);
            } else if (esInicioSigla(sigla, new String[]{"FIL"})) { // Teologia
                Imagen.setImageResource(R.drawable.ic_teologia);
            } else if (esInicioSigla(sigla, new String[]{"IC", "IE", "IM"})) { // Ingenieria
                Imagen.setImageResource(R.drawable.ic_ingenieria);
            }  else {
                Imagen.setImageResource(R.drawable.ic_logo_chico);
            }
        }
    }

    private boolean esInicioSigla(String sigla, String[] isiglas) {
        for (String is: isiglas) {
            if(sigla.startsWith(is)) {
                return true;
            }
        }
        return false;
    }

    // Filter Class
    public void filter(String charText,String Url) {
        charText = charText.toLowerCase(Locale.getDefault());
        //Resetear Valores
        //ListaPines.clear();

        if (charText.length() == 0) {

        }
        else
        {

        }

    }

    //AsyncTasks Get
    private class AsyncTask_GetMarker extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Obteniendo Información...", "Espere porfavor", true, false);
        }


        @Override
        protected String doInBackground(String... urls) {
            return ConsultaHTTP.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {

            result = "{ \"pins\":" + result +   "}" ;
            try {
                JSONObject json = new JSONObject(result);
                JSONArray articles = json.getJSONArray("pins");

                for (int i = 0; i < articles.length(); i++) {
                    //Crear Pin
                    Pin Aux = new Pin();
                    //Encontramos los valores
                    try {Aux.setId_pin(articles.getJSONObject(i).getString("id")); } catch (Exception e) {}
                    try {Aux.getUsuario_Pin().setId_usuario(articles.getJSONObject(i).getString("usuario_id")); } catch (Exception e) {}
                    String Date_Aux="";
                    try {Date_Aux= articles.getJSONObject(i).getString("publicacion"); } catch (Exception e) {}
                    Aux.setHora(Date_Aux.replace("T"," "));
                    try {Aux.setRealizacion(articles.getJSONObject(i).getString("realizacion"));} catch (Exception e) {}
                    try {Aux.setDuracion(articles.getJSONObject(i).getString("duracion"));} catch (Exception e) {}
                    //Cambiar nombre a futuro de titulo
                    try {Aux.getRamo_Pin().setId_ramo(articles.getJSONObject(i).getString("titulo"));}catch(Exception e){}
                    try {Aux.setDescripcion(articles.getJSONObject(i).getString("descripcion"));} catch (Exception e) {}
                    try {Aux.setPrecio(articles.getJSONObject(i).getString("precio"));} catch (Exception e) {}
                    try {Aux.setTipo_ayuda(articles.getJSONObject(i).getString("tipo_ayuda"));} catch (Exception e) {}
                    try {Aux.setCampus(articles.getJSONObject(i).getString("facultad"));} catch (Exception e) {}
                    try {Aux.setLatitude(Double.parseDouble(articles.getJSONObject(i).getString("latitude")));} catch (Exception e) {}
                    try {Aux.setLongitude(Double.parseDouble(articles.getJSONObject(i).getString("longitude"))); } catch (Exception e) {}

                    //Obtener Usuarios

                    try {
                        String usuarios = articles.getJSONObject(i).getString("usuario");
                        usuarios = "{ \"usuarios\":[" + usuarios + "]}";
                        JSONObject json_usuarios = new JSONObject(usuarios);
                        JSONArray articles_usuarios = json_usuarios.getJSONArray("usuarios");

                        try {Aux.getUsuario_Pin().setId_usuario(articles_usuarios.getJSONObject(0).getString("id")); } catch (Exception e) {  }
                        try {Aux.getUsuario_Pin().setEmail(articles_usuarios.getJSONObject(0).getString("email")); } catch (Exception e) {  }
                        try {Aux.getUsuario_Pin().setNombre(articles_usuarios.getJSONObject(0).getString("nombre"));      } catch (Exception e){       }
                        try {Aux.getUsuario_Pin().setCarrera(articles_usuarios.getJSONObject(0).getString("carrera"));  } catch (Exception e) {    }
                        try {Aux.getUsuario_Pin().setRole(articles_usuarios.getJSONObject(0).getString("role"));   } catch (Exception e) {  }
                        try {Aux.getUsuario_Pin().setTelefono(articles_usuarios.getJSONObject(0).getString("telefono"));   } catch (Exception e) {  }
                        Aux.getUsuario_Pin().setTelefono("+56994405326");
                    }
                    catch (Exception e){
                        Toast.makeText(mContext, "Error al crear el Pin en Usuarios on GetMarker()", Toast.LENGTH_LONG).show();
                    }

                    //Obtener Ramos
                    try {
                        String ramos = articles.getJSONObject(i).getString("ramo");
                        ramos = "{ \"ramos\":[" + ramos + "]}";
                        try {
                            JSONObject json_ramos = new JSONObject(ramos);
                            JSONArray articles_ramos = json_ramos.getJSONArray("ramos");
                            //Completar Pin
                            try {Aux.getRamo_Pin().setNombre(articles_ramos.getJSONObject(0).getString("nombre"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setSigla(articles_ramos.getJSONObject(0).getString("sigla"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setUnidad_Academica(articles_ramos.getJSONObject(0).getString("rama"));                                } catch (Exception e) {                               }

                            ListaPines.add(Aux);
                            Mapa.Tabla_Pines.put(Aux.getId_pin(),Aux);
                        } catch (Exception e) {
                            // TODO: handle exception

                        }
                    }catch (Exception e){
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            progressDialog.dismiss();
            notifyDataSetChanged();
        }

    }

}

