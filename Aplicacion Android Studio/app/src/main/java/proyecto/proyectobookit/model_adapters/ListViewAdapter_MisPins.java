package proyecto.proyectobookit.model_adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.Solicitudes;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.utils.AsyncMetodos;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_MisPins extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Pin> listaPines = null;

    public ListViewAdapter_MisPins(Context context) {
        mContext = context;
        this.listaPines = new ArrayList<Pin>();
        inflater = LayoutInflater.from(mContext);
    }

    public void actualizarPines(String Url){
        try {
            listaPines.clear();
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
        TextView  nombre;
        TextView fecha;
        TextView precio;
        TextView solicitudes;
        ImageView foto;
        Pin auxPin = null;
    }

    @Override
    public int getCount() {
        return listaPines.size();
    }

    @Override
    public Pin getItem(int position) {
        return listaPines.get(position);
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
            holder.nombre = (TextView) view.findViewById(R.id.listview_mis_pins_nombre);
            holder.fecha = (TextView) view.findViewById(R.id.listview_mis_pins_fecha);
            holder.precio = (TextView) view.findViewById(R.id.listview_mis_pins_precio);
            holder.foto = (ImageView) view.findViewById(R.id.listview_mis_pins_icono);
            holder.solicitudes = (TextView) view.findViewById(R.id.listview_mis_pins_cantsolicitudes);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Colocar el valor
        holder.auxPin =listaPines.get(position);

        if(holder.auxPin !=null) {

            holder.nombre.setText(holder.auxPin.getRamo_Pin().getSigla()  + " " + holder.auxPin.getRamo_Pin().getNombre());
            holder.fecha.setText(holder.auxPin.getHora());
            holder.solicitudes.setText("" + holder.auxPin.getCantSolicitudes());
            holder.precio.setText("$" + holder.auxPin.getPrecio());

            buscarIcono(holder.foto, holder.auxPin);

            // Listen for ListView Item Click
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(mContext, Solicitudes.class);
                    i.putExtra("pin_id",holder.auxPin.getId_pin());
                    try {
                        mContext.startActivity(i);
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "Ocurrio un Error \n" + ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }

    private void buscarIcono(ImageView Imagen, Pin Aux) {
        String sigla = Aux.getRamo_Pin().getSigla();
        if(Imagen!= null && !sigla.equals("")){
            if (esInicioSigla(sigla, Configuracion.SIGLA_ACTUACION)) {
                Imagen.setImageResource(R.drawable.ic_actuacion);
            } else if (esInicioSigla(sigla,Configuracion.SIGLA_AGRONOMIA)) { // Agronomia
                Imagen.setImageResource(R.drawable.ic_agronomia);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_ARQUITECTURA)) { // Arquitectura
                Imagen.setImageResource(R.drawable.ic_arquitectura);
            } else if (esInicioSigla(sigla,Configuracion.SIGLA_ARTE)) { // Arte
                Imagen.setImageResource(R.drawable.ic_arte);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_BIOLOGIA)) { // Biologia
                Imagen.setImageResource(R.drawable.ic_biologia);
            } else if (esInicioSigla(sigla,Configuracion.SIGLA_DPT)) { // Deportes
                Imagen.setImageResource(R.drawable.ic_deporte);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_DERECHO)) { // Derecho
                Imagen.setImageResource(R.drawable.ic_derecho);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_ECONOMIA)) { // Economia
                Imagen.setImageResource(R.drawable.ic_economia);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_EDUCACION)) { // Educacion
                Imagen.setImageResource(R.drawable.ic_educacion);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_ENFERMERIA)) { // Enfermeria
                Imagen.setImageResource(R.drawable.ic_enfermeria);
            } else if (esInicioSigla(sigla,Configuracion.SIGLA_FISICA)) { // Fisica
                Imagen.setImageResource(R.drawable.ic_fisica);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_GEOGRAFIA)) { // Geografia
                Imagen.setImageResource(R.drawable.ic_geografia);
            }else if (esInicioSigla(sigla, Configuracion.SIGLA_MATEMATICAS)) { // Matematicas
                Imagen.setImageResource(R.drawable.ic_matematica);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_MUSICA)) { // Musica
                Imagen.setImageResource(R.drawable.ic_musica);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_ODONTOLOGIA)) { // Odontologia
                Imagen.setImageResource(R.drawable.ic_odontologia);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_QUIMICA)) { // Quimica
                Imagen.setImageResource(R.drawable.ic_quimica);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_MEDICINA)) { // Medicina
                Imagen.setImageResource(R.drawable.ic_salud);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_TEOLOGIA)) { // Teologia
                Imagen.setImageResource(R.drawable.ic_teologia);
            } else if (esInicioSigla(sigla, Configuracion.SIGLA_INGENIERIA)) { // Ingenieria
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

    //AsyncTasks Get
    private class AsyncTask_GetMarker extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Obteniendo Información", "Espere porfavor ...", true, false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return ConsultaHTTP.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            List<Pin> auxLista = AsyncMetodos.convertirJSON_Pin(result, mContext);
            for (Pin auxPin: auxLista) {
                listaPines.add(auxPin);
                notifyDataSetChanged();
            }
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

