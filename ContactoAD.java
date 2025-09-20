import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ContactoAD {
    private final ConexionDB conexionDB;
    
    // Constructor
    public ContactoAD() {
        conexionDB = new ConexionDB();
    }
    
    // Método para agregar un nuevo contacto
    public boolean agregarContacto(Contacto contacto) {
        try {
            // Preparar la consulta SQL
            String sql = "INSERT INTO contactos (nombre, telefono, correo) VALUES (?, ?, ?)";
            int resultado;
            // Poner los valores en la consulta
            try (PreparedStatement stmt = conexionDB.getConexion().prepareStatement(sql)) {
                // Poner los valores en la consulta
                stmt.setString(1, contacto.getNombre());
                stmt.setString(2, contacto.getTelefono());
                stmt.setString(3, contacto.getCorreo());
                // Ejecutar la consulta
                resultado = stmt.executeUpdate();
                // Cerrar el statement
            }
            
            // Si resultado > 0 significa que se agregó correctamente
            if (resultado > 0) {
                System.out.println("Contacto agregado: " + contacto.getNombre());
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Error agregando contacto: " + e.getMessage());
            return false;
        }
    }
    
    // Método para obtener todos los contactos
    public ArrayList<Contacto> obtenerTodosContactos() {
        ArrayList<Contacto> contactos = new ArrayList<>();
        
        try {
            // Consulta para obtener todos los contactos
            String sql = "SELECT * FROM contactos ORDER BY nombre";
            try (Statement stmt = conexionDB.getConexion().createStatement(); ResultSet resultado = stmt.executeQuery(sql)) {
                // Recorrer los resultados
                while (resultado.next()) {
                    // Crear un nuevo contacto con los datos
                    Contacto contacto = new Contacto();
                    contacto.setId(resultado.getInt("id"));
                    contacto.setNombre(resultado.getString("nombre"));
                    contacto.setTelefono(resultado.getString("telefono"));
                    contacto.setCorreo(resultado.getString("correo"));
                    
                    // Agregar el contacto a la lista
                    contactos.add(contacto);
                }
                // Cerrar todo

            }
            
            System.out.println("Se obtuvieron " + contactos.size() + " contactos");
            
        } catch (SQLException e) {
            System.out.println("Error obteniendo contactos: " + e.getMessage());
        }
        
        return contactos;
    }
    
    // Método para buscar contactos
    @SuppressWarnings("ConvertToTryWithResources")
    public ArrayList<Contacto> buscarContactos(String busqueda) {
        ArrayList<Contacto> contactos = new ArrayList<>();
        
        try {
            // Consulta que busca en nombre, teléfono y correo
            String sql = "SELECT * FROM contactos WHERE " +
                        "nombre ILIKE ? OR telefono ILIKE ? OR correo ILIKE ? " +
                        "ORDER BY nombre";
            
            // Agregar % para buscar en cualquier parte del texto
            try (PreparedStatement stmt = conexionDB.getConexion().prepareStatement(sql)) {
                // Agregar % para buscar en cualquier parte del texto
                String termino = "%" + busqueda + "%";
                stmt.setString(1, termino);
                stmt.setString(2, termino);
                stmt.setString(3, termino);
                
                ResultSet resultado = stmt.executeQuery();
                
                // Recorrer los resultados
                while (resultado.next()) {
                    Contacto contacto = new Contacto();
                    contacto.setId(resultado.getInt("id"));
                    contacto.setNombre(resultado.getString("nombre"));
                    contacto.setTelefono(resultado.getString("telefono"));
                    contacto.setCorreo(resultado.getString("correo"));
                    
                    contactos.add(contacto);
                }
                
                resultado.close();
            }
            
            System.out.println("Búsqueda encontró " + contactos.size() + " contactos");
            
        } catch (SQLException e) {
            System.out.println("Error en búsqueda: " + e.getMessage());
        }
        
        return contactos;
    }
    
    // Método para actualizar un contacto
    public boolean actualizarContacto(Contacto contacto) {
        try {
            String sql = "UPDATE contactos SET nombre=?, telefono=?, correo=? WHERE id=?";
            int resultado;
            try (PreparedStatement stmt = conexionDB.getConexion().prepareStatement(sql)) {
                stmt.setString(1, contacto.getNombre());
                stmt.setString(2, contacto.getTelefono());
                stmt.setString(3, contacto.getCorreo());
                stmt.setInt(4, contacto.getId());
                resultado = stmt.executeUpdate();
            }
            
            if (resultado > 0) {
                System.out.println("Contacto actualizado: " + contacto.getNombre());
                return true;
            } else {
                System.out.println("No se pudo actualizar el contacto");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Error actualizando contacto: " + e.getMessage());
            return false;
        }
    }
    
    // Método para eliminar un contacto
    public boolean eliminarContacto(int id) {
        try {
            String sql = "DELETE FROM contactos WHERE id=?";
            int resultado;
            try (PreparedStatement stmt = conexionDB.getConexion().prepareStatement(sql)) {
                stmt.setInt(1, id);
                resultado = stmt.executeUpdate();
            }
            
            if (resultado > 0) {
                System.out.println("Contacto eliminado con ID: " + id);
                return true;
            } else {
                System.out.println("No se encontró el contacto para eliminar");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Error eliminando contacto: " + e.getMessage());
            return false;
        }
    }
}