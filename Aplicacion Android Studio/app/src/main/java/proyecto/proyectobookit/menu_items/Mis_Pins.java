package proyecto.proyectobookit.menu_items;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import proyecto.proyectobookit.model_adapters.ListViewAdapter_VerPins;
import proyecto.proyectobookit.MetodosUtiles;
import proyecto.proyectobookit.adapters.NestedListView;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Usuario;

public class Mis_Pins extends Activity {

    // Declare Variables
    ListView lista_Pines;
    ListViewAdapter_VerPins adapter;

    private MenuItem Menu_SearchItem = null;
    private Usuario Mi_Usuario = new Usuario();

    private MetodosUtiles M_Utiles = new MetodosUtiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis__pins);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        lista_Pines = (NestedListView) findViewById(R.id.mis_pins_lista);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Mi_Usuario.setId_usuario(extras.getString("id_usuario"));
            Mi_Usuario.setEmail(extras.getString("email"));
            Mi_Usuario.setToken(extras.getString("token"));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mis__pins, menu);

        Menu_SearchItem = menu.findItem(R.id.mis_pins_search);

        adapter = new ListViewAdapter_VerPins(this,null,Mi_Usuario);
        lista_Pines.setAdapter(adapter);

        if(Mi_Usuario.getId_usuario() !=null){
            String Url = "http://pinit-api.herokuapp.com/usuarios/";

            try {
                Url += URLEncoder.encode(Mi_Usuario.getId_usuario(),"UTF-8") +"/pins.json";
                adapter.ColocarPines(Url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Menu_SearchItem.setVisible(false);

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

    public void actualizar_buscar( View v ){

    }
}
