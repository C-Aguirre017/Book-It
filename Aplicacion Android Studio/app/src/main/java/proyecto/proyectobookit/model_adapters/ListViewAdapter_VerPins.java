package proyecto.proyectobookit.model_adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.MetodosUtiles;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Usuario;

public class ListViewAdapter_VerPins extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Pin> ListaPines = null;

    private List<AsyncTask> Lista_AssyncTask = new ArrayList<AsyncTask>();
    private MetodosUtiles M_Utiles = new MetodosUtiles();

    private Usuario Mi_Usuario = new Usuario();

    EditText Search;

    public ListViewAdapter_VerPins(Context context, EditText Search,Usuario usuario) {
        mContext = context;
        this.Search =Search;
        this.ListaPines = new ArrayList<Pin>();
        this.Mi_Usuario =usuario;
        inflater = LayoutInflater.from(mContext);
    }

    public void ColocarPines(String Url){
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

    public List<AsyncTask> getLista_AssyncTask() {
        return Lista_AssyncTask;
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
            holder.Fecha.setText(holder.Aux_Pin.getPublicacion());
            holder.Pago.setText(holder.Aux_Pin.getPrecio());

            BuscarIcono(holder.Imagen,holder.Aux_Pin);
            // Listen for ListView Item Click
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Send single item click data to SingleItemView Class
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(holder.Aux_Pin.getRamo_Pin().getSigla() + " " + holder.Aux_Pin.getRamo_Pin().getNombre())
                            .setMessage(M_Utiles.CrearMensaje(holder.Aux_Pin))
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String Url = "http://pinit-api.herokuapp.com/pins/" + holder.Aux_Pin.getId_pin();

                                    if (Build.VERSION.SDK_INT >= 11) {
                                        //--post GB use serial executor by default --
                                        new AsyncTask_Eliminar(Mi_Usuario.getEmail(),Mi_Usuario.getToken()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Url);
                                    } else {
                                        //--GB uses ThreadPoolExecutor by default--
                                        new AsyncTask_Eliminar(Mi_Usuario.getEmail(),Mi_Usuario.getToken()).execute(Url);
                                    }
                                    ListaPines.remove(holder.Aux_Pin);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

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
        ListaPines.clear();

        if (charText.length() == 0) {

        }
        else
        {

        }

    }

    //AsyncTasks Get
    private class AsyncTask_GetMarker extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
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
                    Aux.setPublicacion(Date_Aux.replace("T"," "));
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
                        } catch (Exception e) {
                            // TODO: handle exception

                        }
                    }catch (Exception e){
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            notifyDataSetChanged();
        }

        private String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }



    }


    private class AsyncTask_Eliminar extends AsyncTask<String,Void,Boolean>{

        private String Email,Token;
        private ProgressDialog progressDialog;

        public AsyncTask_Eliminar(String usuario,String token) {
            this.Email=usuario;
            this.Token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Eliminando ...", "Espere porfavor", true, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return deleteData_Pins(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }

        private boolean deleteData_Pins(String url_entrante ) {

            HttpURLConnection connection = null;
            String Sabe = url_entrante;
            try {
                URL url = new URL(url_entrante+  "?user_email=" + URLEncoder.encode(Email, "UTF-8") +"&user_token=" + URLEncoder.encode(Token, "UTF-8"));
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded" );
                httpCon.setRequestMethod("DELETE");
                httpCon.connect();
                httpCon.getInputStream();
                progressDialog.dismiss();
                return true;

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
                return false;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }

            }

        }
    }
}

