package org.ui;

import org.data.CsvDataReaderPeliculas;
import org.data.CsvDataReaderUsuarios;
import org.data.DataServicePeliculas;
import org.data.DataServiceUsuarios;
import org.models.Peliculas;
import org.models.Sesion;
import org.models.Usuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class inicio extends JFrame{
    private JPanel panel1;
    private JTable table1;
    private JPanel JPanel1;

    DataServicePeliculas ds = new CsvDataReaderPeliculas();
    ArrayList<Peliculas> listaPeliculas = ds.findAllPeliculas();
    DataServiceUsuarios dsu = new CsvDataReaderUsuarios();
    ArrayList<Usuarios> listaUsuarios = dsu.findAllUsuarios();

    public void start(){this.setVisible(true);}
    public inicio() throws IOException {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Peliculas");
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setContentPane(JPanel1);
        var modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Id");
        modelo.addColumn("Titulo");
        modelo.addColumn("Año");
        modelo.addColumn("Director");
        modelo.addColumn("Descripción");
        modelo.addColumn("Genero");
        modelo.addColumn("Imagen");
        table1.setModel(modelo);


        JMenuBar menuBar = PrepareMenuBar();
        this.setJMenuBar(menuBar);

        loadDataTable();



        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table1.getSelectionModel().addListSelectionListener( (e)->{
                    if(!e.getValueIsAdjusting() && table1.getSelectedRow()>0){
                        int cuenta = 0;
                        Peliculas peliculas = new Peliculas();
                        for (Peliculas p : listaPeliculas) {
                            if (Sesion.id.equals(p.getUsuario())) {
                                cuenta++;
                                if (table1.getSelectedRow() == cuenta){
                                    peliculas = p;
                                }
                            }

                            }


                        org.context.ContextService.getInstance().addItem("juegoSeleccionado",peliculas);
                        if (peliculas.getUsuario()==Sesion.id) {
                            (new Detalles(peliculas)).setVisible(true);
                            try {
                                loadDataTable();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                }
        );


    }

    private JMenuBar PrepareMenuBar() {
        /**
         * Menu con todas las funciones enfocadas al usuario
         */
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Inicio");
        JMenuItem menuItemBuscar = new JMenuItem("Buscar pelicula");
        JMenuItem menuItemAñadir = new JMenuItem("Añadir pelicula");
        JMenuItem menuItemBuscarPeliculasUsuario = new JMenuItem("Buscar peliculas por usuario");
        JMenuItem menuItemMisPeliculas = new JMenuItem("Ver todas mis peliculas");
        JMenuItem menuItemCerrarSesion = new JMenuItem("Cerrar sesion");
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemAñadir);
        jMenuInicio.add(menuItemBuscar);
        jMenuInicio.add(menuItemMisPeliculas);
        jMenuInicio.add(menuItemBuscarPeliculasUsuario);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemCerrarSesion);
        jMenuInicio.add(menuItemSalir);


        menuItemBuscar.addActionListener(e -> {
            String peliculaTitulo = JOptionPane.showInputDialog(this, "Ingresa el titulo de la pelicula a buscar");
            try {
                loadDataTablePeliculasPropias(peliculaTitulo);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        menuItemBuscarPeliculasUsuario.addActionListener(e -> {
            String usuario = JOptionPane.showInputDialog(this, "Nombre de usuario o correo electronico");
            try {
                loadDataTablePeliculasOtroUsuario(usuario);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuItemMisPeliculas.addActionListener(e -> {
            try {
                loadDataTable();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuItemAñadir.addActionListener(e -> {
            Añadir añadir = null;
            try {
                añadir = new Añadir();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            añadir.start();
            try {
                loadDataTable();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuItemCerrarSesion.addActionListener(e -> {
            this.dispose();
            Sesion.id = null;
            login login = new login();
            login.start();

        });

        menuItemSalir.addActionListener(e -> { System.exit(0); });

        return menuBar;
    }

    private void loadDataTable() throws IOException {
        /**
         * metodo para cargar todas las peliculas del usuario logueado
         */
        listaPeliculas = ds.findAllPeliculas();
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();

        modelo.setRowCount(0);
        var fila1 = new Object[]{"ID","Titulo","Año","Director","Descripción","Género","Imagen","Usuario"};
        modelo.addRow(fila1);
        listaPeliculas.forEach( (j)->{
            if (Sesion.id.equals(j.getUsuario())) {
                var fila = new Object[]{j.getId(), j.getTitulo(), j.getAño(), j.getDirector(), j.getDescripcion(), j.getGenero(), j.getImagen()};
                modelo.addRow(fila);
            }
        });
    }

    private void loadDataTablePeliculasPropias(String peliculaTitulo) throws IOException {
        /**
         * metodo para buscar peliculas por nombre que pertenezvan al usuario logueado
         */
        if (peliculaTitulo != null) {

        listaPeliculas = ds.findAllPeliculas();
        DefaultTableModel modelo2 = (DefaultTableModel) table1.getModel();

        modelo2.setRowCount(0);
        var fila1 = new Object[]{"ID","Titulo","Año","Director","Descripción","Género","Imagen","Usuario"};
        modelo2.addRow(fila1);
        listaPeliculas.forEach( (j)->{
            if (Sesion.id.equals(j.getUsuario())&& j.getTitulo().toLowerCase().contains(peliculaTitulo.toLowerCase())) {
                var fila = new Object[]{j.getId(), j.getTitulo(), j.getAño(), j.getDirector(), j.getDescripcion(), j.getGenero(), j.getImagen()};
                modelo2.addRow(fila);
            }
        });
            if (modelo2.getRowCount() <=1) {
                loadDataTable();
                JOptionPane.showMessageDialog(this, "No se han encontrado peliculas");
            }
    }
    }

    private void loadDataTablePeliculasOtroUsuario(String usuario) throws IOException {
        /**
         * metodo para buscar peliculas por usuario o email
         */
        if (usuario != null) {
            listaPeliculas = ds.findAllPeliculas();
            DefaultTableModel modelo3 = (DefaultTableModel) table1.getModel();
            AtomicReference<Integer> id = new AtomicReference<>(0);
            modelo3.setRowCount(0);
            var fila1 = new Object[]{"ID", "Titulo", "Año", "Director", "Descripción", "Género", "Imagen", "Usuario"};
            modelo3.addRow(fila1);
            listaUsuarios.forEach((j) -> {
                if (j.getUsuario().equalsIgnoreCase(usuario) || j.getUsuario().equals(usuario)) {
                    id.set(Integer.parseInt(String.valueOf(j.getId())));
                }
            });
            listaPeliculas.forEach((j) -> {
                if (j.getUsuario() == id.get()) {
                    var fila = new Object[]{j.getId(), j.getTitulo(), j.getAño(), j.getDirector(), j.getDescripcion(), j.getGenero(), j.getImagen()};
                    modelo3.addRow(fila);
                }
            });
            if (modelo3.getRowCount() <=1) {
                loadDataTable();
                JOptionPane.showMessageDialog(this, "No se han encontrado peliculas");

            }
        }
    }


}
