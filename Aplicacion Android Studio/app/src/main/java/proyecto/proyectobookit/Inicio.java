package proyecto.proyectobookit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Inicio extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
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
