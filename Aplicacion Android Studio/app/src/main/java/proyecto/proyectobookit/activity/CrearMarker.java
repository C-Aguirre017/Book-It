package proyecto.proyectobookit.activity;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import proyecto.proyectobookit.adapters.CrearMarker_ListViewAdapter_VerRamos;
import proyecto.proyectobookit.adapters.NestedListView;


public class CrearMarker extends Activity {

    // Declare Variables
    ListView lista_ramos;
    CrearMarker_ListViewAdapter_VerRamos adapter;
    EditText editsearch;
    public static TextView hora_label;
    public static TextView dia_label;

    MetodosUtiles M_Utiles = new MetodosUtiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marker);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        lista_ramos = (NestedListView) findViewById(R.id.crearmarker_list);
        editsearch = (EditText) findViewById(R.id.crearmarker_search);

        adapter = new CrearMarker_ListViewAdapter_VerRamos(this,editsearch);
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


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        hora_label = (TextView)findViewById(R.id.crearmarker_hora);
        hora_label.setText("" + hour + ":" + minute);
        hora_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog_hora(v);
            }
        });

        dia_label = (TextView)findViewById(R.id.crearmarker_diaelegido);
        dia_label.setText("" + year + "/" + month + "/" + day);
        dia_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog_dia(v);
            }
        });

        DiaSeleccionado.set(year,month,day,hour,minute);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

            if(adapter.getElegido()!=null) {
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
                        .setMessage(M_Utiles.CrearMensaje(descripcion,hora, precio,"clases"))
                        .setPositiveButton("Crear Marker", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Salir(finalid_ramo, finalDescripcion, finalPrecio, finalCampus, finalTitulo,finalHora);
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

    private static Calendar DiaSeleccionado = new GregorianCalendar();

    public void showTimePickerDialog_hora(View v) {
        DialogFragment dia_fragment = new TimePickerFragment();
        dia_fragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog_dia(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
               int hour = DiaSeleccionado.get(Calendar.HOUR_OF_DAY);
               int minute = DiaSeleccionado.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            hora_label.setText("" + hourOfDay + ":" + minute);
            DiaSeleccionado.set(DiaSeleccionado.get(Calendar.YEAR), DiaSeleccionado.get(Calendar.MONTH),DiaSeleccionado.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this,DiaSeleccionado.get(Calendar.YEAR), DiaSeleccionado.get(Calendar.MONTH),DiaSeleccionado.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            int mes_aux = month +1;
            dia_label.setText("" + year + "/" + mes_aux + "/" + day);
            DiaSeleccionado.set(year,month,day,DiaSeleccionado.get(Calendar.HOUR_OF_DAY),DiaSeleccionado.get(Calendar.MINUTE));
        }
    }

}