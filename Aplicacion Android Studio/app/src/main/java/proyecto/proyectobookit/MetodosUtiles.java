package proyecto.proyectobookit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 21-02-2015.
 */
public class MetodosUtiles {

    private List<Ramo> Lista_Ramos = new ArrayList<Ramo>();

    public String CrearMensaje(String Descripcion, String Publicacion, String precio){
        String Mensaje = " Descripcion: \n	";
        if (Descripcion != null)
            Mensaje += Descripcion;
        else
            Mensaje += "No hay";

        if (Publicacion != null) {
            Mensaje += "\n Fecha Creación: \n	" + Publicacion;
        } else {
            Mensaje += "\n Fecha Creación: \n	Sin Información";
        }

        if (precio != null) {
            Mensaje += "\n Dispuesto a Pagar: \n	" + precio;
        } else {
            Mensaje += "\n Dispuesto a Pagar: \n	Sin Información";
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
        String Mensaje="";
        if (aux.getDescripcion() != null)
            Mensaje += aux.getDescripcion();
        else
            Mensaje += "No hay";

        if (aux.getPublicacion() != null) {
            Mensaje += "\n Fecha Creación: \n	" + aux.getPublicacion();
        } else {
            Mensaje += "\n Fecha Creación: \n	Sin Información";
        }

        if (aux.getPrecio() != null) {
            Mensaje += "\n Dispuesto a Pagar: \n	" + aux.getPrecio();
        } else {
            Mensaje += "\n Dispuesto a Pagar: \n	Sin Información";
        }
        return Mensaje;
    }
}
