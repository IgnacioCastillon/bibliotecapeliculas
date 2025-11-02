package org.ui;

import org.data.CsvDataReaderUsuarios;
import org.data.DataServiceUsuarios;
import org.models.Sesion;
import org.models.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class login extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton iniciarSesiónButton;
    private JLabel respuesta;
    private JPasswordField passwordField1;


    /**
     * Se crea un menu con las distintas opciones que podrá realizar el usuario
     *
     * @return
     */

    private JMenuBar PrepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Login");
        JMenuItem menuItemRegistrarse = new JMenuItem("Crear Usuario");
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemRegistrarse);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemSalir);


        menuItemRegistrarse.addActionListener(e->{
            Registro registro = new Registro();
            registro.start();
        });

        menuItemSalir.addActionListener(e -> { System.exit(0); });

        return menuBar;
    }

    public void start(){
        this.setVisible(true);
        /**
         * metodo start para modificar la visibilidad del form
         */
    }
    public login() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Peliculas");
        this.setResizable(false);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);
        JMenuBar menuBar = PrepareMenuBar();
        this.setJMenuBar(menuBar);

        iniciarSesiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField2.getText();
                String contraseña = passwordField1.getText();
                boolean valido = false;
                boolean incompleto = false;
                if (email == null || email.isEmpty() || contraseña == null || contraseña.isEmpty()) {
                    incompleto = true;
                    JOptionPane.showMessageDialog(null, "Todos los campos han de ser completados");
                }
                if (!incompleto) {
                    DataServiceUsuarios ds = new CsvDataReaderUsuarios();
                    try {
                        ArrayList<Usuarios> listaUsuarios = ds.findAllUsuarios();
                        for (Usuarios c : listaUsuarios) {
                            if ((c.getEmail().equals(email) || c.getUsuario().equalsIgnoreCase(email)) && c.getContraseña().equals(contraseña)) {
                                Sesion.id = Integer.parseInt(String.valueOf(c.getId()));
                                dispose();
                                inicio inicio = new inicio();
                                inicio.start();
                                valido = true;
                            }

                        }
                        if (!valido) {
                            JOptionPane.showMessageDialog(null, "Datos incorrectos", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    /**
                     * Se comprueba que el usuario o email este en el csv de usuarios y que la contraseña coincida con la introducida
                     */

                }
            }
        });
    }
}
