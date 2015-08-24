package proyecto.proyectobookit.model_adapters;

import android.app.Application;
import android.app.FragmentTransaction;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.Solicitudes;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Solicitud;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.fragment.Get_Perfil;
import proyecto.proyectobookit.utils.AlertDialogMetodos;
import proyecto.proyectobookit.utils.AsyncMetodos;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_Solicitudes extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Solicitud> listaSolicitudes = null;

    public ListViewAdapter_Solicitudes(Context context) {
        mContext = context;
        this.listaSolicitudes = new ArrayList<Solicitud>();
        inflater = LayoutInflater.from(mContext);
    }

    public void actualizarSolicitudes(String Url){
        try {
            listaSolicitudes.clear();
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
        Solicitud auxSolicitud = null;
        CircleImageView foto;
        TextView  nombre;
        TextView biografia;
    }

    @Override
    public int getCount() {
        return listaSolicitudes.size();
    }

    @Override
    public Solicitud getItem(int position) {
        return listaSolicitudes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_solicitudes, null);
            holder.foto = (CircleImageView) view.findViewById(R.id.listview_solicitud_profile_image);
            holder.nombre = (TextView) view.findViewById(R.id.listview_solicitud_nombre);
            holder.biografia= (TextView) view.findViewById(R.id.listview_solicitud_biografia);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Colocar el valor
        holder.auxSolicitud = listaSolicitudes.get(position);

        if(holder.auxSolicitud !=null) {

            holder.nombre.setText(holder.auxSolicitud.getUsuario().getNombre());
            holder.biografia.setText(holder.auxSolicitud.getUsuario().getBiografia());

            setFbImage(holder.auxSolicitud.getUsuario().getFbUid(),holder.foto);

            // Listen for ListView Item Click
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    //Intent i = new Intent(mContext, Solicitudes.class);
                    try {
                        AlertDialogMetodos.enviarWhatsappMail(holder.auxSolicitud.getUsuario(),mContext);
                        //mContext.startActivity(i);
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "Ocurrio un Error \n" + ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }

    //AsyncTasks Get
    private class AsyncTask_GetMarker extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Obteniendo Solicitudes", "Espere porfavor ...", true, false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return ConsultaHTTP.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            List<Solicitud> auxLista = AsyncMetodos.convertirJSON_Application(result, mContext);
            for (Solicitud auxApplication: auxLista) {
                listaSolicitudes.add(auxApplication);
                notifyDataSetChanged();
            }
        }

    }

    private void setFbImage(String fbid, CircleImageView picture){
        Log.d("Informacion", "http://graph.facebook.com/" + fbid + "/picture?type=square");
        Picasso.with(mContext)
                .load("https://graph.facebook.com/" + fbid + "/picture?type=square")
                .resize(100, 100)
                .centerCrop()
                .into(picture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Informacion", "Se logro poner imagen de fb");
                    }

                    @Override
                    public void onError() {
                        Log.d("Informacion", "No se pudo poner imagen de facebook");
                    }
                });
    }

}

