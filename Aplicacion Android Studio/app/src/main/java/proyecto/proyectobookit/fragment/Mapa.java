package proyecto.proyectobookit.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.CrearMarker;
import proyecto.proyectobookit.activity.MetodosUtiles;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Usuario;

import static proyecto.proyectobookit.R.layout.fragment_mapa;


public class Mapa extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match

    MapView mapView;
    GoogleMap Mapas;
    LatLng point;

    Context mContext;

    private Usuario Mi_Usuario;

    private View rootView;
    private Menu menu;
    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Buscar = null;

    //Hacer Getters
    private MetodosUtiles M_Utiles = new MetodosUtiles();
    private Hashtable<String,Pin> Tabla_Pines = new Hashtable<String,Pin>();
    private long Tiempo_Start;

    public void setMi_Usuario(Usuario mi_Usuario) {
        Mi_Usuario = mi_Usuario;
    }

    public void setMapa (GoogleMap Aux){
        Mapas = Aux;
    }

    public void setContext(Context Aux){
        mContext = Aux;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //Configurar Mapa
        try {
            rootView = inflater.inflate(fragment_mapa, container, false);
            if(null == Mapas){
                Mapas = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
                Mapas.setMyLocationEnabled(true);
                Mapas.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Mapas.setBuildingsEnabled(true);
                Mapas.setOnMarkerClickListener(this);
                Mapas.setOnMapLongClickListener(this);

                LatLng latLng = new LatLng(-33.498444, -70.611722);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                Mapas.animateCamera(cameraUpdate);

                if(null == Mapas) {
                    Toast.makeText(mContext,"Error creating map",Toast.LENGTH_SHORT).show();
                    onDestroy();
                }

                Actualizar();
            }
        } catch (NullPointerException exception){
                Toast.makeText(mContext,"Ocurrio un Error: ",Toast.LENGTH_SHORT);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        Menu_SearchItem = menu.findItem( R.id.menu_search );
        EditText_Search = (EditText) Menu_SearchItem.getActionView().findViewById( R.id.search );
        Button_Buscar = (Button) Menu_SearchItem.getActionView().findViewById(R.id.buscar);
        Button_Buscar.setVisibility( EditText_Search.getText().length() > 0 ? View.VISIBLE : View.GONE );
        Button_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualizar_Boton();
            }
        });

        EditText_Search.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                Button_Buscar.setVisibility( s.length() > 0 ? View.VISIBLE : View.INVISIBLE );
            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count,
                                           int after )
            {
            }

            @Override
            public void afterTextChanged( Editable s )
            {
                Button_Buscar.setVisibility( s.length() > 0 ? View.VISIBLE : View.GONE );

                String Aux = EditText_Search.getText().toString();
                if(Aux.contains("\n")){
                    Aux = Aux.replace("\n","");
                    EditText_Search.setText(Aux);
                    Actualizar_Boton();
                }
            }
        } );

        ActionBar actions = getActivity().getActionBar();
        actions.setDisplayHomeAsUpEnabled(true);
        actions.setTitle("Mapa");
        actions.setDisplayShowTitleEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
            case android.R.id.home:
                break;
            case R.id.menucentral_refresh:
                Actualizar();
                break;
            case R.id.menucentral_dropdown_campus_san_joaquin:
                LatLng latLng = new LatLng(-33.498444, -70.611722);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                Mapas.animateCamera(cameraUpdate);
                break;
            case R.id.menucentral_dropdown_campus_casa_central:
                LatLng latLng_casacentral = new LatLng(-33.440809, -70.640764);
                CameraUpdate cameraUpdate_casacentral = CameraUpdateFactory.newLatLngZoom(latLng_casacentral, 17);
                Mapas.animateCamera(cameraUpdate_casacentral);
                break;
            case R.id.menucentral_dropdown_campus_lo_contador:
                LatLng latLng_contador = new LatLng(-33.419428, -70.618574);
                CameraUpdate cameraUpdate_contador = CameraUpdateFactory.newLatLngZoom(latLng_contador, 17);
                Mapas.animateCamera(cameraUpdate_contador);
                break;
            case R.id.menucentral_dropdown_campus_oriente:
                LatLng latLng_oriente = new LatLng(-33.446400, -70.593644);
                CameraUpdate cameraUpdate_oriente = CameraUpdateFactory.newLatLngZoom(latLng_oriente, 17);
                Mapas.animateCamera(cameraUpdate_oriente);
                break;
            case R.id.menucentral_dropdown_campus_villarica:
                LatLng latLng_villarica = new LatLng(-38.738457, -72.601795);
                CameraUpdate cameraUpdate_villarica = CameraUpdateFactory.newLatLngZoom(latLng_villarica, 16);
                Mapas.animateCamera(cameraUpdate_villarica);
                break;
        }
        return true;
    }

    private void Actualizar_Boton(){
        String text = EditText_Search.getText().toString().toLowerCase(Locale.getDefault());
        String Url = "http://pinit-api.herokuapp.com/pins";
        try {
            Url += URLEncoder.encode(text, "UTF-8") + ".json";
            Mapas.clear();
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Url);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new AsyncTask_GetMarker().execute("http://pinit-api.herokuapp.com/pins.json");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void Actualizar() {
        Mapas.clear();
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://pinit-api.herokuapp.com/pins.json");
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new AsyncTask_GetMarker().execute("http://pinit-api.herokuapp.com/pins.json");
        }
        Tiempo_Start = System.currentTimeMillis();
    }

    public void delete( View v ){
        if (EditText_Search != null)
        {
            EditText_Search.setText( "" );
        }
    }

    //De Google Map
    @Override
    public boolean onMarkerClick(Marker marker) {
        String id_ramo = marker.getSnippet();

        //Obtener Pin
        Pin Pin_Elegido = Tabla_Pines.get(id_ramo);

        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new AsyncTask_GetEmail(Pin_Elegido).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new AsyncTask_GetEmail(Pin_Elegido).execute("");
        }
        marker.hideInfoWindow();
        return false;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        // TODO Auto-generated method stub

        this.point = point;
        Intent i = new Intent(mContext, CrearMarker.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onMapClick(LatLng latLng) {
    /*
        long Aux = (new Date()).getTime();
        if(( Aux - Tiempo_Start) > 3/2*60*1000) {
            //perform db poll/check
            Actualizar();
            Tiempo_Start = System.currentTimeMillis();
            Toast.makeText(getBaseContext(),"Actualizando...",Toast.LENGTH_SHORT);
        }*/

    }

    //Obtener Resultados
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK ) {
            //Columnas
            String id_ramo = "" + data.getStringExtra("id_ramo");
            String precio =""+ data.getStringExtra("precio");
            String descripcion =""+ data.getStringExtra("descripcion");
            String hora ="" + data.getStringExtra("hora");
            String realizacion = "false";
            String duracion = "5000";
            String Tipo_ayuda ="clase";

            //Crear Pin
            Pin Aux = new Pin();
            Aux.getRamo_Pin().setId_ramo(id_ramo);
            Aux.setPrecio(precio);
            Aux.setDescripcion(descripcion);
            Aux.setRealizacion(realizacion);
            Aux.setDuracion(duracion);
            Aux.setTipo_ayuda(Tipo_ayuda);
            Aux.setLatitude(point.latitude);
            Aux.setLongitude(point.longitude);
            Aux.setHora(hora);
            Aux.setPublicacion(hora);

            //ObtenerCampus
            Aux.setCampus(ObtenerCampus());


            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new AsyncTask_PostMarker(Mi_Usuario.getEmail(),Mi_Usuario.getToken(),Aux).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new AsyncTask_PostMarker(Mi_Usuario.getEmail(),Mi_Usuario.getToken(),Aux).execute("");
            }

        }
    }

    private String ObtenerCampus(){
        LatLng latLng_sj = new LatLng(-33.498444, -70.611722);
        LatLng latLng_casacentral = new LatLng(-33.440809, -70.640764);
        LatLng latLng_locontador = new LatLng(-33.419428, -70.618574);
        LatLng latLng_oriente = new LatLng(-33.446400, -70.593644);
        LatLng latLng_villarica = new LatLng(-38.738457, -72.601795);

        List<LatLng> Lista = new ArrayList<LatLng>();
        Lista.add(latLng_sj);Lista.add(latLng_casacentral);Lista.add(latLng_locontador);
        Lista.add(latLng_oriente);Lista.add(latLng_villarica);

        Double distmin=SacarDistancia(latLng_sj,point);
        LatLng min_latlng = latLng_sj;

        for (int i=0;i<Lista.size();i++){
            if(SacarDistancia(Lista.get(i),point)<distmin){
                distmin = SacarDistancia(Lista.get(i),point);
                min_latlng=Lista.get(i);
            }
        }

        if(min_latlng.equals(latLng_sj))
            return "San Joaquín";
        else if(min_latlng.equals(latLng_casacentral)){
            return "Casa Central";
        }
        else if(min_latlng.equals(latLng_locontador)){
            return "Lo Contador";
        }
        else if(min_latlng.equals(latLng_oriente)){
            return "Oriente";
        }
        else if(min_latlng.equals(latLng_villarica)){
            return "Villarica";
        }
        else{
            return "";
        }
    }

    private double SacarDistancia(LatLng primero,LatLng segundo) {

        final int R = 6371; // Radius of the earth

        Double latDistance = deg2rad(segundo.latitude - primero.latitude);
        Double lonDistance = deg2rad(segundo.longitude - primero.longitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(primero.latitude)) * Math.cos(deg2rad(segundo.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
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
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            result = "{ \"pins\":" + result +   "}" ;
            try {
                JSONObject json = new JSONObject(result);
                JSONArray articles = json.getJSONArray("pins");

                Tabla_Pines.clear();

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

                    //Obtener Ramos
                    try {
                        String ramos = articles.getJSONObject(i).getString("ramo");
                        String Mensaje_Buscador = EditText_Search.getText().toString().toLowerCase();
                        ramos = "{ \"ramos\":[" + ramos + "]}";
                        try {
                            JSONObject json_ramos = new JSONObject(ramos);
                            JSONArray articles_ramos = json_ramos.getJSONArray("ramos");
                            //Completar Pin
                            try {Aux.getRamo_Pin().setNombre(articles_ramos.getJSONObject(0).getString("nombre"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setSigla(articles_ramos.getJSONObject(0).getString("sigla"));                                } catch (Exception e) {                                }
                            try {Aux.getRamo_Pin().setUnidad_Academica(articles_ramos.getJSONObject(0).getString("rama"));                                } catch (Exception e) {                               }

                            //Filtrar
                            if (Aux.getRamo_Pin().getNombre() != null) {
                                Colocar_Marker(Aux);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            Toast.makeText(mContext, "Error al crear el Pin en onPostExecute_GetRamos()", Toast.LENGTH_LONG).show();

                        }
                    }catch (Exception e){
                        Toast.makeText(mContext, "Error al crear el Pin en onPostExecute_GetRamos()", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(mContext,"Error al crear el Pin en onPostExecute_GetMarker()", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
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

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        private void Colocar_Marker(Pin Aux){
            //Inserta el Marker
            if (Aux.getLatitudeNumber() != -1 && Aux.getLongitudeNumber() != -1) {
                LatLng lugar = new LatLng(Aux.getLatitudeNumber(), Aux.getLongitudeNumber());
                MarkerOptions Aux_Marker = new MarkerOptions()
                        .position(lugar)
                        .snippet(Aux.getId_pin())
                        .draggable(false);
                ColocarIcono(Aux_Marker,Aux);
            }
        }

        private void ColocarIcono(MarkerOptions Aux_Marker,Pin Aux) {
            String nombreRamo =  Aux.getRamo_Pin().getNombre();
            String sigla = Aux.getRamo_Pin().getSigla();
            if(Aux_Marker!= null && !sigla.equals("") && !nombreRamo.equals("")){
                if (esInicioSigla(sigla, new String[]{"ACT"})) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_actuacion));
                } else if (esInicioSigla(sigla, new String[]{"AGL"})) { // Agronomia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agronomia));
                } else if (esInicioSigla(sigla, new String[]{"AQH"})) { // Arquitectura
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arquitectura));
                } else if (esInicioSigla(sigla, new String[]{"ARQ", "ARO"})) { // Arte
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arte));
                } else if (esInicioSigla(sigla, new String[]{"BIO"})) { // Biologia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_biologia));
                } else if (esInicioSigla(sigla, new String[]{"DPT"})) { // Deportes
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_deporte));
                } else if (esInicioSigla(sigla, new String[]{"DEC"})) { // Derecho
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_derecho));
                } else if (esInicioSigla(sigla, new String[]{"DEE"})) { // Economia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_economia));
                } else if (esInicioSigla(sigla, new String[]{"EDU"})) { // Educacion
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_educacion));
                } else if (esInicioSigla(sigla, new String[]{"ENA"})) { // Enfermeria
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_enfermeria));
                } else if (esInicioSigla(sigla, new String[]{"FIS"})) { // Fisica
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fisica));
                } else if (esInicioSigla(sigla, new String[]{"GEO"})) { // Geografia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geografia));
                }else if (esInicioSigla(sigla, new String[]{"MAT"})) { // Matematicas
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_matematica));
                } else if (esInicioSigla(sigla, new String[]{"MUC"})) { // Musica
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_musica));
                } else if (esInicioSigla(sigla, new String[]{"ODO"})) { // Odontologia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_odontologia));
                } else if (esInicioSigla(sigla, new String[]{"QI"})) { // Quimica
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_quimica));
                } else if (esInicioSigla(sigla, new String[]{"CCP"})) { // Medicina
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_salud));
                } else if (esInicioSigla(sigla, new String[]{"FIL"})) { // Teologia
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_teologia));
                } else if (esInicioSigla(sigla, new String[]{"IC", "IE", "IM"})) { // Ingenieria
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ingenieria));
                }  else {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo_chico));
                }

                Tabla_Pines.put(Aux.getId_pin(),Aux);
                Mapas.addMarker(Aux_Marker);
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

    }

    private class AsyncTask_GetEmail extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private Pin Pin_Elegido;

        public AsyncTask_GetEmail(Pin aux) {
            this.Pin_Elegido = aux;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Obteniendo Información...", "Espere porfavor", true, false);
        }

        @Override
        protected String doInBackground(String... urls) {
            String Url= "http://pinit-api.herokuapp.com/usuarios/" ;
            Url +=  Pin_Elegido.getUsuario_Pin().getId_usuario() + ".json";
            return GET(Url);
        }

        @Override
        protected void onPostExecute(String result) {
            Boolean Paso=false;
            try {
                result = "{ \"usuarios\":[" + result + "]}";
                JSONObject json = new JSONObject(result);
                JSONArray articles = json.getJSONArray("usuarios");
                if(Pin_Elegido!=null) {
                    try {Pin_Elegido.getUsuario_Pin().setEmail(articles.getJSONObject(0).getString("email")); } catch (Exception e) {     }
                    try {Pin_Elegido.getUsuario_Pin().setNombre(articles.getJSONObject(0).getString("nombre"));} catch (Exception e) {    }
                    try {Pin_Elegido.getUsuario_Pin().setCarrera(articles.getJSONObject(0).getString("carrera"));            } catch (Exception e) {    }
                    try {Pin_Elegido.getUsuario_Pin().setRole(articles.getJSONObject(0).getString("role"));       } catch (Exception e) {     }
                    Pin_Elegido.getUsuario_Pin().setTelefono("+56994405326");
                    CrearAlertDialog(Pin_Elegido);
                }
            }  catch (JSONException e) {
                e.printStackTrace();
                Paso=true;
            }

            if(Paso)
                progressDialog.dismiss();
        }

        private void CrearAlertDialog(final Pin Pin_Elegido) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(Pin_Elegido.getRamo_Pin().getSigla() + " " + Pin_Elegido.getRamo_Pin().getNombre())
                        .setMessage(M_Utiles.CrearMensaje(Pin_Elegido))
                        .setPositiveButton("Mail", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] TO = {Pin_Elegido.getUsuario_Pin().getEmail()};
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("text/plain");

                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Book IT: Aceptar " + Pin_Elegido.getRamo_Pin().getNombre());
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Me gustaría realizar la clase que publicaste en Book IT \n " +
                                        "Para Contactarme te envío mi telefono. \n" +
                                        "Tel: " + "\n Saludos");
                                try {
                                    startActivity(Intent.createChooser(emailIntent, "Elija un cliente de correo electrónico: "));
                                    Log.i("Finished sending email...", "");
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(mContext, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNeutralButton("Whatsapp", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                                if (isWhatsappInstalled) {
                                    Boolean ContactoCreado = CreateContact("Book-IT / " + Pin_Elegido.getUsuario_Pin().getNombre(), Pin_Elegido.getUsuario_Pin().getTelefono());
                                    if (ContactoCreado) {
                                        Uri uri = Uri.parse("smsto:" + Pin_Elegido.getUsuario_Pin().getTelefono());
                                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                                        i.setPackage("com.whatsapp");
                                        startActivity(Intent.createChooser(i, ""));
                                        //deleteContact(numero,nombre);
                                    }

                                } else {
                                    Toast.makeText(mContext, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(goToMarket);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }catch (Exception e){
                Toast.makeText(mContext,"Error",Toast.LENGTH_LONG);
            }
            progressDialog.dismiss();
        }

        private Boolean CreateContact(String nombre, String numero) {
            String displayName = nombre;
            String mobileNumber = numero;
            String email = null;

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            // Names
            if (displayName != null) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                displayName).build());
            }

            // Mobile Number
            if (mobileNumber != null) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
            }

            // Email
            if (email != null) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
            }

            // Asking the Contact provider to create a new contact
            try {
                mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean deleteContact(String phone, String name) {
            Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
            Cursor cur = mContext.getContentResolver().query(contactUri, null, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                            mContext.getContentResolver().delete(uri, null, null);
                            return true;
                        }
                    } while (cur.moveToNext());
                }

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            return false;
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

        private boolean whatsappInstalledOrNot(String uri) {
            PackageManager pm = mContext.getPackageManager();
            boolean app_installed = false;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                app_installed = false;
            }
            return app_installed;
        }

    }

    //AsyncTasks Post
    private class AsyncTask_PostMarker extends AsyncTask<String, Void, Boolean>{

        private String NombreUsuario,Token;
        Pin Pin_Elegido=null;
        private ProgressDialog progressDialog;


        public AsyncTask_PostMarker(String usuario,String token,Pin Aux) {
            this.Pin_Elegido = Aux;
            this.NombreUsuario=usuario;
            this.Token=token;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Creando ...", "Espere porfavor", true, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return postData_Pins(NombreUsuario,Token,Pin_Elegido);
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

        private boolean postData_Pins(String usuario, String token,Pin Aux_Pin) {

            HttpURLConnection connection = null;
            try {

                String urlParameters =
                        "user_email=" + URLEncoder.encode(usuario, "UTF-8") +
                                "&user_token=" + URLEncoder.encode(token, "UTF-8") +
                                "&duracion=" + URLEncoder.encode(Aux_Pin.getDuracion(), "UTF-8") +
                                "&descripcion=" + URLEncoder.encode(Aux_Pin.getDescripcion(), "UTF-8") +
                                "&precio=" + URLEncoder.encode(Aux_Pin.getPrecio(), "UTF-8") +
                                "&facultad=" + URLEncoder.encode(Aux_Pin.getCampus(), "UTF-8") +
                                "&latitude=" + URLEncoder.encode(Aux_Pin.getLatitude(), "UTF-8") +
                                "&longitude=" + URLEncoder.encode(Aux_Pin.getLongitude(), "UTF-8") +
                                "&ramo_id=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getId_ramo(), "UTF-8")+
                                "&publicacion=" + URLEncoder.encode(Aux_Pin.getHora(), "UTF-8") +
                                "&realizacion=" + URLEncoder.encode(Aux_Pin.getRealizacion(), "UTF-8") +
                                "&tipo_ayuda=" + URLEncoder.encode(Aux_Pin.getTipo_ayuda(), "UTF-8") +
                                "&titulo=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getSigla() + " " + Aux_Pin.getRamo_Pin().getNombre() , "UTF-8");

                String UrlEntera = "http://pinit-api.herokuapp.com/pins" +
                        "?user_email=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&user_token=" + URLEncoder.encode(token, "UTF-8") +
                        "&duracion=" + URLEncoder.encode(Aux_Pin.getDuracion(), "UTF-8") +
                        "&descripcion=" + URLEncoder.encode(Aux_Pin.getDescripcion(), "UTF-8") +
                        "&precio=" + URLEncoder.encode(Aux_Pin.getPrecio(), "UTF-8") +
                        "&facultad=" + URLEncoder.encode(Aux_Pin.getCampus(), "UTF-8") +
                        "&latitude=" + URLEncoder.encode(Aux_Pin.getLatitude(), "UTF-8") +
                        "&longitude=" + URLEncoder.encode(Aux_Pin.getLongitude(), "UTF-8") +
                        "&ramo_id=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getId_ramo(), "UTF-8")+
                        "&publicacion=" + URLEncoder.encode(Aux_Pin.getPublicacion(), "UTF-8") +
                        "&realizacion=" + URLEncoder.encode(Aux_Pin.getRealizacion(), "UTF-8") +
                        "&tipo_ayuda=" + URLEncoder.encode(Aux_Pin.getTipo_ayuda(), "UTF-8") +
                        "&titulo=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getSigla() + " " + Aux_Pin.getRamo_Pin().getNombre() , "UTF-8");

                //Create connection
                URL url = new URL("http://pinit-api.herokuapp.com/pins");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                progressDialog.dismiss();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

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