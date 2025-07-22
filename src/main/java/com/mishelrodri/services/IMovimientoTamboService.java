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
    
    // Métodos de negocio específicos
    MovimientoTambo registrarPrestamo(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones);
    
    MovimientoTambo registrarDevolucion(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones);
    
    MovimientoTambo registrarAjuste(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones);
    
    MovimientoTambo registrarAjusteInicial(Tienda tienda, Usuario usuario, Integer cantidadInicial, String observaciones);
    
    boolean validarMovimiento(Tienda tienda, TipoMovimientoTambo tipo, Integer cantidad);
    
    Integer calcularSaldoActualTienda(Tienda tienda);
    
    List<MovimientoTambo> getReporteMovimientosPorPeriodo(LocalDateTime inicio, LocalDateTime fin);
    
    Map<String, Object> getEstadisticasMovimientos();
    
    Map<String, Integer> getResumenMovimientosPorTipo();
}
