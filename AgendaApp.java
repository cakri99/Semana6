import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class AgendaApp extends JFrame {
    private Agenda agenda;
    private DefaultListModel<String> listaModelo;
    private JList<String> listaContactos;
    private JTextField txtNombre, txtTelefono, txtCorreo, txtBuscar;

    public AgendaApp() {
        super("Agenda de Contactos"); // <- aquí fijamos el título de la ventana
        agenda = new Agenda();
        listaModelo = new DefaultListModel<>();
        listaContactos = new JList<>(listaModelo);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // --- Panel formulario (NORTE)
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nuevo contacto"));
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnExportar = new JButton("Exportar CSV");
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnExportar);

        add(panelFormulario, BorderLayout.NORTH);

        // --- Centro: lista de contactos
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createTitledBorder("Contactos"));
        centro.add(new JScrollPane(listaContactos), BorderLayout.CENTER);

        JPanel botonesCentro = new JPanel();
        JButton btnVer = new JButton("Ver detalle");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        botonesCentro.add(btnVer);
        botonesCentro.add(btnEliminar);
        centro.add(botonesCentro, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);

        // --- Sur: búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar"));
        panelBusqueda.add(new JLabel("Nombre:"));
        txtBuscar = new JTextField(15);
        panelBusqueda.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);
        add(panelBusqueda, BorderLayout.SOUTH);

        // --- Listeners
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String correo = txtCorreo.getText().trim();
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Contacto c = new Contacto(nombre, telefono, correo);
            agenda.agregarContacto(c);
            listaModelo.addElement(c.toString());
            txtNombre.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");
        });

        btnBuscar.addActionListener(e -> {
            String q = txtBuscar.getText().trim();
            List<Contacto> encontrados = agenda.buscarPorNombre(q);
            actualizarLista(encontrados);
        });

        btnMostrarTodos.addActionListener(e -> mostrarTodos());

        btnExportar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("contactos.csv"));
            int res = chooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                java.io.File f = chooser.getSelectedFile();
                try {
                    agenda.exportarExcel(f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Exportado a: " + f.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int idx = listaContactos.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un contacto para eliminar.");
                return;
            }
            String seleccionado = listaModelo.getElementAt(idx);
            Contacto toRemove = null;
            for (Contacto c : agenda.getContactos()) {
                if (c.toString().equals(seleccionado)) {
                    toRemove = c;
                    break;
                }
            }
            if (toRemove != null) {
                agenda.getContactos().remove(toRemove);
                listaModelo.remove(idx);
            }
        });

        btnVer.addActionListener(e -> {
            int idx = listaContactos.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un contacto.");
                return;
            }
            String sel = listaModelo.getElementAt(idx);
            JOptionPane.showMessageDialog(this, sel, "Detalle", JOptionPane.INFORMATION_MESSAGE);
        });

        // Mostrar ventana
        setVisible(true);
    }

    private void actualizarLista(List<Contacto> lista) {
        listaModelo.clear();
        for (Contacto c : lista) listaModelo.addElement(c.toString());
    }

    private void mostrarTodos() {
        actualizarLista(agenda.getContactos());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AgendaApp::new);
    }
}