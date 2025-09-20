import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AgendaApp extends JFrame {
    private ContactoDAO dao = new ContactoDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNombre, txtTelefono, txtCorreo, txtBuscar;

    public AgendaApp() {
        setTitle("Agenda de Contactos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelForm = new JPanel(new GridLayout(4, 2));
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Telefono:"));
        txtTelefono = new JTextField();
        panelForm.add(txtTelefono);

        panelForm.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelForm.add(txtCorreo);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarContacto());
        panelForm.add(btnAgregar);

        add(panelForm, BorderLayout.NORTH);

        // Tabla
        model = new DefaultTableModel(new String[]{"ID", "Nombre", "Telefono", "Correo"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel inferior
        JPanel panelBottom = new JPanel();
        txtBuscar = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarContacto());
        JButton btnExportar = new JButton("Exportar a CSV");
        btnExportar.addActionListener(e -> exportarCSV());

        panelBottom.add(new JLabel("Buscar:"));
        panelBottom.add(txtBuscar);
        panelBottom.add(btnBuscar);
        panelBottom.add(btnExportar);

        add(panelBottom, BorderLayout.SOUTH);

        cargarContactos();
    }

    private void agregarContacto() {
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();

        if (!nombre.isEmpty() && !telefono.isEmpty() && !correo.isEmpty()) {
            try {
                dao.agregar(new Contacto(0, nombre, telefono, correo));
                JOptionPane.showMessageDialog(this, "Contacto agregado!");
                cargarContactos();
                txtNombre.setText("");
                txtTelefono.setText("");
                txtCorreo.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar contacto: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Completa todos los campos");
        }
    }

    private void cargarContactos() {
        model.setRowCount(0);
        List<Contacto> lista = dao.listar();
        for (Contacto c : lista) {
            model.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono(), c.getCorreo()});
        }
    }

    private void buscarContacto() {
        String nombre = txtBuscar.getText();
        model.setRowCount(0);
        List<Contacto> lista = dao.buscar(nombre);
        for (Contacto c : lista) {
            model.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono(), c.getCorreo()});
        }
    }

    private void exportarCSV() {
        try (FileWriter fw = new FileWriter("contactos.csv")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                fw.write(
                    model.getValueAt(i, 1) + "," +
                    model.getValueAt(i, 2) + "," +
                    model.getValueAt(i, 3) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "Exportado a contactos.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AgendaApp().setVisible(true));
    }
}