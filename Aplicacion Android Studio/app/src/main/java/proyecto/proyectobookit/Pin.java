package proyecto.proyectobookit;

/**
 * Created by Carlos on 22-02-2015.
 */
public class Pin {
    private String publicacion, duracion,  descripcion,  precio,  tipo_ayuda, campus,titulo, realizacion;
    private double latitude, longitude;

    private Ramo Ramo_Pin = new Ramo("","","","");

    public Pin(){
        latitude =-1;
        longitude=-1;
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

   /* public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
        public String getTitulo() {
        return titulo;
    }
*/
    //Getters
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
}
