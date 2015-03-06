package proyecto.proyectobookit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class ModoLista extends Activity {

    // Declare Variables
    ListView lista_Pines;
    ListViewAdapter_ModoLista adapter;

    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Buscar = null;
    private Usuario Mi_Usuario = new Usuario();

    private MetodosUtiles M_Utiles = new MetodosUtiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_lista);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        lista_Pines = (NestedListView) findViewById(R.id.modo_lista_listapines);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Mi_Usuario.setId_usuario(extras.getString("id_usuario"));
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

        EditText_Search = (EditText) Menu_SearchItem.getActionView().findViewById( R.id.search );
        Button_Buscar = (Button) Menu_SearchItem.getActionView().findViewById(R.id.buscar);
        Button_Buscar.setVisibility( EditText_Search.getText().length() > 0 ? View.VISIBLE : View.GONE );

        adapter = new ListViewAdapter_ModoLista(this,EditText_Search);
        lista_Pines.setAdapter(adapter);
        EditText_Search.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                Button_Buscar.setVisibility( s.length() > 0 ? View.VISIBLE : View.INVISIBLE );
            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count,
                                           int after )
            {
            }

            @Override
            public void afterTextChanged( Editable s )
            {
                Button_Buscar.setVisibility( s.length() > 0 ? View.VISIBLE : View.GONE );

                String Aux = EditText_Search.getText().toString();
                if(Aux.contains("\n")){
                    Aux = Aux.replace("\n","");
                    EditText_Search.setText(Aux);
                    String text = EditText_Search.getText().toString().toLowerCase(Locale.getDefault());

                    String Url = "http://pinit-api.herokuapp.com/pins";
                    try {
                        Url += URLEncoder.encode(text, "UTF-8") + ".json";
                        adapter.ColocarPines();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );

        if(Mi_Usuario.getId_usuario() !=null){
            String Url = "http://pinit-api.herokuapp.com/pins.json";
            adapter.ColocarPines(Url);
        }

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
