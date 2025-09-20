import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private List<Contacto> contactos;

    public Agenda() {
        contactos = new ArrayList<>();
    }

    public void agregarContacto(Contacto c) {
        contactos.add(c);
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public List<Contacto> buscarPorNombre(String nombre) {
        List<Contacto> resultado = new ArrayList<>();
        for (Contacto c : contactos) {
            if (c.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void exportarExcel(String archivo) throws IOException {
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write("Nombre,Telefono,Correo\n");
            for (Contacto c : contactos) {
                fw.write(c.getNombre() + "," + c.getTelefono() + "," + c.getCorreo() + "\n");
            }
        }
    }
}