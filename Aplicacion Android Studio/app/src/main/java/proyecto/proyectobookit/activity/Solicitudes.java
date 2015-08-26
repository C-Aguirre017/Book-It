package proyecto.proyectobookit.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.fragment.SolicitudesFragment;

public class Solicitudes extends Activity {

    private String pin_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        //Agarrar Extra
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pin_id = null;
            } else {
                pin_id = extras.getString("pin_id");
            }
        } else {
            pin_id= (String) savedInstanceState.getSerializable("pin_id");
        }

        SolicitudesFragment SolicitudesFragment = new SolicitudesFragment(pin_id,this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.solicitudes_container, SolicitudesFragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solicitudes, menu);
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
        else if(id ==  android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
