package com.mishelrodri.repositories;

import com.mishelrodri.entities.MovimientoTambo;
import com.mishelrodri.entities.TipoMovimientoTambo;
import com.mishelrodri.entities.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoTamboRepository extends JpaRepository<MovimientoTambo, Long> {
    
    // Buscar por tienda
    List<MovimientoTambo> findByTienda(Tienda tienda);
    
    // Buscar por tipo de movimiento
    List<MovimientoTambo> findByTipoMovimiento(TipoMovimientoTambo tipoMovimiento);
    
    // Buscar movimientos en un rango de fechas
    @Query("SELECT m FROM MovimientoTambo m WHERE m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<MovimientoTambo> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar movimientos de una tienda en un período
    @Query("SELECT m FROM MovimientoTambo m WHERE m.tienda = :tienda AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<MovimientoTambo> findByTiendaAndFechaBetween(@Param("tienda") Tienda tienda, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    // Movimientos del día
    @Query(value = "SELECT m FROM movimiento_tambo m WHERE DATE(m.fecha) = CURRENT_DATE ORDER BY m.fecha DESC", nativeQuery = true)
    List<MovimientoTambo> findMovimientosDelDia();
    
    // Sumar cantidad por tipo de movimiento
    @Query("SELECT SUM(m.cantidad) FROM MovimientoTambo m WHERE m.tipoMovimiento = :tipo")
    Integer sumCantidadByTipoMovimiento(@Param("tipo") TipoMovimientoTambo tipo);
    
    // Últimos movimientos de una tienda
    @Query("SELECT m FROM MovimientoTambo m WHERE m.tienda = :tienda ORDER BY m.fecha DESC")
    List<MovimientoTambo> findUltimosMovimientosByTienda(@Param("tienda") Tienda tienda);
    
    // Historial completo de una tienda ordenado por fecha
    @Query("SELECT m FROM MovimientoTambo m WHERE m.tienda = :tienda ORDER BY m.fecha ASC")
    List<MovimientoTambo> findHistorialCompletoByTienda(@Param("tienda") Tienda tienda);
}
