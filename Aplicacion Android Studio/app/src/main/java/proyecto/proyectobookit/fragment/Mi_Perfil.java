package proyecto.proyectobookit.fragment;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import proyecto.proyectobookit.Central;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.activity.CrearMarker;
import proyecto.proyectobookit.activity.Editar_Perfil;
import proyecto.proyectobookit.base_datos.Usuario;

/**
 * A simple {@link android.app.Fragment} subclass.
 */

public class Mi_Perfil extends Fragment {

    Context mContext;

    public Mi_Perfil() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mi__perfil, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.menucentral_viewasList).setVisible(false);
        menu.findItem(R.id.menucentral_refresh).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menucentral_dropdown_campus).setVisible(false);
        menu.findItem(R.id.menucentral_editar).setVisible(true);

        ActionBar actions = getActivity().getActionBar();
        actions.setDisplayHomeAsUpEnabled(true);
        actions.setTitle("Mi Perfil");
        actions.setDisplayShowTitleEnabled(true);

        //Colocar Mi Perfil

        Get_Perfil Get_Perfil = new proyecto.proyectobookit.fragment.Get_Perfil();
        Get_Perfil.setMi_perfil(Usuario.getUsuarioActual());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mi_perfil_container,Get_Perfil);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menucentral_editar) {
            Intent i = new Intent(mContext, Editar_Perfil.class);
            startActivity(i);
        }

        return true;
    }

    public void setContext(Context Aux){
        mContext = Aux;
    }
}
