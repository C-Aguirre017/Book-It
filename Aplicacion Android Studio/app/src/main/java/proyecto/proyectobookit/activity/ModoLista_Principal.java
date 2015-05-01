package proyecto.proyectobookit.activity;

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

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.adapters.NestedListView;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.model_adapters.ListViewAdapter_ModoLista;
import proyecto.proyectobookit.utils.Configuracion;

public class ModoLista_Principal extends Activity {

    private MenuItem Menu_SearchItem = null;
    private EditText EditText_Search = null;
    private Button Button_Buscar = null;

    ListView lista_Pines;
    ListViewAdapter_ModoLista adapter;

    private Usuario Mi_Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_lista);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        lista_Pines = (NestedListView) findViewById(R.id.modo_lista_listapines);

        Mi_Usuario = Usuario.getUsuarioActual();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modo_lista, menu);

        Menu_SearchItem = menu.findItem(R.id.modo_lista_search);

        EditText_Search = (EditText) Menu_SearchItem.getActionView().findViewById( R.id.search );
        Button_Buscar = (Button) Menu_SearchItem.getActionView().findViewById(R.id.buscar);
        Button_Buscar.setVisibility( EditText_Search.getText().length() > 0 ? View.VISIBLE : View.GONE );
        Button_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualizar_Boton();
            }
        });

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
                    Actualizar_Boton();
                }
            }
        } );

        if(Mi_Usuario.getId_usuario() !=null){
            adapter.Colocar();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.modo_lista_actualizar) {
            Actualizar_Boton();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Actualizar_Boton(){
        String text = EditText_Search.getText().toString().toLowerCase(Locale.getDefault());
        String Url = Configuracion.URLSERVIDOR + "/pins";
        try {
            Url += URLEncoder.encode(text, "UTF-8") + ".json";
            adapter.Actualizar_ColocarPines(Url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
