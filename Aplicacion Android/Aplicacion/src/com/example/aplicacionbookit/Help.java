package com.example.aplicacionbookit;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Help extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		android.app.ActionBar bariita= getActionBar();
		bariita.hide();
			
		Request Obtener = new Request();
		String resp = Obtener.Obtener_Datos("oli");
		
		TextView o = (TextView)findViewById(R.id.probando);
		o.setText(resp);
		
	}
}
