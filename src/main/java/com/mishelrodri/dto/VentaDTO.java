package com.mishelrodri.dto;

import com.mishelrodri.entities.TipoTransaccion;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VentaDTO(
        Long id,
        LocalDateTime fecha,
        String numeroReferencia,
        String tipoTransaccion,
        BigDecimal monto,
        String descripcion,
        String nombreCliente,
        String clienteDui,
        String usuario
) {
}
