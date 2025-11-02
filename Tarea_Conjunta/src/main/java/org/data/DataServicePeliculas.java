package org.data;

import org.models.Peliculas;

import java.io.IOException;
import java.util.ArrayList;

public interface DataServicePeliculas {

    public ArrayList<Peliculas> findAllPeliculas() throws IOException;

}
