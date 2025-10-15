package com.mishelrodri.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mishelrodri.entities.TipoTransaccion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearVentaRequest(
        Long clienteId,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate fecha,
        TipoTransaccion tipoTransaccion,
        BigDecimal monto,
        Integer cantidad,
        String descripcion,
        String numeroReferencia,
        Boolean isSubsidio
) {
}
