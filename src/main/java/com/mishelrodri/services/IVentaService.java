package com.mishelrodri.services;

import com.mishelrodri.dto.CrearVentaRequest;
import com.mishelrodri.dto.VentaDTO;
import com.mishelrodri.entities.Venta;
import com.mishelrodri.entities.TipoTransaccion;
import com.mishelrodri.entities.Cliente;
import com.mishelrodri.entities.Usuario;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IVentaService extends ICrud<Venta, Long> {
    
    // Métodos específicos de Venta
    Optional<Venta> findByNumeroReferencia(String numeroReferencia);
    
    List<Venta> findByCliente(Cliente cliente);

    List<VentaDTO> findAllVentaDTO();
    
    List<Venta> findByUsuario(Usuario usuario);
    
    List<Venta> findByTipoTransaccion(TipoTransaccion tipoTransaccion);
    
    List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Venta> findVentasDelDia();
    
    BigDecimal sumMontoByTipoTransaccion(TipoTransaccion tipo);
    
    Integer sumCantidadByTipoTransaccion(TipoTransaccion tipo);
    
    List<Venta> findByClienteAndFechaBetween(Cliente cliente, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Object[]> findTopClientesByMonto();
    
    // Métodos de negocio específicos
    Venta crearVenta(CrearVentaRequest dto);
    
    String generarNumeroReferencia();
    
    BigDecimal calcularTotalVentasDelDia();
    
    BigDecimal calcularTotalComprasDelDia();
    
    List<Venta> getReporteVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);
    
    Map<String, Object> getEstadisticasVentas();
}
