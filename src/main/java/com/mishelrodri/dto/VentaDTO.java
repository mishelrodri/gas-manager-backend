package com.mishelrodri.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mishelrodri.entities.TipoTransaccion;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VentaDTO(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "America/El_Salvador")
        LocalDateTime fecha,
        String numeroReferencia,
        String tipoTransaccion,
        BigDecimal monto,
        String descripcion,
        String nombreCliente,
        String clienteDui
) {
}
