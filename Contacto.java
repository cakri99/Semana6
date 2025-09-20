public class Contacto {
    // Variables para guardar la información
    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    
    // Constructor vacío
    public Contacto() {
        // No hace nada, pero es necesario
    }
    
    // Constructor con todos los datos
    public Contacto(int id, String nombre, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }
    
    // Métodos get para obtener los valores
    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    // Métodos set para cambiar los valores
    public void setId(int id) {
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    // Método para mostrar la información del contacto
    @Override
    public String toString() {
        return "ID: " + id + " - " + nombre + " - " + telefono + " - " + correo;
    }
}