package proyecto.proyectobookit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;

public class Editar_Perfil extends Activity {

    private TextView Nombre;
    private EditText Carrera,Biografia,Telefono;
    private Spinner Universidad_Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Nombre= (TextView) findViewById(R.id.editar_perfil_nombre);
        Carrera= (EditText) findViewById(R.id.editar_perfil_carrera);
        Biografia= (EditText) findViewById(R.id.editar_perfil_biografíaAcademica);
        Telefono = (EditText) findViewById(R.id.editar_perfil_telefono);

        //Colocar Universidades
        Universidad_Spinner = (Spinner) findViewById(R.id.editar_perfil_dropdownUniversidad);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.universidades));
        Universidad_Spinner.setAdapter(adapter);

        //Verificar
        verificarInfo();
    }

    private void verificarInfo(){
        if(Usuario.getUsuarioActual().getNombre() != null) {
            if(!Usuario.getUsuarioActual().getNombre().equals("null")) {
                Nombre.setText(Usuario.getUsuarioActual().getNombre());
            }
        }

        if(Usuario.getUsuarioActual().getCarrera()!= null) {
            if(!Usuario.getUsuarioActual().getCarrera().equals("null")) {
                Carrera.setText(Usuario.getUsuarioActual().getCarrera());
            }
        }

        if(Usuario.getUsuarioActual().getBiografia() !=null){
            if(!Usuario.getUsuarioActual().getBiografia().equals("null")){
                Biografia.setText(Usuario.getUsuarioActual().getBiografia());
            }
        }

        if(Usuario.getUsuarioActual().getTelefono() !=null){
            if(!Usuario.getUsuarioActual().getTelefono().equals("null")){
                Telefono.setText(Usuario.getUsuarioActual().getTelefono());
            }
        }
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

    //Actualizar

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
                            " \n Telefono:" +
                            " \n \t " + Telefono.getText().toString() +
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
                //progressDialog = ProgressDialog.show(getApplicationContext(), "Modificando Información ...", "Espere porfavor", true, false);
            }

            @Override
            protected String doInBackground(String... params) {
                return Actualizar_Usuario(params[0],params[1],params[2],params[3],params[4]);
            }

            @Override
            protected void onPostExecute(String result) {
                try{
                    int x = 0;
                }
                catch (Exception e){

                }
                Usuario.cargarDatos(Usuario.getUsuarioActual(),result);
                //progressDialog.dismiss();
            }


            private String Actualizar_Usuario(String neoNombre, String neoCarrera, String neoUniv, String neoBiografia,String neoTelefono){

                try {
                    Hashtable<String, String> rparams = new Hashtable<String, String>();
                    rparams.put("user_id",Usuario.getUsuarioActual().getId_usuario());
                    rparams.put("user_token",Usuario.getUsuarioActual().getToken());
                    rparams.put("user[name]",neoNombre);
                    rparams.put("user[profession]",neoCarrera);
                    rparams.put("user[phone]",neoTelefono);
                    rparams.put("user[biography]", neoBiografia);

                    return ConsultaHTTP.PUT(Configuracion.URLSERVIDOR + "/users/" + Usuario.getUsuarioActual().getId_usuario() +".json", rparams);

                } catch (Exception e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

        }).execute(Nombre.getText().toString(),Carrera.getText().toString(),Universidad_Spinner.getSelectedItem().toString(),Biografia.getText().toString(),Telefono.getText().toString());
    }

}
