import java.sql.*;

// Clase para conectar con la base de datos PostgreSQL
public class ConexionDB {
    
    // Datos para conectar a la base de datos - CAMBIAR ESTOS VALORES
    private final String servidor = "localhost";
    private final String puerto = "5432"; 
    private final String baseDatos = "agenda_contactos";
    private final String usuario = "postgres";        // Cambiar por tu usuario
    private final String password = "123456";         // Cambiar por tu contraseña
    
    // Variable para la conexión
    private Connection conexion;
    
    // Constructor
    public ConexionDB() {
        conectar();
        crearTabla();
    }
    
    // Método para conectar con PostgreSQL
    @SuppressWarnings("UseSpecificCatch")
    public final void conectar() {
        try {
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Crear la URL de conexión
            String url = "jdbc:postgresql://" + servidor + ":" + puerto + "/" + baseDatos;
            
            // Hacer la conexión
            conexion = DriverManager.getConnection(url, usuario, password);
            
            System.out.println("¡Conexión exitosa a PostgreSQL!");
            
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            System.out.println("Revisa que PostgreSQL esté funcionando y los datos sean correctos");
        }
    }
    
    // Método para crear la tabla si no existe
    public final void crearTabla() {
        try {
            // SQL para crear la tabla contactos
            try (Statement stmt = conexion.createStatement()) {
                // SQL para crear la tabla contactos
                String sql = "CREATE TABLE IF NOT EXISTS contactos (" +
                        "id SERIAL PRIMARY KEY," +
                        "nombre VARCHAR(100) NOT NULL," +
                        "telefono VARCHAR(20)," +
                        "correo VARCHAR(100)" +
                        ")";
                
                stmt.execute(sql);
                System.out.println("Tabla 'contactos' lista para usar");
            }
            
        } catch (SQLException e) {
            System.out.println("Error creando tabla: " + e.getMessage());
        }
    }
    
    // Método para obtener la conexión
    public Connection getConexion() {
        try {
            // Verificar si la conexión está cerrada y reconectar si es necesario
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.out.println("Error verificando conexión: " + e.getMessage());
        }
        return conexion;
    }
    
    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}