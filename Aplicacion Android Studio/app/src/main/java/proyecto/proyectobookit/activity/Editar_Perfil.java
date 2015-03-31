package proyecto.proyectobookit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Hashtable;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class Editar_Perfil extends Activity {

    private TextView Nombre;
    private EditText Carrera, Biografia;
    private Spinner Universidad_Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //Cambiar

        Nombre= (TextView) findViewById(R.id.editar_perfil_nombre);
        Carrera= (EditText) findViewById(R.id.editar_perfil_carrera);
        //Universidad= (EditText) findViewById(R.id.editar_perfil_Universidad);
        Biografia= (EditText) findViewById(R.id.editar_perfil_biografíaAcademica);
        Universidad_Spinner = (Spinner) findViewById(R.id.editar_perfil_dropdownUniversidad);

        Nombre.setText(Usuario.getUsuarioActual().getNombre());
        Carrera.setText(Usuario.getUsuarioActual().getCarrera());
        //Universidad.setText(Usuario.getUsuarioActual().getUniversidad());
        Biografia.setText(Usuario.getUsuarioActual().getBiografia());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.universidades));
        Universidad_Spinner.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar__perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editar_perfil_send) {
            Confirmar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Aceptar(View view) {
        Confirmar();
    }

    private void Confirmar() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¿Estas seguro?")
                    .setMessage("Nombre:" +
                            " \n \t " + Nombre.getText().toString() +
                            " \n Universidad:" +
                            " \n \t " + Universidad_Spinner.getSelectedItem().toString() +
                            " \n Carrera:" +
                            " \n \t " + Carrera.getText().toString() +
                            " \n Biografía:" +
                            " \n \t " + Biografia.getText().toString()
                    )
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Actualizar();
                        }
                    })
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            Toast.makeText(this,"Error",Toast.LENGTH_LONG);
        }
    }

    private void Actualizar() {
        (new AsyncTask<String, Void, String>() {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getBaseContext(), "Modificando Información ...", "Espere porfavor", true, false);
            }

            @Override
            protected String doInBackground(String... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(String result) {
                progressDialog.dismiss();
            }


        }).execute(Nombre.getText().toString(),Carrera.getText().toString(),Universidad_Spinner.getSelectedItem().toString(),Biografia.getText().toString());
    }

}
