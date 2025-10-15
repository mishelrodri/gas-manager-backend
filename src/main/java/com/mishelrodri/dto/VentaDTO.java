package com.mishelrodri.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VentaDTO(
        Long id,

        LocalDate fecha,
        String numeroReferencia,
        String tipoTransaccion,
        String descripcion,
        String nombreCliente,
        String clienteDui,
        Integer cantidad,
        Boolean isSubsidio
) {
}
