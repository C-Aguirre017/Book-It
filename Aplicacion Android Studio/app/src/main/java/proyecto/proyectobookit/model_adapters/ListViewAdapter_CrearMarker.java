package proyecto.proyectobookit.model_adapters;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Ramo;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class ListViewAdapter_CrearMarker extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Ramo> ListaPalabras = null;

    private List<AsyncTask> Lista_AssyncTask = new ArrayList<AsyncTask>();

    EditText Search;
    AsyncTask Getter;
    public Ramo Elegido = null;

    public ListViewAdapter_CrearMarker(Context context, EditText Search) {
        mContext = context;
        this.Search = Search;
        this.ListaPalabras = new ArrayList<Ramo>();
        inflater = LayoutInflater.from(mContext);
        Getter =  new GetRamos();
    }

    public List<AsyncTask> getLista_AssyncTask() {
        return Lista_AssyncTask;
    }

    public class ViewHolder {
        TextView  Cambiar;
        Ramo curso = null;
    }

    @Override
    public int getCount() {
        return ListaPalabras.size();
    }

    @Override
    public Ramo getItem(int position) {
        return ListaPalabras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.Cambiar = (TextView) view.findViewById(R.id.listview_ramo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Colocar el valor
        holder.curso = ListaPalabras.get(position);

        if(holder.curso !=null) {
            holder.Cambiar.setText(holder.curso.getSigla() + " " + holder.curso.getNombre());


            // Listen for ListView Item Click
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Send single item click data to SingleItemView Class
                    Elegido = holder.curso;
                    Search.setText(holder.curso.getSigla() + " " + holder.curso.getNombre());
                }
            });
        }

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        // Resetear Valores
        ListaPalabras.clear();

        if (charText.length() == 0) {
            notifyDataSetChanged();
        } else {
            String Url = Configuracion.URLSERVIDOR + "/courses/search/";
            charText = charText.substring(0, 1).toUpperCase() + charText.substring(1);
            try {
                Url += URLEncoder.encode(charText, "UTF-8") + ".json";
                if (Build.VERSION.SDK_INT >= 11) {
                    new GetRamos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Url);
                } else {
                    new GetRamos().execute(Url);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private class GetRamos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return ConsultaHTTP.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                ListaPalabras.clear();
                result = "{ \"ramos\":" + result + "}";
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray articles = json.getJSONArray("ramos");
                    for (int i = 0; i < articles.length(); i++) {
                        String NombreRamo = "", Sigla = "", unidad_academica = "", id_ramo = "";
                        NombreRamo = articles.getJSONObject(i).getString("name");
                        Sigla = articles.getJSONObject(i).getString("initials");
                        id_ramo = articles.getJSONObject(i).getString("id");
                        if (!id_ramo.equals("") && !NombreRamo.equals("") && !Sigla.equals("")) {
                            ListaPalabras.add(new Ramo(NombreRamo, Sigla, unidad_academica, id_ramo));
                            notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    Log.d("Informacion", "Excepcion al ver ramos: " + e.getMessage());
                }
            }
        }
    }

    public Ramo getElegido() {
        return Elegido;
    }
}

