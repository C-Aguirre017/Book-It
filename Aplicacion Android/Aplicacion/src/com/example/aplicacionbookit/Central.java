package com.example.aplicacionbookit;

import com.google.android.gms.maps.GoogleMap;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Central extends ActionBarActivity {
	
	Fragment FragmentoActual;
	
	Fragment_Mapa Mapa_Fragmento;
	Help	Ayuda;
	GoogleMap Mapas;
	
	RelativeLayout leftRL;
	DrawerLayout drawerLayout;
	
	 private ListView ListaOpciones;
	 private String[] NombreOpciones = {"Crear Pin","Como Funciona ?","Cerrar Sesion"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_central);
		
		leftRL = (RelativeLayout)findViewById(R.id.LeftDrawer);
	    drawerLayout = (DrawerLayout)findViewById(R.id.activity_central2);
	    
	    Ayuda = new Help();
	    
	    ListaOpciones = (ListView)findViewById(R.id.ListaOpcionesCentral);
	    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NombreOpciones);
	    ListaOpciones.setAdapter(adaptador);
	    ListaOpciones.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            	
            	if(position ==0){
            		Toast.makeText(getApplicationContext(), "Para crear un Pin deje apretado en el mapa el lugar en donde quiere la clase", Toast.LENGTH_SHORT).show();
            	}
            	else if(position ==1){
            		// Esto es Help
            		Intent actoHelp = new Intent(Central.this,Help.class);
            		startActivity(actoHelp);
            	}
            	else if(position ==2){
            		// Esto es Help
            		Intent actoHelp = new Intent(Central.this,Help.class);
            		startActivity(actoHelp);
            	}
            	else {
            	}
            		 Central.this.finish();
            }
        });
	    
	    FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();	
		Mapa_Fragmento = new Fragment_Mapa();
		ft.add(R.id.EspacioCambia, Mapa_Fragmento);
		ft.commit();		
	}	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barra, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.drawer_icon) {
				onLeft(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


    public  void onLeft(View view)
    {
    	if(drawerLayout!=null && leftRL !=null)
    		drawerLayout.openDrawer(leftRL);
    }
   

}
