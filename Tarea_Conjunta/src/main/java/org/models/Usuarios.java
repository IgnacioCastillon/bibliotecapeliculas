package org.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios {
    private Integer id;
    private String email;
    private String usuario;
    private String contraseña;
}

/**
 * Se crea la clase Usuarios
 */
