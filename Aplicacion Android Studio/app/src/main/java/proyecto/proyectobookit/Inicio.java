package proyecto.proyectobookit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.utils.Configuracion;
import proyecto.proyectobookit.utils.ConsultaHTTP;


public class Inicio extends FragmentActivity {

    private LoginButton authButton;
    private ProgressBar spinner;

    private UiLifecycleHelper uiHelper;
    private SharedPreferences sharedPref;

    private String KEY_LOGIN = "ingresado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO sacar despues
        // printKeyHash(this);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("config", MODE_PRIVATE);

        if (yaHaIngresado()) {
            Log.d("Mensaje", "Login saltado");
            seguirCentro();
        }

        setContentView(R.layout.activity_inicio);

        spinner = (ProgressBar)findViewById(R.id.progressBarUserLoading);

        authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(KEY_LOGIN, true);
                    editor.commit();
                } else {
                    Log.d("MainActivity", "Nop.");
                }
            }
        });

    }

    public void existeUsuario(final String fbUid) {
        (new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... fbUid) {
                return ConsultaHTTP.GET(Configuracion.URLSERVIDOR + "/usuarios/fb/" + fbUid[0] + ".json");
            }

            @Override
            protected void onPostExecute(String result) {
                // Depende si existe o no usuario
                // Si existe solo cargamos datos
                try {
                    // Si no existe debemos crearlo
                    if (ConsultaHTTP.response_code == 404) {
                        Log.d("Informacion", "Usuario no existe, lo creamos");
                        crearUsuario(Usuario.getUsuarioActual().getEmail(), Usuario.getUsuarioActual().getFbUid(), Usuario.getUsuarioActual().getToken());
                        return;
                    }
                    // Existe, cargamos informacion
                    Log.d("Informacion", "Cargando informacion de usuario existente");
                    Usuario.cargarDatos(Usuario.getUsuarioActual(), result);
                    seguirCentro();
                }catch (Exception e) {

                }
            }
        }).execute(fbUid);
    }

    public void crearUsuario(String email, String uid, String fbsecret ) {
        (new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Hashtable<String, String> rparams = new Hashtable<String, String>();
                rparams.put("email", params[0]);
                rparams.put("uid", params[1]);
                rparams.put("fbsecrettoken", params[2]);
                try {
                    Log.d("Informacion", "Creando");
                    return ConsultaHTTP.POST(Configuracion.URLSERVIDOR + "/usuarios/registar_movil.json", rparams);
                }catch(Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // Necesatiramos cargar datos
                // Seguir con el programa
                Log.d("Informacion", "Creado!");
                Usuario.cargarDatos(Usuario.getUsuarioActual(), result);
                seguirCentro();
            }
        }).execute(email, uid, fbsecret );
    }

    public void mostrarLoading() {
        authButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }

    private boolean yaHaIngresado() {
        return false;// return sharedPref.getBoolean(KEY_LOGIN, false);
    }

    private void seguirCentro() {
        Intent app = new Intent(Inicio.this, Central.class);
        startActivity(app);
        finish();
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                // Vemos si el usuario ya existe
                Usuario.getUsuarioActual().setFbSession(Session.getActiveSession());
                Request request =  Request.newMeRequest(Usuario.getUsuarioActual().getFbSession(), new Request.GraphUserCallback() {
                    public void onCompleted(GraphUser user, Response response) {

                        if (user != null) {
                            Usuario.getUsuarioActual().setgUser(user);
                            mostrarLoading();
                            existeUsuario(Usuario.getUsuarioActual().getFbUid());
                        }
                        if (response != null) {
                            System.out.println("Response=" + response);
                        }
                    }
                });
                Request.executeBatchAsync(request);
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}