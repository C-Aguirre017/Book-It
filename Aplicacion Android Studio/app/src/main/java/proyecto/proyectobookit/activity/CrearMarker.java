package proyecto.proyectobookit.activity;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
        DiaSeleccionado.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        if (verificarEscrito()) {
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
                crearAlertDialog("Porfavor seleccione un curso de la lista", "Error de Curso");
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

    private boolean verificarEscrito() {

        //Ramo
        EditText Aux_Ramo = (EditText) findViewById(R.id.crearmarker_search);
        if(Aux_Ramo!=null) {
            if (Aux_Ramo.getText().toString().length() == 0) {
                crearAlertDialog("La opcion curso se encuentra vacia", "Faltan Datos");
                return false;
            }
        } else {
            return false;
        }

        //Precio
        EditText Aux_Precio = (EditText) findViewById(R.id.crearmarker_precio);
        if(Aux_Precio!=null) {
            if (Aux_Precio.getText().toString().length() == 0) {
                crearAlertDialog("La opcion precio se encuentra vacia", "Faltan Datos");
                return false;
            }
        } else {
            return false;
        }


        //Dia Antes que ahora
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH),day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);

        if(DiaSeleccionado.getTime().getYear()<=year)
            if(DiaSeleccionado.getTime().getMonth()<=month)
                if(DiaSeleccionado.getTime().getDate()< day) {
                        crearAlertDialog("El dia escogido no es valido", "Datos Incorrectos");
                        return false;
                }
                else if(DiaSeleccionado.getTime().getDate() == day) {
                    if (DiaSeleccionado.getTime().getHours() < (hour+14)) {
                        crearAlertDialog("La hora escogida no es valida. Se necesitan de Mínimo 2 hrs de diferencia", "Datos Incorrectos");
                        return false;
                    }
                }

        return true;
    }

    private void crearAlertDialog(String Mensaje, String Titulo){

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