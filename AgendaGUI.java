import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Ventana principal de la agenda de contactos
public class AgendaGUI extends JFrame {
    
    // Componentes de la interfaz
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtBuscar;
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JButton btnExportar;
    private JButton btnLimpiar;
    
    // Variables para el funcionamiento
    private final ContactoAD contactoDAO;
    private final int contactoSeleccionado = -1;  // ID del contacto seleccionado
    
    // Constructor de la ventana
    public AgendaGUI() {
        contactoDAO = new ContactoAD();
        crearInterfaz();
        cargarContactosEnTabla();
    }
    
    // Método para crear todos los componentes de la ventana
    private void crearInterfaz() {
        
        // Configurar la ventana principal
        setTitle("Mi Agenda de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);  // Centrar la ventana
        
        // Crear el panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        
        // Crear las diferentes secciones
        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelBusqueda = crearPanelBusqueda();
        JPanel panelTabla = crearPanelTabla();
        JPanel panelBotones = crearPanelBotones();
        
        // Agregar las secciones al panel principal
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(panelBusqueda, BorderLayout.CENTER);
        panelPrincipal.add(panelTabla, BorderLayout.SOUTH);
        
        // Agregar todo a la ventana
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    // Crear el panel donde se llenan los datos del contacto
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Contacto"));
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        
        // Campos del formulario
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
        panel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panel.add(txtCorreo);
        
        return panel;
    }
    
    // Crear el panel de búsqueda
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Contactos"));
        panel.setLayout(new FlowLayout());
        
        panel.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        panel.add(txtBuscar);
        
        btnBuscar = new JButton("Buscar");
        panel.add(btnBuscar);
        
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        panel.add(btnMostrarTodos);
        
        // Programar el botón buscar
        btnBuscar.addActionListener((ActionEvent e) -> {
            buscarContactos();
        });
        
        // Programar el botón mostrar todos
        btnMostrarTodos.addActionListener((ActionEvent e) -> {
            cargarContactosEnTabla();
            txtBuscar.setText("");  // Limpiar el campo de búsqueda
        });
        
        return panel;
    }
    
    // Crear la tabla para mostrar los contactos
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Contactos"));
        panel.setLayout(new BorderLayout());
        
        // Crear la tabla
        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);
        
        // Hacer que no se puedan editar las celdas
        tablaContactos.setDefaultEditor(Object.class, null);
        
        // Programar la selección de filas
        tablaContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarContacto();
            }
        });
        
        // Agregar scroll a la tabla
        JScrollPane scroll = new JScrollPane(tablaContactos);
        scroll.setPreferredSize(new Dimension(0, 200));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Crear los botones de acción
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        // Crear los botones
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnExportar = new JButton("Exportar a Excel");
        btnLimpiar = new JButton("Limpiar");
        
        // Deshabilitar botones que necesitan selección
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        
        // Programar los botones
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarContacto();
            }
        });
        
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarContacto();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarContacto();
            }
        });
        
        btnExportar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportarContactos();
            }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        // Agregar botones al panel
        panel.add(btnAgregar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnExportar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    // Método para cargar todos los contactos en la tabla
    private void cargarContactosEnTabla() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Obtener contactos de la base de datos
        ArrayList<Contacto> contactos = contactoDAO.obtenerTodosContactos();
        
        // Agregar cada contacto a la tabla
        for (Contacto contacto : contactos) {
            Object[] fila = {
                contacto.getId(),
                contacto.getNombre(),
                contacto.getTelefono(),
                contacto.getCorreo()
            };
            modeloTabla.addRow(fila);
        }
        
        System.out.println("Tabla actualizada con " + contactos.size() + " contactos");
    }
    
    // Método para buscar contactos
    private void buscarContactos() {
        String terminoBusqueda = txtBuscar.getText().trim();
        
        if (terminoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe algo para buscar");
            return;
        }
        
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Buscar contactos
        ArrayList<Contacto> contactos = contactoDAO.buscarContactos(terminoBusqueda);
        
        // Mostrar resultados en la tabla
        for (Contacto contacto : contactos) {
            Object[] fila = {
                contacto.getId(),
                contacto.getNombre(),
                contacto.getTelefono(),
                contacto.getCorreo()
            };
            modeloTabla.addRow(fila);
        }
        
        if (contactos.size() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron contactos");
        }
    }
    
    // Método para agregar un nuevo contacto
    private void agregarContacto() {
        
        // Verificar que el nombre no esté vacío
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            txtNombre.requestFocus();
            return;
        }
        
        // Crear el contacto
        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setNombre(txtNombre.getText().trim());
        nuevoContacto.setTelefono(txtTelefono.getText().trim());
        nuevoContacto.setCorreo(txtCorreo.getText().trim());
        
        // Guardar en la base de datos
        boolean agregado = contactoDAO.agregarContacto(nuevoContacto);
        
        if (agregado) {
            JOptionPane.showMessageDialog(this, "¡Contacto agregado correctamente!");
            limpiarFormulario();
            cargarContactosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el contacto");
        }
    }
    
    // Método para editar un contacto existente
    private void editarContacto() {
        
        if (contactoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un contacto para editar");
            return;
        }
        
        // Verificar que el nombre no esté vacío
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            return;
        }
        
        // Crear el contacto con los nuevos datos
        Contacto contactoEditado = new Contacto();
        contactoEditado.setId(contactoSeleccionado);
        contactoEditado.setNombre(txtNombre.getText().trim());
        contactoEditado.setTelefono(txtTelefono.getText().trim());
        contactoEditado.setCorreo(txtCorreo.getText().trim());
        
        // Actualizar en la base de datos
        boolean actualizado = contactoDAO.actualizarContacto(contactoEditado);
        
        if (actualizado) {
            JOptionPane.showMessageDialog(this, "¡Contacto actualizado correctamente!");
            limpiarFormulario();
            cargarContactosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el contacto");
        }
    }
    
    // Método para eliminar un contacto
    private void eliminarContacto() {
        
        if (contactoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un contacto para eliminar");
            return;
        }
        
        // Pedir confirmación
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro de eliminar este contacto?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = contactoDAO.eliminarContacto(contactoSeleccionado);
            
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "¡Contacto eliminado correctamente!");
                limpiarFormulario();
                cargarContactosEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el contacto");
            }
        }
    }
    
    // Método para exportar contactos a Excel
    private void exportarContactos() {
        
        // Obtener todos los contactos
        ArrayList<Contacto> contactos = contactoDAO.obtenerTodosContactos();
        
        if (contactos.size() == 0) {
            JOptionPane.showMessageDialog(this, "No hay contactos para exportar");
            return;
        }
        
        // Crear selector de archivo
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar archivo de contactos");
        
        // Poner nombre por defecto
        String nombreArchivo = ExportarExcel.crearNombreArchivo();
        selector.setSelectedFile(new java.io.File(nombreArchivo));
        
        int resultado = selector.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {