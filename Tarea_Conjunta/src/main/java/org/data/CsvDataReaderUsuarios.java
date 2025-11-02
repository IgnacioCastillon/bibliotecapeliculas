package org.data;

import org.models.Usuarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvDataReaderUsuarios implements DataServiceUsuarios {

    /**
     * Se lee el archivo usuarios.csv y se crea un arraylist de usuarios
     * @return
     * @throws IOException
     */

    @Override
    public ArrayList<Usuarios> findAllUsuarios() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("usuarios.csv"));
        String linea;
        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
        while ((linea = br.readLine()) != null){
            String[] partes = linea.split(",");
            Usuarios usuario = new Usuarios();
            usuario.setId(Integer.parseInt(partes[0]));
            usuario.setEmail(partes[1]);
            usuario.setUsuario(partes[2]);
            usuario.setContraseña(partes[3]);
            listaUsuarios.add(usuario);
        }

        return listaUsuarios;
    }

    }


