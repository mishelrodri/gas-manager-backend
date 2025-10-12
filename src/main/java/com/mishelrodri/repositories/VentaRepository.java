package com.mishelrodri.repositories;

import com.mishelrodri.entities.Venta;
import com.mishelrodri.entities.TipoTransaccion;
import com.mishelrodri.entities.Cliente;
import com.mishelrodri.interfaces.IVentaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar por número de referencia
    Optional<Venta> findByNumeroReferencia(String numeroReferencia);

    // Buscar por cliente
    List<Venta> findByCliente(Cliente cliente);

    // Buscar por tipo de transacción
    List<Venta> findByTipoTransaccion(TipoTransaccion tipoTransaccion);

    // Buscar ventas en un rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fecha DESC")
    List<Venta> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar ventas del día
    @Query(value = "SELECT * FROM venta v WHERE DATE(v.fecha) = CURRENT_DATE ORDER BY v.fecha DESC", nativeQuery = true)
    List<Venta> findVentasDelDia();

    //monto vendido del dia
    @Query(value = "SELECT SUM(v.monto) FROM venta v WHERE DATE(v.fecha) = CURRENT_DATE", nativeQuery = true)
    BigDecimal findMontoVendidoDelDia();

    // Sumar monto total por tipo de transacción
    @Query("SELECT SUM(v.monto) FROM Venta v WHERE v.tipoTransaccion = :tipo")
    BigDecimal sumMontoByTipoTransaccion(@Param("tipo") TipoTransaccion tipo);

    // Contar tambos vendidos/comprados
    @Query("SELECT SUM(v.cantidad) FROM Venta v WHERE v.tipoTransaccion = :tipo")
    Integer sumCantidadByTipoTransaccion(@Param("tipo") TipoTransaccion tipo);

    // Ventas de un cliente en un período
    @Query("SELECT v FROM Venta v WHERE v.cliente = :cliente AND v.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fecha DESC")
    List<Venta> findByClienteAndFechaBetween(@Param("cliente") Cliente cliente, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    // Top clientes por monto
    @Query("SELECT v.cliente, SUM(v.monto) as total FROM Venta v GROUP BY v.cliente ORDER BY total DESC")
    List<Object[]> findTopClientesByMonto();

    //Total de ventas GENERAL
    @Query(value = "SELECT SUM(v.monto) FROM venta v ", nativeQuery = true)
    BigDecimal findTotalVentas();

    // Filtro para traer las ventas

    @Query(value = """
            SELECT
             v.id,
             v.fecha,
             v.numero_referencia as numeroReferencia,
                        v.tipo_transaccion as tipoTransaccion,
                        v.monto,
                        v.descripcion,
             concat(c.nombre, ' ', c.apellido) as nombreCliente,
             c.dui as clienteDui
           FROM venta v
           INNER JOIN cliente c ON v.cliente_id = c.id
            WHERE
             (:fecha IS NULL OR TO_CHAR(v.fecha, 'YYYY-MM-DD') ILIKE CONCAT('%', :fecha, '%'))
             AND (
               :busqueda IS NULL OR (
                    c.nombre ILIKE CONCAT('%', :busqueda, '%')
                 OR c.apellido ILIKE CONCAT('%', :busqueda, '%')
                 OR c.dui ILIKE CONCAT('%', :busqueda, '%')
                 OR v.numero_referencia ILIKE CONCAT('%', :busqueda, '%')
               )
             )
            
            """, nativeQuery = true)
    List<IVentaDTO> buscarVentas(@Param("busqueda") String busqueda, @Param("fecha") String fecha);
}
