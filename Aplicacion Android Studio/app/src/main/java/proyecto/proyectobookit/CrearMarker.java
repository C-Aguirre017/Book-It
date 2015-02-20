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

        Spinner dropdown = (Spinner)findViewById(R.id.crearmarker_facultad);
        String[] items = new String[]{"Actuación", "Agronomía", "Arquitectura","Arte","Ciencias Biológicas","Cursos Deportivos","Derecho","Ciencias Económicas y Administrativas","Educación","Enfermería","Física","Geografía","Ingeniería","Matemática","Música","Odontología","Química","Ciencias de la Salud/Medicina","Teología","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

    }

    public void Aceptar(View v){

        //EditText descripcion = (EditText) findViewById(R.id.crear_marker_descripcion);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("ramo",editsearch.getText().toString());
        returnIntent.putExtra("descripcion", "");
        setResult(RESULT_OK,returnIntent);

        //Matar Threads
        List<AsyncTask> Lista = adapter.getLista_AssyncTask();
        for (int i=0; i < Lista.size();i++){
                Lista.get(i).cancel(true);
        }

        this.onDestroy();
        this.finish();
    }

}

