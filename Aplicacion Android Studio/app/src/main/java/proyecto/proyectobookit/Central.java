package proyecto.proyectobookit;

import com.facebook.Session;
import com.google.android.gms.maps.GoogleMap;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.net.Uri;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Central extends Activity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {

    GoogleMap Mapas;
    LatLng point;
    RelativeLayout leftRL;
    DrawerLayout drawerLayout;

    //MarkerOptions
    List<MarkerOptions> Maps_Markers = new ArrayList<MarkerOptions>();

    private Menu menu;
    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Delete = null;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        leftRL = (RelativeLayout)findViewById(R.id.LeftDrawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_central2);

        String[] NombreOpciones = {"Crear Pin","Como Funciona ?","Cerrar Sesion"};

        ListView ListaOpciones = (ListView)findViewById(R.id.ListaOpcionesCentral);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NombreOpciones);
        ListaOpciones.setAdapter(adaptador);
        ListaOpciones.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                if(position ==0){
                    Toast.makeText(Central.this, "Manten apretado en el mapa el lugar en donde quieres recibir tu clase", Toast.LENGTH_SHORT).show();
                }
                else if(position ==1){
                   Intent NuevaActividad_Help = new Intent(getApplication(),Help.class);
                   startActivity(NuevaActividad_Help);
                }
                else {
                    Central.this.finish();
                }
            }
        });

        try {
            if(null == Mapas){
                Mapas = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();
                Mapas.setMyLocationEnabled(true);
                Mapas.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Mapas.setOnMarkerClickListener(this);
                Mapas.setOnMapLongClickListener(this);

                LatLng latLng = new LatLng(-33.498444, -70.611722);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                Mapas.animateCamera(cameraUpdate);

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == Mapas) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){

        }
        setupDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Connect the client.
        if(Mapas != null){
            if(Mapas.getMyLocation()!=null){
                LatLng latLng = new LatLng(Mapas.getMyLocation().getLatitude(), Mapas.getMyLocation().getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                Mapas.animateCamera(cameraUpdate);
            }
        }

        //Pedir todos los Pins
        //Aca hay q filtar segun lo q se busca por ejemplo si se busca ramo calculo II poner algo

        Mapas.clear();
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new HttpAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://pinit-api.herokuapp.com/pins.json");
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new HttpAsyncTask().execute("http://pinit-api.herokuapp.com/pins.json");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_central, menu);
        this.menu =menu;

        Menu_SearchItem = menu.findItem( R.id.menu_search );
        EditText_Search = (EditText) Menu_SearchItem.getActionView().findViewById( R.id.search );
        Button_Delete = (Button) Menu_SearchItem.getActionView().findViewById( R.id.delete );
        Button_Delete.setVisibility( EditText_Search.getText().length() > 0 ? View.VISIBLE : View.GONE );
        EditText_Search.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                Button_Delete.setVisibility( s.length() > 0 ? View.VISIBLE : View.INVISIBLE );
            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count,
                                           int after )
            {
            }

            @Override
            public void afterTextChanged( Editable s )
            {
                Button_Delete.setVisibility( s.length() > 0 ? View.VISIBLE : View.GONE );

                Mapas.clear();
                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    new HttpAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://pinit-api.herokuapp.com/pins.json");
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    new HttpAsyncTask().execute("http://pinit-api.herokuapp.com/pins.json");
                }
            }
        } );


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if(id ==  android.R.id.home){
            onLeft(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void onLeft(View view)
    {
        if(drawerLayout!=null && leftRL !=null)
            drawerLayout.openDrawer(leftRL);
    }

    public void delete( View v )
    {
        if (EditText_Search != null)
        {
            EditText_Search.setText( "" );
        }
    }


    //De Google Map

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String nombre_ramo = marker.getTitle();
        String descripcion = marker.getSnippet();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(nombre_ramo)
                .setMessage(
                        " Descripcion: \n	" + descripcion)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String[] TO = {"c.aguirre017@gmail.com"};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");


                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Primera Prueba");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Probando");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            Log.i("Finished sending email...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getBaseContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        marker.hideInfoWindow();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return false;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        // TODO Auto-generated method stub

        this.point = point;
        Intent i = new Intent(this, CrearMarker.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onMapClick(LatLng point) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK ) {
            //some code
            String ramo = "" + data.getStringExtra("ramo");
            ramo= ramo.replace(";","");
            String unidadacademica =""+ data.getStringExtra("unidadacademica");
            String precio =""+ data.getStringExtra("precio");
            String campus =""+ data.getStringExtra("campus");
            String descripcion =""+ data.getStringExtra("descripcion");
            descripcion=descripcion.replace(";"," ");

            //Obtener Fecha y Hora
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();


            //Publicacion (0), Realizacion(1) , Duracion (2) , Titulo (3), Descripcion (4), Precio (5), Tipo_ayuda (6), Facultad (7), Latitud (8), Longitud (9)

            String Mensaje= ts + ";" + "no" + ";"  + "5000" +  ";" + ramo +  ";" + descripcion +  ";" + precio +  ";" + "clase" +  ";" + campus +  ";" + point.latitude+ ";" +point.longitude;

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new AsyncTask_PostMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Mensaje);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new AsyncTask_PostMarker().execute(Mensaje);
            }

        }
    }

    public boolean postData_Pins( String usuario, String token, String publicacion, String realizacion,  String duracion ,  String titulo ,String descripcion,String precio, String tipo_ayuda,  String facultad, String latitud , String longitud) {

        HttpURLConnection connection = null;
        try {

            String urlParameters =
                    "user_email=" + URLEncoder.encode(usuario, "UTF-8") +
                            "&user_token=" + URLEncoder.encode(token, "UTF-8") +
                            "&duracion=" + URLEncoder.encode(duracion, "UTF-8") +
                            "&titulo=" + URLEncoder.encode(titulo, "UTF-8") +
                            "&descripcion=" + URLEncoder.encode(descripcion, "UTF-8") +
                            "&precio=" + URLEncoder.encode(precio, "UTF-8") +
                            "&facultad=" + URLEncoder.encode(facultad, "UTF-8") +
                            "&latitude=" + URLEncoder.encode(latitud, "UTF-8") +
                            "&longitude=" + URLEncoder.encode(longitud, "UTF-8");
            //"&publicacion=" + URLEncoder.encode(publicacion, "UTF-8") +
            //"&realizacion=" + URLEncoder.encode(realizacion, "UTF-8") +
            //"&tipo ayuda=" + URLEncoder.encode(tipo_ayuda, "UTF-8") +

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
            return false;

        } finally {

            if (connection != null) {
                connection.disconnect();
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

    //AsyncTasks

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {

            String Mensaje_Buscador =EditText_Search.getText().toString().toLowerCase();
            result = "{ \"pins\":" + result +   "}" ;
            try {
                JSONObject json = new JSONObject(result);
                JSONArray articles = json.getJSONArray("pins");
                for (int i = 0; i < articles.length(); i++) {
                    String Titulo=null;
                    try{Titulo = articles.getJSONObject(i).getString("titulo");}catch(Exception e){}

                    if(Titulo != null) {
                        if(Mensaje_Buscador.equals("")) {
                            double lat = -1, lon = -1, precio = -1;
                            String Descripcion = null, Facultad = null, Publicacion = null, unidad_academica = "";

                            //Encontramos los valores
                            try {
                                lat = Double.parseDouble(articles.getJSONObject(i).getString("latitude"));
                            } catch (Exception e) {
                            }
                            try {
                                lon = Double.parseDouble(articles.getJSONObject(i).getString("longitude"));
                            } catch (Exception e) {
                            }

                            try {
                                Descripcion = articles.getJSONObject(i).getString("descripcion");
                            } catch (Exception e) {
                            }
                            try {
                                Publicacion = articles.getJSONObject(i).getString("publicacion");
                            } catch (Exception e) {
                            }
                            try {
                                precio = Double.parseDouble(articles.getJSONObject(i).getString("precio"));
                            } catch (Exception e) {
                            }
                            try {
                                unidad_academica = articles.getJSONObject(i).getString("unidad_academica");
                            } catch (Exception e) {
                            }

                            //Creando Mensaje
                            String Mensaje;
                            if (Descripcion != null)
                                Mensaje = Descripcion;
                            else
                                Mensaje = "No hay";

                            if (Publicacion != null) {
                                Mensaje += "\n Fecha Creación: \n	" + Publicacion;
                            } else {
                                Mensaje += "\n Fecha Creación: \n	No hay";
                            }

                            if (precio != -1) {
                                Mensaje += "\n Dispuesto a Pagar: \n	" + precio;
                            } else {
                                Mensaje += "\n Dispuesto a Pagar: \n	No hay";
                            }

                            //Inserta el Marker
                            if (lat != -1 && lon != -1) {
                                LatLng lugar = new LatLng(lat, lon);
                                MarkerOptions Aux = new MarkerOptions()
                                        .position(lugar)
                                        .title(Titulo)
                                        .snippet(Mensaje)
                                        .draggable(false);

                                if (unidad_academica.toLowerCase().equals("actuación")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_actuacion));
                                } else if (unidad_academica.toLowerCase().equals("agronomía")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agronomia));
                                } else if (unidad_academica.toLowerCase().equals("arquitectura")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arquitectura));
                                } else if (unidad_academica.toLowerCase().equals("arte")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_art));
                                } else if (unidad_academica.toLowerCase().equals("ciencias biológicas")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_biologia));
                                } else if (unidad_academica.toLowerCase().equals("cursos deportivos")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_deporte));
                                } else if (unidad_academica.toLowerCase().equals("derecho")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_derecho));
                                } else if (unidad_academica.toLowerCase().equals("ciencias económicas y administrativas")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_economia));
                                } else if (unidad_academica.toLowerCase().equals("educación")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_educacion));
                                } else if (unidad_academica.toLowerCase().equals("enfermería")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_enfermeria));
                                } else if (unidad_academica.toLowerCase().equals("física")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fisica));
                                } else if (unidad_academica.toLowerCase().equals("geografía")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geografia));
                                } else if (unidad_academica.toLowerCase().equals("ingeniería")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ingenieria));
                                } else if (unidad_academica.toLowerCase().equals("matemática")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_matematicas));
                                } else if (unidad_academica.toLowerCase().equals("música")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_musica));
                                } else if (unidad_academica.toLowerCase().equals("odontología")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_odontologia));
                                } else if (unidad_academica.toLowerCase().equals("química")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_quimica));
                                } else if (unidad_academica.toLowerCase().equals("ciencias de la salud/medicina")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_salud));
                                } else if (unidad_academica.toLowerCase().equals("teología")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_teologia));
                                } else {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo));
                                }

                                Mapas.addMarker(Aux);
                            }
                        }
                        else if(Titulo.toLowerCase().contains(Mensaje_Buscador)) {
                            double lat = -1, lon = -1, precio = -1;
                            String Descripcion = null, Facultad = null, Publicacion = null, unidad_academica = "";

                            //Encontramos los valores
                            try {
                                lat = Double.parseDouble(articles.getJSONObject(i).getString("latitude"));
                            } catch (Exception e) {
                            }
                            try {
                                lon = Double.parseDouble(articles.getJSONObject(i).getString("longitude"));
                            } catch (Exception e) {
                            }

                            try {
                                Descripcion = articles.getJSONObject(i).getString("descripcion");
                            } catch (Exception e) {
                            }
                            try {
                                Publicacion = articles.getJSONObject(i).getString("publicacion");
                            } catch (Exception e) {
                            }
                            try {
                                precio = Double.parseDouble(articles.getJSONObject(i).getString("precio"));
                            } catch (Exception e) {
                            }
                            try {
                                unidad_academica = articles.getJSONObject(i).getString("unidad_academica");
                            } catch (Exception e) {
                            }

                            //Creando Mensaje
                            String Mensaje;
                            if (Descripcion != null)
                                Mensaje = Descripcion;
                            else
                                Mensaje = "No hay";

                            if (Publicacion != null) {
                                Mensaje += "\n Fecha Creación: \n	" + Publicacion;
                            } else {
                                Mensaje += "\n Fecha Creación: \n	No hay";
                            }

                            if (precio != -1) {
                                Mensaje += "\n Dispuesto a Pagar: \n	" + precio;
                            } else {
                                Mensaje += "\n Dispuesto a Pagar: \n	No hay";
                            }

                            //Inserta el Marker
                            if (lat != -1 && lon != -1) {
                                LatLng lugar = new LatLng(lat, lon);
                                MarkerOptions Aux = new MarkerOptions()
                                        .position(lugar)
                                        .title(Titulo)
                                        .snippet(Mensaje)
                                        .draggable(false);

                                if (unidad_academica.toLowerCase().equals("actuación")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_actuacion));
                                } else if (unidad_academica.toLowerCase().equals("agronomía")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agronomia));
                                } else if (unidad_academica.toLowerCase().equals("arquitectura")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arquitectura));
                                } else if (unidad_academica.toLowerCase().equals("arte")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_art));
                                } else if (unidad_academica.toLowerCase().equals("ciencias biológicas")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_biologia));
                                } else if (unidad_academica.toLowerCase().equals("cursos deportivos")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_deporte));
                                } else if (unidad_academica.toLowerCase().equals("derecho")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_derecho));
                                } else if (unidad_academica.toLowerCase().equals("ciencias económicas y administrativas")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_economia));
                                } else if (unidad_academica.toLowerCase().equals("educación")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_educacion));
                                } else if (unidad_academica.toLowerCase().equals("enfermería")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_enfermeria));
                                } else if (unidad_academica.toLowerCase().equals("física")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fisica));
                                } else if (unidad_academica.toLowerCase().equals("geografía")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geografia));
                                } else if (unidad_academica.toLowerCase().equals("ingeniería")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ingenieria));
                                } else if (unidad_academica.toLowerCase().equals("matemática")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_matematicas));
                                } else if (unidad_academica.toLowerCase().equals("música")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_musica));
                                } else if (unidad_academica.toLowerCase().equals("odontología")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_odontologia));
                                } else if (unidad_academica.toLowerCase().equals("química")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_quimica));
                                } else if (unidad_academica.toLowerCase().equals("ciencias de la salud/medicina")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_salud));
                                } else if (unidad_academica.toLowerCase().equals("teología")) {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_teologia));
                                } else {
                                    Aux.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo));
                                }

                                Mapas.addMarker(Aux);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getBaseContext(),"Error al conectar con la Base de datos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncTask_PostMarker extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub

            String[] Mensaje = params[0].split(";");

            //Publicacion (0)
            String publicacion = Mensaje[0];
            // Realizacion(1)
            String realizacion = Mensaje[1];
            // Duracion (2)
            String duracion = Mensaje[2];
            // Titulo (3)
            String titulo = Mensaje[3];
            // Descripcion (4)
            String descripcion = Mensaje[4];
            // Precio (5)
            String precio = Mensaje[5];
            // Tipo_ayuda (6)
            String tipo_ayuda = Mensaje[6];
            // Facultad (7)
            String facultad = Mensaje[7];
            // Latitud (8)
            String latitud = Mensaje[8];
            // Longitud (9)
            String longitud = Mensaje[9];


            return postData_Pins("ejcorrea@uc.cl","LDskzPi1vfr31746VKG3",publicacion, realizacion, duracion, titulo, descripcion, precio, tipo_ayuda, facultad, latitud, longitud);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getBaseContext(),"Pin Creado Exitosamente", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getBaseContext(),"Ocurrio un Error", Toast.LENGTH_LONG).show();
            }

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new HttpAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://pinit-api.herokuapp.com/pins.json");
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new HttpAsyncTask().execute("http://pinit-api.herokuapp.com/pins.json");
            }
        }
    }

}
