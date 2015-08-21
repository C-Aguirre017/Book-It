package proyecto.proyectobookit.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import proyecto.proyectobookit.R;
import proyecto.proyectobookit.base_datos.Usuario;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Get_Perfil extends Fragment {

    private Usuario mi_perfil;

    private TextView Nombre,Carrera,Telefono,Biografia;
    private CircleImageView user_picture;

    public Get_Perfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get__perfil, container, false);
        //Colocar Cosas

        Nombre = (TextView) v.findViewById(R.id.get_perfil_nombre);
        Carrera = (TextView) v.findViewById(R.id.get_perfil_carrera);
        Telefono = (TextView) v.findViewById(R.id.get_perfil_telefono);
        Biografia = (TextView) v.findViewById(R.id.get_perfil_curriculum);
        user_picture = (CircleImageView) v.findViewById(R.id.get_perfil_profile_image);

        setFbImage(mi_perfil.getFbUid(),user_picture);

        //Verificar Textos
        verificarEscrito();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mi_perfil = Usuario.getUsuarioActual();
        verificarEscrito();
    }

    public void setMi_perfil(Usuario mi_perfil) {
        this.mi_perfil = mi_perfil;
    }

    private void setFbImage(String fbid, CircleImageView picture){
        Log.d("Informacion", "http://graph.facebook.com/" + fbid + "/picture?type=square");
        Picasso.with(getActivity().getApplication().getApplicationContext())
                .load("https://graph.facebook.com/" + fbid + "/picture?type=square")
                .resize(100, 100)
                .centerCrop()
                .into(picture, new Callback() {
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

    private void verificarEscrito(){
        if(mi_perfil.getNombre() != null) {
            if(!mi_perfil.getNombre().equals("null")) {
                Nombre.setText(mi_perfil.getNombre());
            }
        }

        if(mi_perfil.getCarrera()!= null) {
            if(!mi_perfil.getCarrera().equals("null")) {
                Carrera.setText(mi_perfil.getCarrera());
            }
        }

        if(mi_perfil.getBiografia() !=null){
            if(!mi_perfil.getBiografia().equals("null")){
                Biografia.setText(mi_perfil.getBiografia());
            }
        }

        if(mi_perfil.getTelefono() !=null){
            if(!mi_perfil.getTelefono().equals("null")){
                Telefono.setText(mi_perfil.getTelefono());
            }
        }
    }
}
