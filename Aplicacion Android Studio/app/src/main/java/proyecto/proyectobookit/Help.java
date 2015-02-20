package proyecto.proyectobookit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Help extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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

    	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	  if (keyCode == KeyEvent.KEYCODE_BACK) {

	  /*  new AlertDialog.Builder(this)
	      .setIcon(android.R.drawable.ic_dialog_alert)
	      .setTitle("Salir")
	      .setMessage("Estás seguro?")
	      .setNegativeButton(android.R.string.cancel, null)//sin listener
	      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
	        @Override
	        public void onClick(DialogInterface dialog, int which){
	          //Salir

	        }
	      })
	      .show();
		  this.finish();

	    // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
	    return true;
	  }
	//para las demas cosas, se reenvia el evento al listener habitual
	  return super.onKeyDown(keyCode, event);
	}
	*/


    public void Prueba(View v){
        this.finish();
    }
}
