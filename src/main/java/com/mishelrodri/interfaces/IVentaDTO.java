package com.mishelrodri.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IVentaDTO {
        Long getId();
        LocalDate getFecha();
        Integer getCantidad();
//        String getNumeroReferencia();
        String getTipoTransaccion();
        BigDecimal getMonto();
        String getDescripcion();
        String getNombreCliente();
        String getClienteDui();
        Boolean getIsSubsidio();
//    String usuario();


}
