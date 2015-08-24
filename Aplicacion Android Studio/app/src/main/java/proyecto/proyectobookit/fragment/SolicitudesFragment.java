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
import proyecto.proyectobookit.activity.Solicitudes;
import proyecto.proyectobookit.base_datos.Usuario;
import proyecto.proyectobookit.model_adapters.ListViewAdapter_Solicitudes;
import proyecto.proyectobookit.nav_drawner.NestedListView;
import proyecto.proyectobookit.utils.Configuracion;

/**
 * A placeholder fragment containing a simple view.
 */
public class SolicitudesFragment extends Fragment {

    private Context mContext;
    ListView lista_Solicitudes;
    ListViewAdapter_Solicitudes adapter;
    private String pin_id;

    public SolicitudesFragment(String pin_id, Context mContext) {
        this.pin_id = pin_id;
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_solicitudes, container, false);
        setHasOptionsMenu(true);

        lista_Solicitudes = (NestedListView) v.findViewById(R.id.mis_solicitudes_lista);
        adapter = new ListViewAdapter_Solicitudes(mContext);
        lista_Solicitudes.setAdapter(adapter);

        actualizarSolicitudes();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        ActionBar actions = getActivity().getActionBar();
        actions.setDisplayHomeAsUpEnabled(true);
        actions.setTitle("Solicitudes");
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
            actualizarSolicitudes();
        }

        return true;
    }

    private void actualizarSolicitudes() {
        if(Usuario.getUsuarioActual().getId_usuario() !=null){
            String Url = Configuracion.URLSERVIDOR + "/pins/";

            try {
                Url += URLEncoder.encode(this.pin_id, "UTF-8") +"/applications.json";
                adapter.actualizarSolicitudes(Url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
