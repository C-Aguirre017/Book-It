package com.example.aplicacionbookit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Help extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		android.app.ActionBar bariita= getActionBar();
		bariita.hide();		

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
