package com.example.aplicacionbookit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Registro extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		android.app.ActionBar bariita= getActionBar();
//		bariita.hide();
	}
	
	 public void Aceptar(View v){
	    	this.finish();
	    }
}
