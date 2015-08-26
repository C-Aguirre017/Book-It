package proyecto.proyectobookit.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import proyecto.proyectobookit.Central;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.model_adapters.ListViewAdapter_CrearMarker;
import proyecto.proyectobookit.nav_drawner.NestedListView;
import proyecto.proyectobookit.utils.AlertDialogMetodos;


public class CrearMarker extends Activity {

    // Declare Variables
    static TextView hora_label, dia_label;
    Calendar DiaSeleccionado = new GregorianCalendar();
    ListViewAdapter_CrearMarker adapter;
    ListView lista_ramos;
    EditText editsearch;
    DatePicker datePicker;
    TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marker);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        lista_ramos = (NestedListView) findViewById(R.id.crearmarker_list);
        editsearch = (EditText) findViewById(R.id.crearmarker_search);
        datePicker = (DatePicker) findViewById(R.id.crear_marker_datePicker);
        timePicker = (TimePicker) findViewById(R.id.crear_marker_timePicker);

        adapter = new ListViewAdapter_CrearMarker(this,editsearch);
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

    public void Aceptar(View v) {

        //Colocar Date Picker
        DiaSeleccionado.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());

        if (VerificarEscrito()) {
            String descripcion = "", precio = "", campus = "", titulo = "", id_ramo = "", hora = "";
            EditText get_descripcion = (EditText) findViewById(R.id.crearmarker_descripcion);
            EditText get_precio = (EditText) findViewById(R.id.crearmarker_precio);

            if (adapter.getElegido() != null) {
                id_ramo = adapter.getElegido().getId_ramo();
                titulo = adapter.getElegido().getSigla() + " " + adapter.getElegido().getNombre();
                descripcion = get_descripcion.getText().toString();
                precio = get_precio.getText().toString();
                hora = (new Timestamp(DiaSeleccionado.getTimeInMillis())).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final String finalDescripcion = descripcion;
                final String finalPrecio = precio;
                final String finalCampus = campus;
                final String finalid_ramo = id_ramo;
                final String finalTitulo = titulo;
                final String finalHora = hora;
                builder.setTitle(titulo)
                        .setMessage(AlertDialogMetodos.crearInfoPin(descripcion, hora, precio, "clases"))
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
            } else {
                CrearAlertDialog("Porfavor seleccione un curso de la lista", "Error de Curso");
            }
        }
    }

    private void Salir(String id_ramo, String descripcion, String precio, String campus, String titulo, String finalHora) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("id_ramo",id_ramo);
        returnIntent.putExtra("descripcion", descripcion);
        returnIntent.putExtra("titulo",titulo);
        returnIntent.putExtra("precio", precio);
        returnIntent.putExtra("hora", finalHora);

        setResult(RESULT_OK, returnIntent);
        this.onDestroy();
        this.finish();
    }

    private boolean VerificarEscrito() {

        //Ramo
        EditText Aux_Ramo = (EditText) findViewById(R.id.crearmarker_search);
        if(Aux_Ramo!=null) {
            if (Aux_Ramo.getText().toString().length() == 0) {
                CrearAlertDialog("La opcion curso se encuentra vacia", "Faltan Datos");
                return false;
            }
        } else {
            return false;
        }

        //Precio
        EditText Aux_Precio = (EditText) findViewById(R.id.crearmarker_precio);
        if(Aux_Precio!=null) {
            if (Aux_Precio.getText().toString().length() == 0) {
                CrearAlertDialog("La opcion precio se encuentra vacia", "Faltan Datos");
                return false;
            }
        } else {
            return false;
        }


        //Dia Antes que ahora
        Date Actual = new Date();
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Actual.setHours(hour); Actual.setMinutes(minute);Actual.setYear(year);Actual.setMonth(month);Actual.setDate(day);
        if(DiaSeleccionado.before(Actual)){
            CrearAlertDialog("La fecha escogida no es valida", "Datos Incorrectos");
            return false;}
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