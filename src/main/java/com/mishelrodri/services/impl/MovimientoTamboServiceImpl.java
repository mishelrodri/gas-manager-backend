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
    
    // Métodos de negocio simplificados
    @Override
    public MovimientoTambo registrarPrestamo(Long tienda, Usuario usuario, Integer cantidad, String observaciones) {

        Tienda objTienda = tiendaService.findById(tienda).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"La tienda no existe"));
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(objTienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.PRESTAMO);
        movimiento.setCantidad(cantidad);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());
        
        // Actualizar tambos prestados en la tienda
        int nuevaCantidad = objTienda.getNumeroTambosPrestados() + cantidad;
        tiendaService.actualizarTambosPrestados(tienda, nuevaCantidad);
        
        return save(movimiento);
    }

    @Override
    public MovimientoTambo registrarOnlyPrestamo(Long tienda, Usuario usuario, Integer cantidad, String observaciones) {

        Tienda objTienda = tiendaService.findById(tienda).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"La tienda no existe"));
        MovimientoTambo movimiento = new MovimientoTambo();
        movimiento.setTienda(objTienda);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(TipoMovimientoTambo.PRESTAMO);
        movimiento.setCantidad(cantidad);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());

        return save(movimiento);
    }
    
    @Override
    public MovimientoTambo registrarDevolucion(Tienda tienda, Usuario usuario, Integer cantidad, String observaciones) {
//       try{
           Tienda objTienda = tiendaService.findById(tienda.getId()).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"La tienda no existe"));

           if (objTienda.getNumeroTambosPrestados() < cantidad){
               throw new CustomException(HttpStatus.BAD_REQUEST, "Esta intentado devolver más tambos de los prestados");
           }

           MovimientoTambo movimiento = new MovimientoTambo();

           movimiento.setTienda(objTienda);
           movimiento.setUsuario(usuario);
           movimiento.setTipoMovimiento(TipoMovimientoTambo.DEVOLUCION);
           movimiento.setCantidad(cantidad);
           movimiento.setObservaciones(observaciones);
           movimiento.setFecha(LocalDateTime.now());

           // Actualizar tambos prestados en la tienda
           int nuevaCantidad = objTienda.getNumeroTambosPrestados() - cantidad;

           tiendaService.actualizarTambosPrestados(objTienda.getId(), nuevaCantidad);

           return save(movimiento);
//       }catch (Exception e){
//           System.out.println("🔴🔴🔴🔴🔴 " + e);
//       }
//       return null;
    }
}
