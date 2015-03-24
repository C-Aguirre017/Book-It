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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.GoogleMap;

import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import proyecto.proyectobookit.activity.ModoLista_Principal;
import proyecto.proyectobookit.adapters.NavDrawer_ListAdapter;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.fragment.Acerca_De;
import proyecto.proyectobookit.fragment.Help;
import proyecto.proyectobookit.fragment.Mapa;
import proyecto.proyectobookit.fragment.Mis_Pins;
import proyecto.proyectobookit.model_adapters.NavDrawerItem;

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
        Mi_Usuario.setToken("LDskzPi1vfr31746VKG3");

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

        setFbImage(Mi_Usuario.getFbUid());

        if (savedInstanceState == null) {
            displayView(1);
        }
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
                Aux.setMi_Usuario(Mi_Usuario);
                FragmentoELegido = Aux;
                break;
            case 2:
                Mis_Pins Aux_pins = new Mis_Pins();
                Aux_pins.setmContext(this);
                Aux_pins.setMi_Usuario(Mi_Usuario);
                FragmentoELegido = Aux_pins;
                break;
            case 3:
                FragmentoELegido = new Help();
                break;
            case 4:
                String[] TO = {getResources().getString(R.string.mailaplicacion)};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Elija un cliente de correo electrónico: "));
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getBaseContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
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
        (new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... fbid) {
                try {
                    URL img_value = new URL("http://graph.facebook.com/" + fbid[0] + "/picture?type=square");
                    Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
                    return mIcon1;
                } catch(Exception e) {
                    Log.d("Informacion", "Problemas al cargar imagen de fb: " + e.toString());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                Log.d("Informacion", "Cargando imagen de fb");
                CircleImageView  user_picture = (CircleImageView) findViewById(R.id.centralheader_profile_image);
                user_picture.setImageBitmap(result);
                user_picture.invalidate();
            }
        }).execute(fbid);

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
            Intent NuevaActividad_ModoLista = new Intent(Central.this,ModoLista_Principal.class);
            NuevaActividad_ModoLista.putExtra("id_usuario", Mi_Usuario.getId_usuario());
            startActivity(NuevaActividad_ModoLista);
        }
        return super.onOptionsItemSelected(item);
    }
}
