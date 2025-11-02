package org.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peliculas {
    private Integer id;
    private String titulo;
    private Integer año;
    private String director;
    private String descripcion;
    private String genero;
    private String imagen;
    private Integer usuario;
}
/**
 * Se crea la clase Peliculas
 */