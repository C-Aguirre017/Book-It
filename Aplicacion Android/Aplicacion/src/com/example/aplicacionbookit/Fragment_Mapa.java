package com.example.aplicacionbookit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.la;
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
		  
		  LatLng latLng = new LatLng(-33.498444, -70.611722);
		  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
		  Mapas.animateCamera(cameraUpdate);
		 
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
	      
	      //Pedir todos los Pins
	      	//Aca hay q filtar segun lo q se busca por ejemplo si se busca ramo calculo II poner algo 
	      new HttpAsyncTask().execute("http://pinit-api.herokuapp.com/pins.json");
	 }  
	  
	@Override
	public boolean onMarkerClick(Marker marker) {

		String nombre_ramo = marker.getTitle();
		String descripcion = marker.getSnippet();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
		builder.setTitle(nombre_ramo)
				.setMessage(
			  				 " Descripcion: \n	"+ descripcion)
			  	.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int id) {
		                   }
		               })
	           .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                   }
		               });
		AlertDialog alert = builder.create();  
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
	
 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if(requestCode == 1 && resultCode == Activity.RESULT_OK ) {
	       //some code
	    	String ramo = data.getStringExtra("ramo");
	    	String descripcion = data.getStringExtra("descripcion");
	    	
	    	//Obtener Fecha y Hora 
	    	Long tsLong = System.currentTimeMillis()/1000;
	    	String ts = tsLong.toString();
	    	
	    	String Mensaje = point.latitude+ "," +point.longitude+ "," +0+ "," +ramo+ "," +descripcion + "," + ts;
	    	
	    	new AsyncTask_PostMarker().execute(Mensaje);

	    }
	 }
 
 
 // Metodos Assync Task
	public boolean postData_Pins(double lat, double lon , double precio, String titulo, String Descripcion, String publicacion) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://pinit-api.herokuapp.com/pins");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("latitude",""+lat));
	        nameValuePairs.add(new BasicNameValuePair("longitude",""+lon));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	       
	        
	        return true;
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	return false;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	return false;
	    }
	} 
	
	public boolean postData_Pins(String Mensaje) {
		
		String[] Aux = Mensaje.split(",");
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://pinit-api.herokuapp.com/pins");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("latitud",Aux[0]));
	        nameValuePairs.add(new BasicNameValuePair("longitud",Aux[1]));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	       
	        
	        return true;
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	return false;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	return false;
	    }
	} 
	
	public String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
	
    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
	
	
	
	//AsyncTasks
	
 	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... urls) {
	        	 return GET(urls[0]);
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        
	        @Override
	        protected void onPostExecute(String result) {            

				result = "{ \"pins\":" + result +   "}" ;
				try {
					JSONObject json = new JSONObject(result); 
					JSONArray articles = json.getJSONArray("pins"); 	
					for (int i = 0; i < articles.length(); i++) {
							double lat=-1,lon=-1, precio=-1;
							String Titulo=null,Descripcion=null,Facultad=null,Publicacion=null;
							
							//Encontramos los valores
							try{lat = Double.parseDouble(articles.getJSONObject(i).getString("latitude"));}catch(Exception e){}
							try{lon = Double.parseDouble(articles.getJSONObject(i).getString("longitude"));}catch(Exception e){}
							try{Titulo = articles.getJSONObject(i).getString("titulo");}catch(Exception e){}
							try{Descripcion = articles.getJSONObject(i).getString("descripcion");}catch(Exception e){}
							try{Publicacion = articles.getJSONObject(i).getString("publicacion");}catch(Exception e){}
							try{precio = Double.parseDouble(articles.getJSONObject(i).getString("precio"));}catch(Exception e){}
							
							//Creando Mensaje
							String Mensaje;
							if(Descripcion !=null)
								Mensaje= Descripcion;
							else
								Mensaje= "No hay";
							
							if (Publicacion !=null) {
								Mensaje += "\n Fecha Creación: \n	" + Publicacion;	
							}
							else {
								Mensaje += "\n Fecha Creación: \n	No hay";
							}
							
							if(	precio !=-1){
								Mensaje += "\n Dispuesto a Pagar: \n	"  + precio; 
							}else{
								Mensaje+= "\n Dispuesto a Pagar: \n	No hay" ;
							}
								
							//Inserta el Marker
							if(lat != -1 && lon != -1){
								LatLng lugar = new LatLng(lat,lon);
								Mapas.addMarker(new MarkerOptions()
					    			.position(lugar)
					    			.title(Titulo)
					    			.snippet(Mensaje)
					    			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
					    			.draggable(false)
									);
							}
							
						}			
				} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity().getBaseContext(),"Error al conectar con la Base de datos", Toast.LENGTH_LONG).show();
				}
	       }
 	}
 
 	private class AsyncTask_PostMarker extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return postData_Pins(params[0]);
		}
 		
 		
 	}
}