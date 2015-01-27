package com.example.aplicacionbookit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Mapa extends Fragment  implements OnMapClickListener, OnMapLongClickListener,OnMarkerClickListener {
	
	GoogleMap Mapas;	
	LatLng point;

	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//		  super.onCreate(savedInstanceState);
		  View v =inflater.inflate(R.layout.fragment_mapa,container,false);
		  
		  Mapas = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		  Mapas.setMyLocationEnabled(true);
		  Mapas.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		  Mapas.setOnMarkerClickListener(this);
		  Mapas.setOnMapLongClickListener(this);
		 
		  return v;	  
	  }
	  
	  public GoogleMap getMapas() {
		  return Mapas;
	  }

	@Override
	public void onStart() {
	      super.onStart();
	      // Connect the client.
	      if(Mapas.getMyLocation()!=null){
			  LatLng latLng = new LatLng(Mapas.getMyLocation().getLatitude(), Mapas.getMyLocation().getLongitude());
			  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			  Mapas.animateCamera(cameraUpdate);
	      }
	 }  
	  
	@Override
	public boolean onMarkerClick(Marker marker) {

		String nombre_ramo = marker.getTitle();
		String descripcion = marker.getSnippet();
		
	      // EditText Cambiando_Descripcion = (EditText) getView().findViewById(R.id.dialog_completar);
	      // Cambiando_Descripcion.setText(descripcion.toString());
	
			  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
			  //LayoutInflater inflater = getActivity().getLayoutInflater();  			  
			 // builder.setView(inflater.inflate(R.layout.dialog_layout, null))
			  builder.setTitle(nombre_ramo)
			  		 .setMessage(
			  				 " Descripcion: \n	"+ descripcion + "\n Persona: \n	" + "Tu"
			  		 
			  				 )
		      		 .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // Send the positive button event back to the host activity
		                      
		                   }
		               })
		             .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // Send the negative button event back to the host activity
		                      
		                   }
		               });
		       AlertDialog alert = builder.create();
		       
		     //Agregar la Info al Layer del Dialog_layout
		    
		       alert.show();

		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		
		this.point = point;
		Intent i = new Intent(getActivity(), CrearMarker.class);
		startActivityForResult(i, 1);	
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub

	}

	public void Botar() {
		// TODO Auto-generated method stub		
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if(requestCode == 1 && resultCode == Activity.RESULT_OK ) {
	       //some code
	    	String ramo = data.getStringExtra("ramo");
	    	String descripcion = data.getStringExtra("descripcion");
	    	
			Marker NuevoMarcador = Mapas.addMarker(new MarkerOptions()
		       			.position(point)
		       			.title(ramo)
		       			.snippet(descripcion)
		       			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		       			.draggable(false)
					);
	    }
	 }
	

	
}