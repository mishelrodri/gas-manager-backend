package com.mishelrodri.services.impl;

import com.mishelrodri.entities.MovimientoTambo;
import com.mishelrodri.entities.TipoMovimientoTambo;
import com.mishelrodri.entities.Tienda;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.exceptions.CustomException;
import com.mishelrodri.repositories.MovimientoTamboRepository;
import com.mishelrodri.services.IMovimientoTamboService;
import com.mishelrodri.services.ITiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MovimientoTamboServiceImpl implements IMovimientoTamboService {
    
    @Autowired
    private MovimientoTamboRepository movimientoTamboRepository;
    
    @Autowired
    private ITiendaService tiendaService;
    
    // Métodos CRUD básicos
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findAll() {
        return movimientoTamboRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoTambo> findById(Long id) {
        return movimientoTamboRepository.findById(id);
    }
    
    @Override
    public MovimientoTambo save(MovimientoTambo entity) {
        return movimientoTamboRepository.save(entity);
    }
    
    @Override
    public MovimientoTambo update(MovimientoTambo entity) {
        if (entity.getId() != null && movimientoTamboRepository.existsById(entity.getId())) {
            return movimientoTamboRepository.save(entity);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Movimiento no encontrado para actualizar");
    }
    
    @Override
    public void deleteById(Long id) {
        movimientoTamboRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return movimientoTamboRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return movimientoTamboRepository.count();
    }
    
    // Métodos específicos de MovimientoTambo
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findByTienda(Tienda tienda) {
        return movimientoTamboRepository.findByTienda(tienda);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findByUsuario(Usuario usuario) {
        return movimientoTamboRepository.findByUsuario(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findByTipoMovimiento(TipoMovimientoTambo tipoMovimiento) {
        return movimientoTamboRepository.findByTipoMovimiento(tipoMovimiento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoTamboRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findByTiendaAndFechaBetween(Tienda tienda, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoTamboRepository.findByTiendaAndFechaBetween(tienda, fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findMovimientosDelDia() {
        return movimientoTamboRepository.findMovimientosDelDia();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer sumCantidadByTipoMovimiento(TipoMovimientoTambo tipo) {
        Integer resultado = movimientoTamboRepository.sumCantidadByTipoMovimiento(tipo);
        return resultado != null ? resultado : 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findUltimosMovimientosByTienda(Tienda tienda) {
        return movimientoTamboRepository.findUltimosMovimientosByTienda(tienda);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> findHistorialCompletoByTienda(Tienda tienda) {
        return movimientoTamboRepository.findHistorialCompletoByTienda(tienda);
    }
    
    // Métodos de negocio específicos
    @Override
    public MovimientoTambo registrarPrestamo(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones) {
        if (!validarMovimiento(tienda, TipoMovimientoTambo.PRESTAMO, cantidad)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "No se puede realizar el préstamo. Capacidad insuficiente.");
        }
        
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(tienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.PRESTAMO);
        movimiento.setCantidad(cantidad);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());
        
        // Actualizar tambos prestados en la tienda
        int nuevaCantidad = tienda.getNumeroTambosPrestados() + cantidad;
        tiendaService.actualizarTambosPrestados(tienda.getId(), nuevaCantidad);
        
        return save(movimiento);
    }
    
    @Override
    public MovimientoTambo registrarDevolucion(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones) {
        if (!validarMovimiento(tienda, TipoMovimientoTambo.DEVOLUCION, cantidad)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "No se puede realizar la devolución. Cantidad insuficiente.");
        }
        
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(tienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.DEVOLUCION);
        movimiento.setCantidad(cantidad);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());
        
        // Actualizar tambos prestados en la tienda
        int nuevaCantidad = tienda.getNumeroTambosPrestados() - cantidad;
        tiendaService.actualizarTambosPrestados(tienda.getId(), nuevaCantidad);
        
        return save(movimiento);
    }
    
    @Override
    public MovimientoTambo registrarAjuste(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones) {
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(tienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.AJUSTE);
        movimiento.setCantidad(cantidad);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());
        
        // Para ajustes regulares, la cantidad se suma al total actual
        int nuevaCantidad = tienda.getNumeroTambosPrestados() + cantidad;
        tiendaService.actualizarTambosPrestados(tienda.getId(), nuevaCantidad);
        
        return save(movimiento);
    }
    
    @Override
    public MovimientoTambo registrarAjusteInicial(Tienda tienda, Usuario usuario, Integer cantidadInicial, String observaciones) {
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(tienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.AJUSTE);
        movimiento.setCantidad(cantidadInicial);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());
        
        // Para ajuste inicial, establecer la cantidad exacta (no sumar)
        tiendaService.actualizarTambosPrestados(tienda.getId(), cantidadInicial);
        
        return save(movimiento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validarMovimiento(Tienda tienda, TipoMovimientoTambo tipo, Integer cantidad) {
        if (cantidad <= 0) return false;
        
        switch (tipo) {
            case PRESTAMO:
                return tiendaService.puedePrestarTambos(tienda.getId(), cantidad);
            case DEVOLUCION:
                return tienda.getNumeroTambosPrestados() >= cantidad;
            case AJUSTE:
                return true; // Los ajustes siempre son válidos
            default:
                return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer calcularSaldoActualTienda(Tienda tienda) {
        return tienda.getNumeroTambosPrestados();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoTambo> getReporteMovimientosPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return findByFechaBetween(inicio, fin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEstadisticasMovimientos() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        estadisticas.put("totalMovimientos", count());
        estadisticas.put("movimientosDelDia", findMovimientosDelDia().size());
        estadisticas.put("prestamosDelDia", sumCantidadByTipoMovimiento(TipoMovimientoTambo.PRESTAMO));
        estadisticas.put("devolucionesDelDia", sumCantidadByTipoMovimiento(TipoMovimientoTambo.DEVOLUCION));
        estadisticas.put("ajustesDelDia", sumCantidadByTipoMovimiento(TipoMovimientoTambo.AJUSTE));
        
        return estadisticas;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getResumenMovimientosPorTipo() {
        Map<String, Integer> resumen = new HashMap<>();
        
        resumen.put("PRESTAMO", sumCantidadByTipoMovimiento(TipoMovimientoTambo.PRESTAMO));
        resumen.put("DEVOLUCION", sumCantidadByTipoMovimiento(TipoMovimientoTambo.DEVOLUCION));
        resumen.put("AJUSTE", sumCantidadByTipoMovimiento(TipoMovimientoTambo.AJUSTE));
        
        return resumen;
    }
}
