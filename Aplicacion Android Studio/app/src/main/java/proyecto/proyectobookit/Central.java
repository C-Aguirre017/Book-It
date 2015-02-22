package proyecto.proyectobookit;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.GoogleMap;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
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

    private Menu menu;
    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Delete = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private Session fbSession;
    private GraphUser gUser;

    private MetodosUtiles M_Utiles = new MetodosUtiles();
    private Pin Pin_Elegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        // Facebook
        fbSession = Session.getActiveSession();
        Request request =  Request.newMeRequest(fbSession, new Request.GraphUserCallback() {
            public void onCompleted(GraphUser user, Response response) {

                if (user != null) {
                    gUser = user;
                    Log.d("Mensaje", gUser.getId());
                    setFbImage(gUser.getId());
                    // first_nameText.setText(user.getFirstName());
                    // last_nameText.setText(user.getLastName());
                }
                if (response != null) {
                    System.out.println("Response=" + response);
                }
            }
        });
        Request.executeBatchAsync(request);

        // Configurar Action Bar
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actionbar_campus,android.R.layout.simple_spinner_dropdown_item);

        ActionBar.OnNavigationListener callback = new ActionBar.OnNavigationListener() {

            String[] items = getResources().getStringArray(R.array.actionbar_campus); // List items from res

            @Override
            public boolean onNavigationItemSelected(int position, long id) {

                // Do stuff when navigation item is selected

                Toast.makeText(getBaseContext(),items[position],Toast.LENGTH_LONG);
                if(Mapas != null) {
                    if (items[position].toLowerCase().equals("san joaquín")) {

                        LatLng latLng = new LatLng(-33.498444, -70.611722);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                        Mapas.animateCamera(cameraUpdate);
                    }
                    else if (items[position].toLowerCase().equals("casa central")) {

                        LatLng latLng = new LatLng(-33.440809, -70.640764);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                        Mapas.animateCamera(cameraUpdate);
                    }
                    else if (items[position].toLowerCase().equals("lo contador")) {

                        LatLng latLng = new LatLng(-33.419428, -70.618574);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                        Mapas.animateCamera(cameraUpdate);
                    }
                    else if (items[position].toLowerCase().equals("oriente")) {

                        LatLng latLng = new LatLng(-33.437226, -70.623899);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                        Mapas.animateCamera(cameraUpdate);
                    }
                    else if (items[position].toLowerCase().equals("villarica")) {

                        LatLng latLng = new LatLng(-38.738457, -72.601795);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                        Mapas.animateCamera(cameraUpdate);
                    }
                }

                return true;

            }

        };


        ActionBar actions = getActionBar();
        try{ actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);}catch (Exception e){}
        actions.setDisplayHomeAsUpEnabled(true);
        actions.setDisplayShowTitleEnabled(false);
        try{actions.setListNavigationCallbacks(adapter, callback);}catch (Exception e){}



        // Configurar List View
        leftRL = (RelativeLayout)findViewById(R.id.LeftDrawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_central2);

        String[] NombreOpciones = {"Crear Pin","Como Funciona ?","Cerrar Sesion"};
        ListView ListaOpciones = (ListView)findViewById(R.id.ListaOpcionesCentral);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NombreOpciones);
        ListaOpciones.setAdapter(adaptador);
        ListaOpciones.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                if(position == 0){
                    Toast.makeText(Central.this, "Manten apretado en el mapa el lugar en donde quieres recibir tu clase", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                   Intent NuevaActividad_Help = new Intent(getApplication(),Help.class);
                   startActivity(NuevaActividad_Help);
                } else {
                    Central.this.finish();
                }
            }
        });

        //Configurar Mapa
        try {
            if(null == Mapas){
                Mapas = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();
                Mapas.setMyLocationEnabled(true);
                Mapas.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Mapas.setBuildingsEnabled(true);
                Mapas.setOnMarkerClickListener(this);
                Mapas.setOnMapLongClickListener(this);

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
         Actualizar();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setFbImage(String id){
        ImageView user_picture = (ImageView)findViewById(R.id.profile_image);
        URL img_value = null;
        try {
            img_value = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
            Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
            user_picture.setImageBitmap(mIcon1);
        } catch(Exception e) {

        }
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
                Actualizar();
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
        else if(id== R.id.menucentral_refresh){
            Actualizar();
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

    private void Actualizar() {
        Mapas.clear();
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://pinit-api.herokuapp.com/pins.json");
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new AsyncTask_GetMarker().execute("http://pinit-api.herokuapp.com/pins.json");
        }
    }

    //Obtener Resultados
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK ) {

            //Usuario y Token
            String usuario = "ejcorrea@uc.cl";
            String token = "LDskzPi1vfr31746VKG3";

            //Columnas
            String id_ramo = "" + data.getStringExtra("id_ramo");
            String precio =""+ data.getStringExtra("precio");
            String campus =""+ data.getStringExtra("campus");
            String descripcion =""+ data.getStringExtra("descripcion");
            String titulo = "" + data.getStringExtra("titulo");
            String realizacion = "false";
            String duracion = "5000";
            String Tipo_ayuda ="clase";

            //Obtener Fecha y Hora
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            //Crear Pin
            Pin Aux = new Pin();
            Aux.getRamo_Pin().setId_ramo(id_ramo);
            Aux.setPrecio(precio);
            Aux.setCampus(campus);
            Aux.setDescripcion(descripcion);
            //Aux.setTitulo(titulo);
            Aux.setRealizacion(realizacion);
            Aux.setDuracion(duracion);
            Aux.setTipo_ayuda(Tipo_ayuda);
            Aux.setLatitude(point.latitude);
            Aux.setLongitude(point.longitude);

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new AsyncTask_PostMarker(usuario,token,Aux).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new AsyncTask_PostMarker(usuario,token,Aux).execute("");
            }

        }
    }

    //AsyncTasks Get
    private class AsyncTask_GetMarker extends AsyncTask<String, Void, String> {

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
                    try {Aux.setPublicacion(articles.getJSONObject(i).getString("publicacion")); } catch (Exception e) {}
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

                    Pin_Elegido=Aux;
                    //Obtener Unidad_Academica
                    if (Build.VERSION.SDK_INT >= 11) {
                        //--post GB use serial executor by default --
                        new AsyncTask_GetRamos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
                    } else {
                        //--GB uses ThreadPoolExecutor by default--
                        new AsyncTask_GetRamos().execute("");
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getBaseContext(),"Error al crear el Pin en onPostExecute_GetMarker()", Toast.LENGTH_LONG).show();
            }
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

    private class AsyncTask_GetRamos extends AsyncTask<String, Void, String> {

        private Pin Aux;

        public AsyncTask_GetRamos() {
            this.Aux = Pin_Elegido;
        }

        @Override
        protected String doInBackground(String... urls) {
            String Url = "http://pinit-api.herokuapp.com/ramos/" + Aux.getRamo_Pin().getId_ramo() + ".json";
            return GET(Url);
        }

        @Override
        protected void onPostExecute(String result) {
            String Mensaje_Buscador = EditText_Search.getText().toString().toLowerCase();
            result = "{ \"ramos\":[" + result + "]}";
            try {
                JSONObject json = new JSONObject(result);
                JSONArray articles = json.getJSONArray("ramos");
                if(articles.length()== 1) {

                    //Completar Pin
                    try {Aux.getRamo_Pin().setNombre(articles.getJSONObject(0).getString("nombre")); } catch (Exception e) {}
                    try {Aux.getRamo_Pin().setSigla(articles.getJSONObject(0).getString("sigla"));} catch (Exception e) {}
                    try {Aux.getRamo_Pin().setUnidad_Academica(articles.getJSONObject(0).getString("rama"));} catch (Exception e) {}

                    //Filtrar
                    if(Aux.getRamo_Pin().getNombre() != null) {
                        if(Mensaje_Buscador.equals("")) {
                                Colocar_Marker();
                        }
                        else if(Aux.getRamo_Pin().getNombre().toLowerCase().contains(Mensaje_Buscador)) {
                                Colocar_Marker();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getBaseContext(), "Error al crear el Pin en onPostExecute_GetRamos()", Toast.LENGTH_LONG).show();
            }

        }

        private void Colocar_Marker(){
            //Inserta el Marker
            if (Aux.getLatitudeNumber() != -1 && Aux.getLongitudeNumber() != -1) {
                LatLng lugar = new LatLng(Aux.getLatitudeNumber(), Aux.getLongitudeNumber());
                MarkerOptions Aux_Marker = new MarkerOptions()
                        .position(lugar)
                        .snippet(M_Utiles.CrearMensaje(Aux))
                        .draggable(false);
                ColocarIcono(Aux_Marker);
            }
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

        private void ColocarIcono(MarkerOptions Aux_Marker) {
            String NombreRamo =  Aux.getRamo_Pin().getNombre();
            String Sigla = Aux.getRamo_Pin().getSigla();
            String unidad_academica=  Aux.getRamo_Pin().getUnidad_Academica();

            if(Aux_Marker!= null && !Sigla.equals("") && !NombreRamo.equals("")){
                if (unidad_academica.toLowerCase().equals("actuación")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_actuacion));
                } else if (unidad_academica.toLowerCase().equals("agronomía")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agronomia));
                } else if (unidad_academica.toLowerCase().equals("arquitectura")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arquitectura));
                } else if (unidad_academica.toLowerCase().equals("arte")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_art));
                } else if (unidad_academica.toLowerCase().equals("ciencias biológicas")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_biologia));
                } else if (unidad_academica.toLowerCase().equals("cursos deportivos")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_deporte));
                } else if (unidad_academica.toLowerCase().equals("derecho")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_derecho));
                } else if (unidad_academica.toLowerCase().equals("ciencias económicas y administrativas")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_economia));
                } else if (unidad_academica.toLowerCase().equals("educación")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_educacion));
                } else if (unidad_academica.toLowerCase().equals("enfermería")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_enfermeria));
                } else if (unidad_academica.toLowerCase().equals("física")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fisica));
                } else if (unidad_academica.toLowerCase().equals("geografía")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geografia));
                } else if (unidad_academica.toLowerCase().equals("ingeniería")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ingenieria));
                } else if (unidad_academica.toLowerCase().equals("matemática")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_matematicas));
                } else if (unidad_academica.toLowerCase().equals("música")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_musica));
                } else if (unidad_academica.toLowerCase().equals("odontología")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_odontologia));
                } else if (unidad_academica.toLowerCase().equals("química")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_quimica));
                } else if (unidad_academica.toLowerCase().equals("ciencias de la salud/medicina")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_salud));
                } else if (unidad_academica.toLowerCase().equals("teología")) {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_teologia));
                } else {
                    Aux_Marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo));
                }
                Aux_Marker.title(Sigla + " " + NombreRamo);
                Mapas.addMarker(Aux_Marker);
            }
        }

    }

    //AsyncTasks Post
    private class AsyncTask_PostMarker extends AsyncTask<String, Void, Boolean>{

        private String NombreUsuario,Token;
        Pin Pin_Elegido=null;
        public AsyncTask_PostMarker(String usuario,String token,Pin Aux) {
            this.Pin_Elegido = Aux;
            this.NombreUsuario=usuario;
            this.Token=token;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub

            return postData_Pins(NombreUsuario,Token,Pin_Elegido);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getBaseContext(),"Pin Creado Exitosamente", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getBaseContext(),"Ocurrio un Error", Toast.LENGTH_LONG).show();
            }
        }

        private boolean postData_Pins(String usuario, String token,Pin Aux_Pin) {

            HttpURLConnection connection = null;
            try {

                String urlParameters =
                        "user_email=" + URLEncoder.encode(usuario, "UTF-8") +
                                "&user_token=" + URLEncoder.encode(token, "UTF-8") +
                                "&duracion=" + URLEncoder.encode(Aux_Pin.getDuracion(), "UTF-8") +
                                "&titulo=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getId_ramo(), "UTF-8") +
                                "&descripcion=" + URLEncoder.encode(Aux_Pin.getDescripcion(), "UTF-8") +
                                "&precio=" + URLEncoder.encode(Aux_Pin.getPrecio(), "UTF-8") +
                                "&facultad=" + URLEncoder.encode(Aux_Pin.getCampus(), "UTF-8") +
                                "&latitude=" + URLEncoder.encode(Aux_Pin.getLatitude(), "UTF-8") +
                                "&longitude=" + URLEncoder.encode(Aux_Pin.getLongitude(), "UTF-8");
                //"&ramo=" + URLEncoder.encode(Aux_Pin.getId_ramo, "UTF-8");
                //"&publicacion=" + URLEncoder.encode(publicacion, "UTF-8") +
                //"&realizacion=" + URLEncoder.encode(realizacion, "UTF-8") +
                //"&tipo ayuda=" + URLEncoder.encode(tipo_ayuda, "UTF-8") +

                String UrlEntera = "http://pinit-api.herokuapp.com/pins" + "?user_email=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&user_token=" + URLEncoder.encode(token, "UTF-8") +"&duracion=" + URLEncoder.encode(Aux_Pin.getDuracion(), "UTF-8")
                        +"&titulo=" + URLEncoder.encode(Aux_Pin.getRamo_Pin().getId_ramo(), "UTF-8") +"&descripcion=" +
                        URLEncoder.encode(Aux_Pin.getDescripcion(), "UTF-8") +"&precio=" + URLEncoder.encode(Aux_Pin.getPrecio(), "UTF-8") +
                        "&facultad=" + URLEncoder.encode(Aux_Pin.getCampus(), "UTF-8") + "&latitude=" + URLEncoder.encode(Aux_Pin.getLatitude(), "UTF-8") +
                        "&longitude=" + URLEncoder.encode(Aux_Pin.getLongitude(), "UTF-8");

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

    }

}
