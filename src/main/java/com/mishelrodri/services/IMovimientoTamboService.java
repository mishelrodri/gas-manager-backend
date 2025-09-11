package com.mishelrodri.services;

import com.mishelrodri.entities.MovimientoTambo;
import com.mishelrodri.entities.TipoMovimientoTambo;
import com.mishelrodri.entities.Tienda;
import com.mishelrodri.entities.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IMovimientoTamboService extends ICrud<MovimientoTambo, Long> {
    
    // Métodos específicos de MovimientoTambo
    List<MovimientoTambo> findByTienda(Tienda tienda);
    
    List<MovimientoTambo> findByUsuario(Usuario usuario);
    
    List<MovimientoTambo> findByTipoMovimiento(TipoMovimientoTambo tipoMovimiento);
    
    List<MovimientoTambo> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<MovimientoTambo> findByTiendaAndFechaBetween(Tienda tienda, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<MovimientoTambo> findMovimientosDelDia();
    
    Integer sumCantidadByTipoMovimiento(TipoMovimientoTambo tipo);
    
    List<MovimientoTambo> findUltimosMovimientosByTienda(Tienda tienda);
    
    List<MovimientoTambo> findHistorialCompletoByTienda(Tienda tienda);
    
    // Métodos de negocio simplificados
    MovimientoTambo registrarPrestamo(Long tienda, Usuario usuario, Integer cantidad, String observaciones);
    MovimientoTambo registrarOnlyPrestamo(Long tienda, Usuario usuario, Integer cantidad, String observaciones);

    MovimientoTambo registrarDevolucion(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones);
}
