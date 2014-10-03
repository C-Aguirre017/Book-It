package com.example.aplicacionbookit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//		  super.onCreate(savedInstanceState);
		  View v =inflater.inflate(R.layout.fragment_mapa,container,false);
		  
		  Mapas = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		  Mapas.setMyLocationEnabled(true);
		  Mapas.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {
//		// TODO Auto-generated method stub
		
		Intent act = new Intent(this.getActivity(),CrearMarker.class);
		startActivity(act);  
		 Marker NuevoMarcador = Mapas.addMarker(new MarkerOptions()
       		.position(point)
       		.title("Nuevo Marker")
       		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
       );	 
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub

	}

	public void Botar() {
		// TODO Auto-generated method stub

		
	}
}