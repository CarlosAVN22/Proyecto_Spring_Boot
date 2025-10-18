package com.example.demo.catalogo;

import java.math.BigDecimal;

public interface LugarResumenRow {
    Long getLugarId();
    String getPais();
    String getCiudad();
    String getCategoriaCodigo();
    String getCategoria();
    String getLugar();
    String getDireccion();
    BigDecimal getPrecio();
    BigDecimal getPromedioEstrellas();
    String getImagenPrincipal();
}
