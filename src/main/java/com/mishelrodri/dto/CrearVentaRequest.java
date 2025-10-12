package com.mishelrodri.dto;

import com.mishelrodri.entities.TipoTransaccion;

import java.math.BigDecimal;

public record CrearVentaRequest(
        Long clienteId,
        TipoTransaccion tipoTransaccion,
        BigDecimal monto,
        Integer cantidad,
        String descripcion,
        String numeroReferencia
) {
}
