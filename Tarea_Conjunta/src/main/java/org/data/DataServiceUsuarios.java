package org.data;

import org.models.Usuarios;

import java.io.IOException;
import java.util.ArrayList;

public interface DataServiceUsuarios {

    public ArrayList<Usuarios> findAllUsuarios () throws IOException;
}
