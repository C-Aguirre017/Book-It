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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.fragment.Mapa;
import proyecto.proyectobookit.utils.AlertDialogMetodos;
import proyecto.proyectobookit.utils.AsyncMetodos;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_ListaMapa extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Pin> ListaPines = null;

    EditText Search;

    public ListViewAdapter_ListaMapa(Context context, EditText Search) {
        mContext = context;
        this.Search = Search;
        this.ListaPines = new ArrayList<Pin>(Mapa.Tabla_Pines.values());
        inflater = LayoutInflater.from(mContext);
    }

    public void actualizarPines_ListaMapa(String url){
        ListaPines.clear();
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            } else {
                new AsyncTask_GetMarker().execute(url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarListaMapa() {
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
            view = inflater.inflate(R.layout.listview_modolista, null);
            holder.NombreRamo = (TextView) view.findViewById(R.id.listview_modolista_NombreRamo);
            holder.Fecha = (TextView) view.findViewById(R.id.listview_modolista_FechayHora);
            holder.Pago = (TextView) view.findViewById(R.id.listview_modolista_Pago);
            holder.Imagen = (ImageView) view.findViewById(R.id.listview_modolista_icono);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Colocar el valor
        holder.Aux_Pin = ListaPines.get(position);

        if(holder.Aux_Pin !=null) {
            holder.NombreRamo.setText(holder.Aux_Pin.getRamo_Pin().getSigla() + " " + holder.Aux_Pin.getRamo_Pin().getNombre());
            holder.Fecha.setText(holder.Aux_Pin.getHora());
            holder.Pago.setText(holder.Aux_Pin.getPrecio());

            BuscarIcono(holder.Imagen,holder.Aux_Pin);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialogMetodos.crearPinApplication(holder.Aux_Pin, mContext);
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


    // AsyncTasks Get
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

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            List<Pin> auxLista = AsyncMetodos.convertirJSON_Pin(result, mContext);
            for (Pin auxPin: auxLista) {
                ListaPines.add(auxPin);
                Mapa.Tabla_Pines.put(auxPin.getId_pin(),auxPin);
                notifyDataSetChanged();
            }
        }

    }

}

