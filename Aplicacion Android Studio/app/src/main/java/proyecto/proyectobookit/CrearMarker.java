package proyecto.proyectobookit;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class CrearMarker extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Aceptar(View v){

        EditText titulo_ramo = (EditText) findViewById(R.id.crear_marker_nombreramo);
        EditText descripcion = (EditText) findViewById(R.id.crear_marker_descripcion);


        Intent returnIntent = new Intent();
        returnIntent.putExtra("ramo",titulo_ramo.getText().toString());
        returnIntent.putExtra("descripcion", descripcion.getText().toString());
        setResult(RESULT_OK,returnIntent);
        finish();

    }

}
