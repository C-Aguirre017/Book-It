package proyecto.proyectobookit.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.CrearMarker;
import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.utils.AlertDialogMetodos;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;
import proyecto.proyectobookit.utils.AsyncMetodos;


import static proyecto.proyectobookit.R.layout.fragment_mapa;


public class Mapa extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {

    Context mContext;

    GoogleMap Mapas;
    LatLng point;

    public static Hashtable<String,Pin> Tabla_Pines = new Hashtable<String,Pin>();
    private long tiempoStart;
    private View rootView;
    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Buscar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //Configurar Mapa
        try {
            rootView = inflater.inflate(fragment_mapa, container, false);
            if(null == Mapas){
                //Mapas = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
                Mapas = getMapFragment().getMap();
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

                actualizarGoogleMaps(null);
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

        //Escondidos
        menu.findItem(R.id.menu_search).setVisible(false);
        //menu.findItem(R.id.menucentral_viewasList).setVisible(false);
        menu.findItem(R.id.menucentral_editar).setVisible(false);

        EditText_Search = (EditText) Menu_SearchItem.getActionView().findViewById( R.id.search );
        Button_Buscar = (Button) Menu_SearchItem.getActionView().findViewById(R.id.buscar);
        Button_Buscar.setVisibility( EditText_Search.getText().length() > 0 ? View.VISIBLE : View.GONE );
        Button_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarBoton();
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
                    actualizarBoton();
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
                actualizarGoogleMaps(null);
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



    //Google Maps Methods
    @Override
    public boolean onMarkerClick(Marker marker) {
        AlertDialogMetodos.crearAlertPin(Tabla_Pines.get(marker.getSnippet()),mContext);
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
        if(( Aux - tiempoStart) > 3/2*60*1000) {
            //perform db poll/check
            Actualizar();
            Tiempo_Start = System.currentTimeMillis();
            Toast.makeText(getBaseContext(),"Actualizando...",Toast.LENGTH_SHORT);
        }*/

    }

    private void actualizarBoton(){
        String text = EditText_Search.getText().toString().toLowerCase(Locale.getDefault());
        String url = Configuracion.URLSERVIDOR + "/pins.json";
        actualizarGoogleMaps(url);
    }

    private void actualizarGoogleMaps(String url) {
        if(url == null)
          url = Configuracion.URLSERVIDOR + "/pins.json";

        Mapas.clear();
        if (Build.VERSION.SDK_INT >= 11) {
            new AsyncTask_GetMarker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        } else {
            new AsyncTask_GetMarker().execute(url);
        }
        tiempoStart = System.currentTimeMillis();
    }

    public void delete( View v ){
        if (EditText_Search != null)
        {
            EditText_Search.setText( "" );
        }
    }



    //Obtener Resultados
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1 && resultCode == Activity.RESULT_OK ) {

            //Datos
            String id_ramo = data.getStringExtra("id_ramo");
            String precio = data.getStringExtra("precio");
            String descripcion = data.getStringExtra("descripcion");
            String hora = data.getStringExtra("hora");
            String titulo = data.getStringExtra("titulo");
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
            Aux.setTitulo(titulo);

            // ObtenerCampus
            Aux.setCampus(ObtenerCampus());

            if (Build.VERSION.SDK_INT >= 11) {
                new AsyncTask_PostMarker(Aux).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
            } else {
                new AsyncTask_PostMarker(Aux).execute("");
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
        Lista.add(latLng_sj);
        Lista.add(latLng_casacentral);
        Lista.add(latLng_locontador);
        Lista.add(latLng_oriente);
        Lista.add(latLng_villarica);

        Double distmin=SacarDistancia(latLng_sj, point);
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

        @Override
        protected String doInBackground(String... urls) {
            return ConsultaHTTP.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Tabla_Pines.clear();
            List<Pin> auxPines = AsyncMetodos.convertirJSON_Pin(result, mContext);
            for (Pin auxPin: auxPines) {
                if (auxPin.getRamo_Pin().getNombre() != null) {
                    Colocar_Marker(auxPin);
                }
            }

        }

        private void Colocar_Marker(Pin Aux){
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

    private class AsyncTask_PostMarker extends AsyncTask<String, Void, Boolean>{

        Pin Pin_Elegido = null;
        private ProgressDialog progressDialog;

        public AsyncTask_PostMarker(Pin Aux) {
            this.Pin_Elegido = Aux;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Creando Clase", "Espere porfavor ...", true, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return postData_Pins(Pin_Elegido);
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

        private boolean postData_Pins(Pin Aux_Pin) {

            try {
                Hashtable<String, String> rparams = new Hashtable<String, String>();
                rparams.put("pin[user_id]", Usuario.getUsuarioActual().getId_usuario());
                rparams.put("user_token", Usuario.getUsuarioActual().getToken());
                rparams.put("pin[duration]", Aux_Pin.getDuracion());
                rparams.put("pin[description]", Aux_Pin.getDescripcion());
                rparams.put("pin[price]", Aux_Pin.getPrecio());
                rparams.put("pin[faculty]", Aux_Pin.getCampus());
                rparams.put("pin[latitude]", Aux_Pin.getLatitude());
                rparams.put("pin[longitude]", Aux_Pin.getLongitude());
                rparams.put("pin[course_id]", Aux_Pin.getRamo_Pin().getId_ramo());
                rparams.put("pin[publication]", Aux_Pin.getHora());
                rparams.put("pin[realization]", Aux_Pin.getRealizacion());
                rparams.put("pin[help_type]", Aux_Pin.getTipo_ayuda());
                rparams.put("pin[title]", Aux_Pin.getTitulo());

                ConsultaHTTP.POST(Configuracion.URLSERVIDOR + "/pins.json", rparams);

                progressDialog.dismiss();

                return true;

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
                return false;

            }
        }

    }



    //Getters and Setters
    public void setMapa (GoogleMap Aux){
        Mapas = Aux;
    }
    public void setContext(Context Aux){
        mContext = Aux;
    }
    private MapFragment getMapFragment() {
        FragmentManager fm = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.mapView);
    }


}
