package org.ui;

import org.data.CsvDataReaderPeliculas;
import org.data.DataServicePeliculas;
import org.models.Peliculas;
import org.models.Sesion;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Añadir extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tituloText;
    private JTextField añoText;
    private JTextField directorText;
    private JTextField descripcionText;
    private JTextField generoText;
    private JTextField imagenText;

    /**
     * Metodo start para activar la visibilidad del form
     */
    public void start(){this.setVisible(true);}
    public Añadir() throws IOException {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(500,500);
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
                try {
                    onCancel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    onCancel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCancel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws IOException {
        boolean valido = true;
        if (tituloText.getText().isEmpty()||añoText.getText().isEmpty()||directorText.getText().isEmpty()||descripcionText.getText().isEmpty()||generoText.getText().isEmpty()||imagenText.getText().isEmpty()) {
            valido = false;
            JOptionPane.showMessageDialog(this, "Todos los campos han de ser completados");
        }
        /**
         * Al rellenar el formulario y clickar en ok se modifica el csv de peliculas y se le añade la id obteniendo la id de la ultima pelicula y el usuario guardado en la clase sesion
         */
        if (valido) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("peliculas.csv", true));
            DataServicePeliculas dsp = new CsvDataReaderPeliculas();
            ArrayList<Peliculas> listaPeliculas = dsp.findAllPeliculas();
            Peliculas ultimaPelicula = listaPeliculas.getLast();
            Integer nuevaId = ultimaPelicula.getId() + 1;

            bw.write(nuevaId + "," + tituloText.getText() + "," + añoText.getText() + "," + directorText.getText() + "," + descripcionText.getText() + "," + generoText.getText() + "," + imagenText.getText() + "," + Sesion.id);
            bw.newLine();
            bw.close();
            dispose();
        }
    }

    private void onCancel() throws IOException {
        dispose();
    }

    public static void main(String[] args) throws IOException {
        Añadir dialog = new Añadir();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
