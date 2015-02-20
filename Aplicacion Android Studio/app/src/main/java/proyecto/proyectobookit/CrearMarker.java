package proyecto.proyectobookit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;
import java.util.Locale;


public class CrearMarker extends Activity {

    // Declare Variables
    ListView lista_ramos;
    ListViewAdapter adapter;
    EditText editsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marker);

        lista_ramos = (ListView) findViewById(R.id.crearmarker_list);
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

        //Unidad Academica
        Spinner dropdown_ua = (Spinner)findViewById(R.id.crearmarker_unidadacademica);
        String[] items = new String[]{"Actuación", "Agronomía", "Arquitectura","Arte","Ciencias Biológicas","Cursos Deportivos","Derecho","Ciencias Económicas y Administrativas","Educación","Enfermería","Física","Geografía","Ingeniería","Matemática","Música","Odontología","Química","Ciencias de la Salud/Medicina","Teología","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown_ua.setAdapter(adapter);


        //Facultad
        Spinner dropdown_campus = (Spinner)findViewById(R.id.crearmarker_facultad);
        String[] items_campus = new String[]{"Campus Externo","Casa Central","Lo Contador","Oriente","San Joaquin","Villarica"};
        ArrayAdapter<String> adapter_campus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items_campus);
        dropdown_campus.setAdapter(adapter_campus);
    }

    public void Aceptar(View v){

        String unidadacademica="",descripcion="",precio="",campus="";

        Spinner get_unidad_academica = (Spinner) findViewById(R.id.crearmarker_unidadacademica);
        EditText get_descripcion = (EditText) findViewById(R.id.crearmarker_descripcion);
        EditText get_precio = (EditText) findViewById(R.id.crearmarker_precio);
        Spinner get_campus = (Spinner) findViewById(R.id.crearmarker_facultad);

        unidadacademica = get_unidad_academica.getSelectedItem().toString();
        descripcion = get_descripcion.getText().toString();
        precio = get_precio.getText().toString();
        campus = get_campus.getSelectedItem().toString();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("ramo",editsearch.getText().toString());
        returnIntent.putExtra("descripcion", descripcion);
        returnIntent.putExtra("unidadacademica",unidadacademica );
        returnIntent.putExtra("precio",precio );
        returnIntent.putExtra("campus",campus );

        setResult(RESULT_OK,returnIntent);
        this.onDestroy();
        this.finish();
    }

}

