package org.ui;

import org.data.CsvDataReaderPeliculas;
import org.data.DataServicePeliculas;
import org.models.Peliculas;
import org.models.Sesion;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Detalles extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel textImagen;
    private JLabel textGenero;
    private JLabel textDescripcion;
    private JLabel textDirector;
    private JLabel textAño;
    private JLabel textTitulo;
    private JLabel textId;
    private JButton eliminarButton;
    String textoAntiguo = "";
    String textoNuevo = "";

    public Detalles(Peliculas pelicula) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(600, 400);
        textId.setText(String.valueOf(pelicula.getId()));
        textTitulo.setText(pelicula.getTitulo());
        textAño.setText(String.valueOf(pelicula.getAño()));
        textDirector.setText(pelicula.getDirector());
        textDescripcion.setText(pelicula.getDescripcion());
        textGenero.setText(pelicula.getGenero());
        textImagen.setText(pelicula.getImagen());
        buttonCancel.setVisible(false);
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
        textDescripcion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String nuevoTexto = JOptionPane.showInputDialog(null, "Cambiar descripción:", textDescripcion.getText());
                if (nuevoTexto != null && !nuevoTexto.isEmpty()) {
                    textoAntiguo = textDescripcion.getText();
                    textoNuevo = nuevoTexto;
                    textDescripcion.setText(nuevoTexto);
                    buttonOK.setText("Actualizar");
                    buttonCancel.setVisible(true);
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            /**
             * Se elimina la pelicula
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(Detalles.this, "¿Seguro que quieres eliminar esta pelicula", "Eliminar pelicula", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if  (opcion == JOptionPane.YES_OPTION) {

                    try {

                        DataServicePeliculas ds = new CsvDataReaderPeliculas();
                        ArrayList<Peliculas> listaPeliculas = ds.findAllPeliculas();
                        BufferedWriter bw = new BufferedWriter(new FileWriter("peliculas.csv", false));
                        for (Peliculas p : listaPeliculas) {
                            boolean encontrado = false;
                            if (Integer.parseInt(p.getUsuario().toString()) == Sesion.id) {
                                if (p.getTitulo().equals(textTitulo.getText())) {
                                    encontrado = true;
                                }
                            }
                            if (!encontrado) {
                                bw.write(p.getId() + "," + p.getTitulo() + "," + p.getAño() + "," + p.getDirector() + "," + p.getDescripcion() + "," + p.getGenero() + "," + p.getImagen() + "," + p.getUsuario());
                                bw.newLine();
                            }
                        }

                        bw.close();
                        JOptionPane.showMessageDialog(Detalles.this, "Película eliminada correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }

    private void onOK() throws IOException {
        try {
            DataServicePeliculas dsp = new CsvDataReaderPeliculas();
            ArrayList<Peliculas> listaPeliculas = dsp.findAllPeliculas();

            BufferedWriter bw = new BufferedWriter(new FileWriter("peliculas.csv", false));
            for (Peliculas p : listaPeliculas) {
                if (textoAntiguo.equals(p.getDescripcion())) {
                    bw.write(p.getId()+","+p.getTitulo()+","+p.getAño()+","+p.getDirector()+","+textoNuevo+","+p.getGenero()+","+p.getImagen()+","+p.getUsuario());
                    bw.newLine();
                }else{
                    bw.write(p.getId()+","+p.getTitulo()+","+p.getAño()+","+p.getDirector()+","+p.getDescripcion()+","+p.getGenero()+","+p.getImagen()+","+p.getUsuario());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        dispose();
        /**
         * Si se pulsa en ok, en el caso de haber cambiado la descripcion se modificaria el csv añadiendo pelicula tras pelicula y editando la descripcion de la pelicula modificada.
         */
    }

    private void onCancel() throws IOException {
        // add your code here if necessary
        dispose();
    }

}
