package proyecto.proyectobookit.base_datos;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Carlos on 22-02-2015.
 */
public class Pin {

    private String id_pin, publicacion, duracion,  descripcion,  precio,  tipo_ayuda, campus, titulo, realizacion, hora;
    private double latitude, longitude;
    private int cantSolicitudes;

    private Ramo Ramo_Pin = new Ramo("","","","");
    private Usuario Usuario_Pin = new Usuario();

    public Pin(){
        latitude = -1;
        longitude = -1;
    }

    public static void cargarDatos(String datos, Pin auxPin) {
        try {
            Log.d("Informacion Pin: ", datos);
            JSONObject jo = new JSONObject(datos);

            try {auxPin.setId_pin(jo.getString("id")); } catch (Exception e) {}
            try {auxPin.getUsuario_Pin().setId_usuario(jo.getString("user_id")); } catch (Exception e) {}
            try {auxPin.setPublicacion(jo.getString("publication")); } catch (Exception e) {}
            auxPin.setHora(auxPin.getPublicacion().replace("T", " "));
            try {auxPin.setRealizacion(jo.getString("realization"));} catch (Exception e) {}
            try {auxPin.setDuracion(jo.getString("duration"));} catch (Exception e) {}
            try {auxPin.getRamo_Pin().setId_ramo(jo.getString("title"));}catch(Exception e){}
            try {auxPin.setDescripcion(jo.getString("description"));} catch (Exception e) {}
            try {auxPin.setPrecio(jo.getString("price"));} catch (Exception e) {}
            try {auxPin.setTipo_ayuda(jo.getString("help_type"));} catch (Exception e) {}
            try {auxPin.setCampus(jo.getString("faculty"));} catch (Exception e) {}
            try {auxPin.setLatitude(Double.parseDouble(jo.getString("latitude")));} catch (Exception e) {}
            try {auxPin.setLongitude(Double.parseDouble(jo.getString("longitude"))); } catch (Exception e) {}
            try {auxPin.setCantSolicitudes(Integer.parseInt(jo.getString("applications"))); } catch (Exception e) {}

        } catch (Exception e) {
            Log.d("Informacion", "Error al cargar datos de pines:" + datos);
        }
    }

    //Setters
    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    public void setRealizacion(String realizacion) {
        this.realizacion = realizacion;
    }

    public void setTipo_ayuda(String tipo_ayuda) {
        this.tipo_ayuda = tipo_ayuda;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //Getters
    public String getTitulo() {
        return titulo;
    }

    public String getLatitude() {
        return latitude + "";
    }

    public String getLongitude() {
        return longitude+"";
    }

    public String getCampus() {
        return campus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getPrecio() {
        return precio;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public String getTipo_ayuda() {
        return tipo_ayuda;
    }

    public String getRealizacion() {
        return realizacion;
    }

    public Ramo getRamo_Pin() {
        return Ramo_Pin;
    }

    public double getLatitudeNumber() {
        return this.latitude;
    }

    public double getLongitudeNumber() {
        return this.longitude;
    }

    public Usuario getUsuario_Pin() {
        return Usuario_Pin;
    }

    public String getId_pin() {
        return id_pin;
    }

    public void setId_pin(String id_pin) {
        this.id_pin = id_pin;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCantSolicitudes() {
        return cantSolicitudes;
    }

    public void setCantSolicitudes(int cantSolicitudes) {
        this.cantSolicitudes = cantSolicitudes;
    }
}
