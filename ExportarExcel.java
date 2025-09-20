import java.io.*;
import java.util.ArrayList;

// Clase para exportar contactos a Excel (formato CSV)
public class ExportarExcel {
    
    // Método principal para exportar contactos
    public static boolean exportarContactos(ArrayList<Contacto> contactos, String nombreArchivo) {
        
        // Verificar que hay contactos para exportar
        if (contactos == null || contactos.isEmpty()) {
            System.out.println("No hay contactos para exportar");
            return false;
        }
        
        try {
            // Crear el archivo
            FileWriter archivo = new FileWriter(nombreArchivo);
            PrintWriter escribir = new PrintWriter(archivo);
            
            // Escribir la primera línea con los títulos
            escribir.println("ID,Nombre,Teléfono,Correo");
            
            // Escribir cada contacto en una línea
            for (int i = 0; i < contactos.size(); i++) {
                Contacto contacto = contactos.get(i);
                
                // Crear la línea con los datos del contacto
                String linea = contacto.getId() + "," + 
                              "\"" + contacto.getNombre() + "\"," +
                              "\"" + contacto.getTelefono() + "\"," +
                              "\"" + contacto.getCorreo() + "\"";
                
                escribir.println(linea);
            }
            
            // Cerrar el archivo
            escribir.close();
            archivo.close();
            
            System.out.println("Archivo exportado exitosamente: " + nombreArchivo);
            System.out.println("Total de contactos exportados: " + contactos.size());
            
            return true;
            
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }
    
    // Método para crear un nombre de archivo con fecha y hora
    public static String crearNombreArchivo() {
        // Obtener fecha y hora actual
        java.util.Date fecha = new java.util.Date();
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaTexto = formato.format(fecha);
        
        // Crear el nombre del archivo
        String nombreArchivo = "contactos_" + fechaTexto + ".csv";
        
        return nombreArchivo;
    }
    
    // Método adicional para exportar con más información
    public static boolean exportarContactosDetallado(ArrayList<Contacto> contactos, String nombreArchivo) {
        
        if (contactos == null || contactos.isEmpty()) {
            System.out.println("No hay contactos para exportar");
            return false;
        }
        
        try {
            FileWriter archivo = new FileWriter(nombreArchivo);
            PrintWriter escribir = new PrintWriter(archivo);
            
            // Escribir información del archivo
            escribir.println("# Agenda de Contactos");
            escribir.println("# Fecha de exportación: " + new java.util.Date());
            escribir.println("# Total de contactos: " + contactos.size());
            escribir.println("#");
            
            // Escribir los títulos
            escribir.println("ID,Nombre,Teléfono,Correo");
            
            // Escribir cada contacto
            for (int i = 0; i < contactos.size(); i++) {
                Contacto contacto = contactos.get(i);
                
                // Revisar si los datos están vacíos y poner "Sin datos"
                String nombre = (contacto.getNombre() == null || contacto.getNombre().isEmpty()) ? 
                               "Sin nombre" : contacto.getNombre();
                String telefono = (contacto.getTelefono() == null || contacto.getTelefono().isEmpty()) ? 
                                 "Sin teléfono" : contacto.getTelefono();
                String correo = (contacto.getCorreo() == null || contacto.getCorreo().isEmpty()) ? 
                               "Sin correo" : contacto.getCorreo();
                
                String linea = contacto.getId() + "," + 
                              "\"" + nombre + "\"," +
                              "\"" + telefono + "\"," +
                              "\"" + correo + "\"";
                
                escribir.println(linea);
            }
            
            escribir.close();
            archivo.close();
            
            System.out.println("Archivo detallado exportado: " + nombreArchivo);
            return true;
            
        } catch (IOException e) {
            System.out.println("Error exportando archivo detallado: " + e.getMessage());
            return false;
        }
    }
}