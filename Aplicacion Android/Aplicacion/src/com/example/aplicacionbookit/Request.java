package com.example.aplicacionbookit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class Request {
	
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
}
