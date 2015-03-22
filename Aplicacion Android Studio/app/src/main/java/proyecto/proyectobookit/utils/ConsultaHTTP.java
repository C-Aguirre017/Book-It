package proyecto.proyectobookit.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isseu on 22-03-15.
 */
public class ConsultaHTTP {

    // Constantes
    private final static String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)";

    // Variables estaticas
    private static HttpsURLConnection connection;

    // Ultimos Datos
    public static int response_code;
    public static String resultString;

    public static String POST(String url, Hashtable<String, String> params) throws Exception {
        StringBuffer urlParameters = new StringBuffer();
        URL u = new URL(url);
        connection = (HttpsURLConnection) u.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.toString().getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // Unimos parametros en una mismo string
        Iterator<String> paramIterator = params.keySet().iterator();
        while (paramIterator.hasNext()) {
            String key = paramIterator.next();
            String value = params.get(key);
            urlParameters.append(URLEncoder.encode(key, "UTF-8"));
            urlParameters.append("=").append(URLEncoder.encode(value, "UTF-8"));
            urlParameters.append("&");
        }

        // Enviamos parametros
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(urlParameters.toString());
        writer.flush();

        response_code = connection.getResponseCode();

        // receive response as inputStream
        InputStream inputStream = connection.getInputStream();
        // convert inputstream to string
        resultString = null;
        if(inputStream != null) {
            resultString = convertInputStreamToString(inputStream);
        }
        disconnect();

        return resultString;
    }

    public static String GET(String url) {
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

    /**
     * Closes the connection if opened
     */
    public static void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        StringBuffer result = new StringBuffer();
        while((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        inputStream.close();
        return result.toString();

    }
}
