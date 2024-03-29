package proyecto.proyectobookit.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.nav_drawner.NestedListView;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.model_adapters.ListViewAdapter_MisPins;
import proyecto.proyectobookit.utils.Configuracion;

public class Mis_Pins extends Fragment {

    // Declare Variables
    ListView lista_Pines;
    ListViewAdapter_MisPins adapter;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_pins, container, false);
        setHasOptionsMenu(true);

        lista_Pines = (NestedListView) v.findViewById(R.id.mis_pins_lista);
        adapter = new ListViewAdapter_MisPins(mContext);
        lista_Pines.setAdapter(adapter);

        actualizarPin();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.menucentral_viewasList).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menucentral_dropdown_campus).setVisible(false);
        menu.findItem(R.id.menucentral_editar).setVisible(false);

        ActionBar actions = getActivity().getActionBar();
        actions.setDisplayHomeAsUpEnabled(true);
        actions.setTitle("Mis Pins");
        actions.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id== R.id.menucentral_refresh){
            actualizarPin();
        }

        return true;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private void actualizarPin() {
        if(Usuario.getUsuarioActual().getId_usuario() !=null){
            String Url = Configuracion.URLSERVIDOR + "/users/";

            try {
                Url += URLEncoder.encode(Usuario.getUsuarioActual().getId_usuario(), "UTF-8") +"/pins.json";
                adapter.actualizarPines(Url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
