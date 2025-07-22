package com.mishelrodri.services;

import com.mishelrodri.entities.Tienda;
import com.mishelrodri.entities.Usuario;
import java.util.List;
import java.util.Map;

public interface ITiendaService extends ICrud<Tienda, Long> {
    
    // Métodos específicos de Tienda
    List<Tienda> findByActivoTrue();
    
    List<Tienda> findByNombreContainingIgnoreCase(String nombre);
    
    List<Tienda> findTiendasConTambosPrestados();
    
    List<Tienda> findTiendasDisponiblesParaPrestamo();
    
    List<Tienda> findTiendasConCapacidadMaxima();
    
    Integer sumTotalTambosPrestados();
    
    List<Tienda> findByContactoContainingIgnoreCase(String contacto);
    
    boolean existsByNombre(String nombre);
    
    // Métodos de negocio específicos
    Tienda activateTienda(Long id);
    
    Tienda deactivateTienda(Long id);
    
    Tienda crearTiendaConMovimientoInicial(Tienda tienda, Usuario usuario, String observaciones);
    
    boolean puedePrestarTambos(Long tiendaId, Integer cantidad);
    
    Tienda actualizarTambosPrestados(Long tiendaId, Integer nuevaCantidad);
    
    Integer getTambosDisponiblesParaPrestamo(Long tiendaId);
    
    List<Tienda> buscarTiendas(String termino);
    
    Map<String, Object> getEstadisticasTiendas();
}
