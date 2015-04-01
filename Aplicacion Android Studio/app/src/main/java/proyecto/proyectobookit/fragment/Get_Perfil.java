package proyecto.proyectobookit.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Usuario;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Get_Perfil extends Fragment {

    private Usuario mi_perfil;
    private String numero_telefono = null;

    public Get_Perfil() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get__perfil, container, false);
        //Colocar Cosas

        TextView Nombre = (TextView) v.findViewById(R.id.get_perfil_nombre);
        TextView Carrera = (TextView) v.findViewById(R.id.get_perfil_carrera);
        TextView Telefono = (TextView) v.findViewById(R.id.get_perfil_telefono);
        //TextView Nivel = (TextView) v.findViewById(R.id.get_perfil_nivel);
        TextView Curriculum = (TextView) v.findViewById(R.id.get_perfil_curriculum);

        Nombre.setText(mi_perfil.getNombre());
        Carrera.setText(mi_perfil.getCarrera());
        //Nivel.setText(mi_perfil.getNivel());
        //Curriculum.setText(mi_perfil.getHabilidades());
        if(numero_telefono!=null){
            Telefono.setText(numero_telefono);
        }else{
            Telefono.setText("+569 XXXXXXXX");
        }


        return v;
    }

    public void setMi_perfil(Usuario mi_perfil) {
        this.mi_perfil = mi_perfil;
    }


    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }
}
