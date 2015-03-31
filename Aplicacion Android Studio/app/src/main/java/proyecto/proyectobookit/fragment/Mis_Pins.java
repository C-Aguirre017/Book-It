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
import proyecto.proyectobookit.adapters.NestedListView;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.model_adapters.ListViewAdapter_VerPins;

public class Mis_Pins extends Fragment {

    // Declare Variables
    ListView lista_Pines;
    ListViewAdapter_VerPins adapter;

    private Usuario Mi_Usuario;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_pins, container, false);
        setHasOptionsMenu(true);
        lista_Pines = (NestedListView) v.findViewById(R.id.mis_pins_lista);
        adapter = new ListViewAdapter_VerPins(mContext,Mi_Usuario);
        lista_Pines.setAdapter(adapter);
        Actualizar();
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
            Actualizar();
        }

        return true;
    }

    private void Actualizar() {
        if(Mi_Usuario.getId_usuario() !=null){
            String Url = "http://pinit-api.herokuapp.com/usuarios/";

            try {
                Url += URLEncoder.encode(Mi_Usuario.getId_usuario(), "UTF-8") +"/pins.json";
                adapter.ColocarPines(Url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setMi_Usuario(Usuario mi_Usuario) {
        Mi_Usuario = mi_Usuario;
    }
}
