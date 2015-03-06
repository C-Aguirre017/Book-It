package proyecto.proyectobookit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
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

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Ramo> ListaPalabras = null;

    private List<AsyncTask> Lista_AssyncTask = new ArrayList<AsyncTask>();

    EditText Search;
    AsyncTask Getter;
    public Ramo Elegido = null;

    public ListViewAdapter(Context context, EditText Search) {
        mContext = context;
        this.Search =Search;
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
        holder.curso =ListaPalabras.get(position);

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
        //Resetear Valores
        ListaPalabras.clear();

        if (charText.length() == 0) {
            notifyDataSetChanged();
        }
        else
        {
            String Url = "http://pinit-api.herokuapp.com/ramos/buscar/";
            charText= charText.substring(0,1).toUpperCase() + charText.substring(1);
            try {
                Url += URLEncoder.encode(charText, "UTF-8") + ".json";
                if (Build.VERSION.SDK_INT >= 11) {
                    new GetRamos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Url);
                } else {
                    new GetRamos().execute(Url);
                }
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    public String GET(String url){
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
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class GetRamos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            if(result !=null) {
                ListaPalabras.clear();
                result = "{ \"ramos\":" + result + "}";
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray articles = json.getJSONArray("ramos");
                    for (int i = 0; i < articles.length(); i++) {
                        String NombreRamo = "", Sigla = "",unidad_academica="",id_ramo="";
                        try { NombreRamo = articles.getJSONObject(i).getString("nombre");} catch (Exception e) {       }
                        try {Sigla = articles.getJSONObject(i).getString("sigla");} catch (Exception e) {  }
                        try {unidad_academica = articles.getJSONObject(i).getString("rama");} catch (Exception e) {  }
                        try {id_ramo = articles.getJSONObject(i).getString("id");} catch (Exception e) {  }
                        if(!id_ramo.equals("")&& !NombreRamo.equals("") && !Sigla.equals(""))
                            ListaPalabras.add(new Ramo(NombreRamo,Sigla,unidad_academica,id_ramo));
                    }
                    notifyDataSetChanged();
                } catch (Exception e) {
                    // TODO: handle exception

                }
            }
        }
    }

    public Ramo getElegido() {
        return Elegido;
    }
}

