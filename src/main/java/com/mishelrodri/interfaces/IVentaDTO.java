package com.mishelrodri.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IVentaDTO {
        Long getId();
        LocalDateTime getFecha();
        String getNumeroReferencia();
        String getTipoTransaccion();
        BigDecimal getMonto();
        String getDescripcion();
        String getNombreCliente();
        String getClienteDui();
//    String usuario();


}
