package org.ui;

import org.data.CsvDataReaderPeliculas;
import org.data.CsvDataReaderUsuarios;
import org.data.DataServicePeliculas;
import org.data.DataServiceUsuarios;
import org.models.Peliculas;
import org.models.Usuarios;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Registro extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField correo;
    private JPasswordField contraseña;
    private JTextField contraseña2;
    private JTextField usuario;

    public Registro() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    void start(){this.setVisible(true);}

    private void onOK() throws IOException {
        DataServiceUsuarios dsu = new CsvDataReaderUsuarios();
        ArrayList<Usuarios> listaUsuarios = dsu.findAllUsuarios();
        boolean coincidencia = false;
        String correo = this.correo.getText();
        String usuario = this.usuario.getText();
        String contraseña = this.contraseña.getText();
        String contraseñaConfirmacion = this.contraseña2.getText();
        Integer id = 0;
        boolean terminado = false;
        boolean error = false;
        if ((correo == null || correo.equals("") || (usuario == null || usuario.equals("")) || (contraseña == null || contraseña.equals("")) || (contraseñaConfirmacion == null || contraseñaConfirmacion.equals("")))) {
            error = true;
            JOptionPane.showMessageDialog(this, "Todos los campos han de ser completados");
        }
        if (!error) {
            for (Usuarios c : listaUsuarios) {

                if (c.getUsuario().equalsIgnoreCase(usuario)) {
                    JOptionPane.showMessageDialog(this, "Este usuario ya esta registrado");
                    coincidencia = true;
                    break;
                } else if (c.getEmail().equalsIgnoreCase(correo)) {
                    JOptionPane.showMessageDialog(this, "Este correo ya esta registrado");
                    coincidencia = true;
                    break;
                }
                if (id < c.getId()) {
                    id = c.getId();
                }

            }
            if (!coincidencia) {
                if (correo.contains("@") && correo.contains(".")) {
                    if (contraseña.equals(contraseñaConfirmacion)) {
                        id++;
                        BufferedWriter bw = new BufferedWriter(new FileWriter("usuarios.csv", true));
                        bw.newLine();
                        bw.write(id + "," + correo + "," + usuario + "," + contraseña);
                        bw.close();
                        terminado = true;
                    } else JOptionPane.showMessageDialog(this, "Las contraseñas no coindicen");
                } else JOptionPane.showMessageDialog(this, "El correo no esta formado correctamente");
            }
            if (terminado) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente");
                dispose();
            }
            /**
             * Se comprueba que el correo introducido ni el usuario esten en uso, despues se comprueba que ambas contraseñas coincidan
             */
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Registro dialog = new Registro();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
