package com.example.demo.catalogo;

import java.math.BigDecimal;

public interface LugarDetalleRow {
    Long getLugarId();
    String getLugar();
    String getDireccion();
    BigDecimal getPrecio();
    BigDecimal getPromedioEstrellas();
    String getCategoria();
    String getCategoriaCodigo();
    String getPais();
    String getCiudad();
}
