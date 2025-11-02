package org.data;

import org.models.Peliculas;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvDataReaderPeliculas implements DataServicePeliculas {
    /**
     * Se lee el archivo peliculas.csv y se devuelve un arraylist de peliculas
     * @return
     * @throws IOException
     */
    @Override
    public ArrayList<Peliculas> findAllPeliculas() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("peliculas.csv"));
        String linea;
        ArrayList<Peliculas> listaPeliculas = new ArrayList<>();
        while ((linea = br.readLine()) != null){
            String[] partes = linea.split(",");
            Peliculas pelicula = new Peliculas();
            pelicula.setId(Integer.parseInt(partes[0]));
            pelicula.setTitulo(partes[1]);
            pelicula.setAño(Integer.parseInt(partes[2]));
            pelicula.setDirector(partes[3]);
            pelicula.setDescripcion(partes[4]);
            pelicula.setGenero(partes[5]);
            pelicula.setImagen(partes[6]);
            pelicula.setUsuario(Integer.parseInt(partes[7]));
            listaPeliculas.add(pelicula);
        }


        return listaPeliculas;
    }

}
