package proyecto.proyectobookit.activity;

import java.util.ArrayList;
import java.util.List;

import proyecto.proyectobookit.base_datos.Pin;
import proyecto.proyectobookit.base_datos.Ramo;

/**
 * Created by Carlos on 21-02-2015.
 */
public class MetodosUtiles {

    private List<Ramo> Lista_Ramos = new ArrayList<Ramo>();

    public String CrearMensaje(String Descripcion, String Publicacion, String precio,String Tipo_ayuda){
        String Mensaje="Descripcion: \n";
        if (Descripcion != null && !Descripcion.equals(""))
            Mensaje += "\t " + Descripcion;
        else
            Mensaje += "\t Sin Información";

        Mensaje+= "\n Publicado el: \n";
        if (Publicacion != null && !Publicacion.equals("") ) {
            Mensaje += "\t " + Publicacion;
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Dispuesto a Pagar: \n";
        if (precio != null && !precio.equals("") ) {
            Mensaje += "\t " + precio;
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Tipo de Ayuda: \n";
        if (Tipo_ayuda != null && !Tipo_ayuda.equals("") ) {
            Mensaje += "\t " + Tipo_ayuda;
        } else {
            Mensaje += "\t Sin Información";
        }

        return Mensaje;
    }

    public List<Ramo> getLista_Ramos() {
        return Lista_Ramos;
    }

    public void Agregar_Ramo(Ramo Aux) {
        Lista_Ramos.add(Aux);
    }

    public String CrearMensaje(Pin aux) {
        String Mensaje="Descripcion: \n";
        if (aux.getDescripcion() != null && !aux.getDescripcion().equals(""))
            Mensaje += "\t " +aux.getDescripcion();
        else
            Mensaje += "\t Sin Información";

        Mensaje+= "\n Fecha: \n";
        if (aux.getHora() != null && !aux.getHora().equals("") ) {
            String[] tiem = aux.getHora().split(" ");

            Mensaje += "\t " + tiem[0]  + "\n Hora: \n \t " + tiem[1].substring(0,5);
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Dispuesto a Pagar: \n";
        if (aux.getPrecio() != null && !aux.getPrecio().equals("") ) {
            Mensaje += "\t " + aux.getPrecio();
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Facultad: \n";
        if (aux.getCampus() != null && !aux.getCampus().equals("") ) {
            Mensaje += "\t " + aux.getCampus();
        } else {
            Mensaje += "\t Sin Información";
        }

        return Mensaje;
    }

}
