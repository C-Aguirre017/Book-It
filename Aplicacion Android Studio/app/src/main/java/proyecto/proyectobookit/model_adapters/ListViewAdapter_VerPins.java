package proyecto.proyectobookit.model_adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_VerPins extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Pin> ListaPines = null;

    public ListViewAdapter_VerPins(Context context) {
        mContext = context;
        this.ListaPines = new ArrayList<Pin>();
        inflater = LayoutInflater.from(mContext);
    }

    public void ColocarPines(String Url){
        try {
            ListaPines.clear();
            if (Build.VERSION.SDK_INT >= 11) {
                new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Url);
            } else {
                new AsyncTask_GetMarker().execute(Url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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
            view = inflater.inflate(R.layout.listview_mispins, null);
            holder.NombreRamo = (TextView) view.findViewById(R.id.listview_mis_pins_NombreRamo);
            holder.Fecha = (TextView) view.findViewById(R.id.listview_mis_pins_FechayHora);
            holder.Pago = (TextView) view.findViewById(R.id.listview_mis_pins_Pago);
            holder.Imagen = (ImageView) view.findViewById(R.id.listview_mis_pins_icono);
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
                    try {Aux.getUsuario_Pin().setId_usuario(articles.getJSONObject(i).getString("user_id")); } catch (Exception e) {}
                    String Date_Aux="";
                    try {Date_Aux= articles.getJSONObject(i).getString("publication"); } catch (Exception e) {}
                    Aux.setPublicacion(Date_Aux.replace("T"," "));
                    try {Aux.setRealizacion(articles.getJSONObject(i).getString("realization"));} catch (Exception e) {}
                    try {Aux.setDuracion(articles.getJSONObject(i).getString("duration"));} catch (Exception e) {}
                    //Cambiar nombre a futuro de titulo
                    try {Aux.getRamo_Pin().setId_ramo(articles.getJSONObject(i).getString("title"));}catch(Exception e){}
                    try {Aux.setDescripcion(articles.getJSONObject(i).getString("description"));} catch (Exception e) {}
                    try {Aux.setPrecio(articles.getJSONObject(i).getString("price"));} catch (Exception e) {}
                    try {Aux.setTipo_ayuda(articles.getJSONObject(i).getString("help_type"));} catch (Exception e) {}
                    try {Aux.setCampus(articles.getJSONObject(i).getString("faculty"));} catch (Exception e) {}
                    try {Aux.setLatitude(Double.parseDouble(articles.getJSONObject(i).getString("latitude")));} catch (Exception e) {}
                    try {Aux.setLongitude(Double.parseDouble(articles.getJSONObject(i).getString("longitude"))); } catch (Exception e) {}

                    //Obtener Ramos
                    try {
                        String ramos = articles.getJSONObject(i).getString("course");
                        ramos = "{ \"ramos\":[" + ramos + "]}";
                        try {
                            JSONObject json_ramos = new JSONObject(ramos);
                            JSONArray articles_ramos = json_ramos.getJSONArray("ramos");
                            //Completar Pin
                            try {Aux.getRamo_Pin().setNombre(articles_ramos.getJSONObject(0).getString("name"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setSigla(articles_ramos.getJSONObject(0).getString("initials"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setUnidad_Academica(articles_ramos.getJSONObject(0).getString("branch"));                                } catch (Exception e) {                               }

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
            progressDialog.dismiss();
            notifyDataSetChanged();
        }



    }



    /*

    ELIMINAR PIN

    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    builder.setTitle(holder.Aux_Pin.getRamo_Pin().getSigla() + " " + holder.Aux_Pin.getRamo_Pin().getNombre())
            .setMessage(M_Utiles.CrearMensaje(holder.Aux_Pin))
            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {

            String Url = Configuracion.URLSERVIDOR + "/pins/" + holder.Aux_Pin.getId_pin();

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new AsyncTask_Eliminar(Mi_Usuario).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Url);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new AsyncTask_Eliminar(Mi_Usuario).execute(Url);
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

    private class AsyncTask_Eliminar extends AsyncTask<String, Void, Boolean>{

        private Usuario usuario;
       // private ProgressDialog progressDialog;

        public AsyncTask_Eliminar(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog = ProgressDialog.show(mContext, "Eliminando ...", "Espere porfavor", true, false);
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
                URL url = new URL(url_entrante+  "?user_id=" + URLEncoder.encode(usuario.getId_usuario(), "UTF-8") +"&user_token=" + URLEncoder.encode(usuario.getToken(), "UTF-8"));
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded" );
                httpCon.setRequestMethod("DELETE");
                httpCon.connect();
                httpCon.getInputStream();
                //progressDialog.dismiss();
                return true;

            } catch (Exception e) {

                e.printStackTrace();
                //progressDialog.dismiss();
                return false;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }

            }

        }
    }

    */

}

