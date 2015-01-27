package com.example.aplicacionbookit;

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
