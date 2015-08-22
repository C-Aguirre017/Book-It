package proyecto.proyectobookit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import proyecto.proyectobookit.activity.ListaMapa;
import proyecto.proyectobookit.nav_drawner.NavDrawer_ListAdapter;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.fragment.Acerca_De;
import proyecto.proyectobookit.fragment.Help;
import proyecto.proyectobookit.fragment.Mapa;
import proyecto.proyectobookit.fragment.Mi_Perfil;
import proyecto.proyectobookit.fragment.Mis_Pins;
import proyecto.proyectobookit.nav_drawner.NavDrawerItem;

public class Central extends Activity {

    GoogleMap Mapas;
    Boolean EsMapaActual = false;

    //Menu
    private Usuario Mi_Usuario = null;

    //Navigation Drawner
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawer_ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        Mi_Usuario = Usuario.getUsuarioActual();

        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.central_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.central_navigation_drawner);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // Cargar Menu
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navMenuIcons.recycle();

        View header = getLayoutInflater().inflate(R.layout.navigationdrawer_header, mDrawerList, false);
        mDrawerList.addHeaderView(header, null, false);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new NavDrawer_ListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(1);
        }

        setFbImage(Mi_Usuario.getFbUid());
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        Fragment FragmentoELegido = null;

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapView));
        if(fragment!=null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }

        switch (position) {
            case 0:
                break;
            case 1:
                Mapa Aux = new Mapa();
                Aux.setContext(this);
                Aux.setMapa(Mapas);
                FragmentoELegido = Aux;
                break;
            case 2:
                Mi_Perfil Aux_Mi_Perfil = new Mi_Perfil();
                Aux_Mi_Perfil.setContext(this);
                FragmentoELegido = Aux_Mi_Perfil;
                break;
            case 3:
                Mis_Pins Aux_pins = new Mis_Pins();
                Aux_pins.setmContext(this);
                FragmentoELegido = Aux_pins;
                break;
            case 4:
                FragmentoELegido = new Help();
                break;
            case 5:
                String[] TO = {getResources().getString(R.string.mailaplicacion)};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Elija un cliente de correo electrónico: "));
                    Log.i("Correo Enviado", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getBaseContext(), "No hay una Aplicación de Correo Instalada", Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                FragmentoELegido = new Acerca_De();
                break;
            default:
                Usuario.getUsuarioActual().getFbSession().closeAndClearTokenInformation();
                Inicio.setIngreso(false);
                Intent mStartActivity = new Intent(this, Inicio.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                this.finish();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);

        if (FragmentoELegido != null) {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.central_container, FragmentoELegido);
            transaction.commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

    }

    private void setFbImage(String fbid){
        CircleImageView  user_picture = (CircleImageView) findViewById(R.id.centralheader_profile_image);
        Log.d("Informacion", "http://graph.facebook.com/" + fbid + "/picture?type=square");
        Picasso.with(this)
                .load("https://graph.facebook.com/" + fbid + "/picture?type=square")
                .resize(100, 100)
                .centerCrop()
                .into(user_picture, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("Informacion", "Se logro poner imagen de fb");
            }

            @Override
            public void onError() {
                Log.d("Informacion", "No se pudo poner imagen de facebook");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_central, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if(id ==  android.R.id.home){
            return true;
        }
        else if(id== R.id.menucentral_viewasList){
            Intent neaModoLista = new Intent(Central.this,ListaMapa.class);
            startActivity(neaModoLista);
        }
        return super.onOptionsItemSelected(item);
    }
}
