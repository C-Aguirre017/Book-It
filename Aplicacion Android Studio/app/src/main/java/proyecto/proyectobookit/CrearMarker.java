package proyecto.proyectobookit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CrearMarker extends Activity {

    // Declare Variables
    ListView lista_ramos;
    ListViewAdapter adapter;
    EditText editsearch;

    MetodosUtiles M_Utiles = new MetodosUtiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marker);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        lista_ramos = (NestedListView) findViewById(R.id.crearmarker_list);
        editsearch = (EditText) findViewById(R.id.crearmarker_search);

        adapter = new ListViewAdapter(this,editsearch);
        lista_ramos.setAdapter(adapter);
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }
        });

         /*
        //Unidad Academica
        Spinner dropdown_ua = (Spinner)findViewById(R.id.crearmarker_unidadacademica);
        String[] items = new String[]{"Actuación", "Agronomía", "Arquitectura","Arte","Ciencias Biológicas","Cursos Deportivos","Derecho","Ciencias Económicas y Administrativas","Educación","Enfermería","Física","Geografía","Ingeniería","Matemática","Música","Odontología","Química","Ciencias de la Salud/Medicina","Teología","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown_ua.setAdapter(adapter);


        //Facultad
        Spinner dropdown_campus = (Spinner)findViewById(R.id.crearmarker_facultad);
        String[] items_campus = new String[]{"Casa Central","Lo Contador","Oriente","San Joaquin","Villarica"};
        ArrayAdapter<String> adapter_campus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items_campus);
        dropdown_campus.setAdapter(adapter_campus);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_marker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id ==  android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void Aceptar(View v){

        if(VerificarEscrito()) {
            String  descripcion = "", precio = "", campus = "",titulo="",id_ramo="",hora="";
            EditText get_descripcion = (EditText) findViewById(R.id.crearmarker_descripcion);
            EditText get_precio = (EditText) findViewById(R.id.crearmarker_precio);
            EditText get_hora = (EditText) findViewById(R.id.crearmarker_hora);
            // Spinner get_campus = (Spinner) findViewById(R.id.crearmarker_facultad);

            if(adapter.getElegido()!=null) {
                id_ramo = adapter.getElegido().getId_ramo();
                titulo = adapter.getElegido().getSigla() + " " + adapter.getElegido().getNombre();
                descripcion = get_descripcion.getText().toString();
                precio = get_precio.getText().toString();
                hora = get_hora.getText().toString();
                //campus = get_campus.getSelectedItem().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final String finalDescripcion = descripcion;
                final String finalPrecio = precio;
                final String finalCampus = campus;
                final String finalid_ramo = id_ramo;
                final String finalTitulo = titulo;
                final String finalHora = hora;
                builder.setTitle(titulo)
                        .setMessage(M_Utiles.CrearMensaje(descripcion, "", precio))
                        .setPositiveButton("Crear Marker", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Salir(finalid_ramo, finalDescripcion, finalPrecio, finalCampus, finalTitulo, finalHora);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                CrearAlertDialog("Porfavor seleccione un curso de la lista","Error de Curso");
            }
        }
    }

    private void Salir(String id_ramo, String descripcion, String precio, String campus, String titulo, String finalHora){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("id_ramo",id_ramo);
        returnIntent.putExtra("descripcion", descripcion);
        returnIntent.putExtra("precio", precio);
        returnIntent.putExtra("hora", finalHora);

        setResult(RESULT_OK, returnIntent);
        this.onDestroy();
        this.finish();
    }

    private boolean VerificarEscrito()
    {

        //Ramo
        EditText Aux_Ramo = (EditText) findViewById(R.id.crearmarker_search);
        if(Aux_Ramo!=null) {
            if (Aux_Ramo.getText().toString().length() == 0) {
                CrearAlertDialog("La opcion curso se encuentra vacia", "Faltan Datos");
                return false;
            }
        }
        else
            return false;

        //Precio
        EditText Aux_Precio = (EditText) findViewById(R.id.crearmarker_precio);
        if(Aux_Precio!=null) {
            if (Aux_Precio.getText().toString().length() == 0) {
                CrearAlertDialog("La opcion precio se encuentra vacia", "Faltan Datos");
                return false;
            }
        }
        else
            return false;

        return true;
    }

    private void CrearAlertDialog(String Mensaje, String Titulo){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Titulo)
                .setMessage(Mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

