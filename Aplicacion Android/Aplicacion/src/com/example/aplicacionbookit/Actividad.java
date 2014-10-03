package com.example.aplicacionbookit;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Actividad extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad);
		
		android.app.ActionBar bariita= getActionBar();
		bariita.hide();
	}
	
	public void BotonRegistrarse(View v){
		Intent Registro = new Intent(this,Registro.class);
		startActivity(Registro);
	}
	
	public void BotonLogin(View v ) {
		Intent Login = new Intent(this,Central.class);
		startActivity(Login);
	}
}
