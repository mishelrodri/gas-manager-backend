package com.mishelrodri.services.impl;

import com.mishelrodri.entities.Tienda;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.exceptions.CustomException;
import com.mishelrodri.repositories.TiendaRepository;
import com.mishelrodri.services.ITiendaService;
import com.mishelrodri.services.IMovimientoTamboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TiendaServiceImpl implements ITiendaService {
    
    @Autowired
    private TiendaRepository tiendaRepository;
    
    @Autowired
    @Lazy // Para evitar dependencia circular
    private IMovimientoTamboService movimientoTamboService;
    
    // Métodos CRUD básicos
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> findAll() {
        return tiendaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Tienda> findById(Long id) {
        return tiendaRepository.findById(id);
    }
    
    @Override
    public Tienda save(Tienda entity) {
        // Si es una nueva tienda (sin ID) y tiene tambos prestados iniciales
        boolean esNuevaTienda = entity.getId() == null;
        Integer tambosIniciales = entity.getNumeroTambosPrestados();
        
        Tienda tiendaGuardada = tiendaRepository.save(entity);
        
        // Si es nueva tienda y tiene tambos iniciales, crear movimiento de ajuste
        if (esNuevaTienda && tambosIniciales != null && tambosIniciales > 0) {
            // Necesitamos un usuario para el movimiento, por ahora usaremos null
            // En la práctica, esto debería pasarse como parámetro
            // movimientoTamboService.registrarAjuste(tiendaGuardada, null, tambosIniciales, "Ajuste inicial al crear tienda");
        }
        
        return tiendaGuardada;
    }
    
    @Override
    public Tienda update(Tienda entity) {
        if (entity.getId() != null && tiendaRepository.existsById(entity.getId())) {
            return tiendaRepository.save(entity);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Tienda no encontrada para actualizar");
    }
    
    @Override
    public void deleteById(Long id) {
        tiendaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return tiendaRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return tiendaRepository.count();
    }
    
    // Métodos específicos de Tienda
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> findByActivoTrue() {
        return tiendaRepository.findByActivoTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> findByNombreContainingIgnoreCase(String nombre) {
        return tiendaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> findTiendasConTambosPrestados() {
        return tiendaRepository.findTiendasConTambosPrestados();
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<Tienda> findTiendasDisponiblesParaPrestamo() {
//        return tiendaRepository.findTiendasDisponiblesParaPrestamo();
//    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<Tienda> findTiendasConCapacidadMaxima() {
//        return tiendaRepository.findTiendasConCapacidadMaxima();
//    }
//
    @Override
    @Transactional(readOnly = true)
    public Integer sumTotalTambosPrestados() {
        Integer resultado = tiendaRepository.sumTotalTambosPrestados();
        return resultado != null ? resultado : 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> findByContactoContainingIgnoreCase(String contacto) {
        return tiendaRepository.findByContactoContainingIgnoreCase(contacto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return tiendaRepository.existsByNombre(nombre);
    }
    
    // Métodos de negocio específicos
    @Override
    public Tienda activateTienda(Long id) {
        Optional<Tienda> tienda = findById(id);
        if (tienda.isPresent()) {
            tienda.get().setActivo(true);
            return save(tienda.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Tienda no encontrada");
    }
    
    @Override
    public Tienda deactivateTienda(Long id) {
        Optional<Tienda> tienda = findById(id);
        if (tienda.isPresent()) {
            tienda.get().setActivo(false);
            return save(tienda.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Tienda no encontrada");
    }
    
    @Override
    public Tienda crearTiendaConMovimientoInicial(Tienda tienda, Usuario usuario, String observaciones) {
        // Asegurar que es una nueva tienda
        tienda.setId(null);
        
        // Guardar los tambos iniciales antes de limpiarlos
        Integer tambosIniciales = tienda.getNumeroTambosPrestados();
        if (tambosIniciales == null) {
            tambosIniciales = 0;
        }
        
        // Limpiar los tambos para guardar la tienda con 0 inicialmente
        tienda.setNumeroTambosPrestados(0);
        
        // Guardar la tienda
        Tienda tiendaGuardada = tiendaRepository.save(tienda);
        
        // Si tiene tambos iniciales, crear movimiento de ajuste inicial
        if (tambosIniciales > 0 && usuario != null) {
            try {
                String observacionCompleta = "Ajuste inicial al crear tienda";
                if (observaciones != null && !observaciones.trim().isEmpty()) {
                    observacionCompleta += " - " + observaciones;
                }
                movimientoTamboService.registrarAjusteInicial(tiendaGuardada, usuario, tambosIniciales, observacionCompleta);
            } catch (Exception e) {
                // Si hay error en el movimiento, loguear pero no fallar la creación de la tienda
                System.err.println("Error al crear movimiento inicial para tienda: " + e.getMessage());
            }
        }
        
        return tiendaGuardada;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public boolean puedePrestarTambos(Long tiendaId, Integer cantidad) {
//        Optional<Tienda> tienda = findById(tiendaId);
//        if (tienda.isPresent() && tienda.get().getActivo()) {
//            int tambosPrestados = tienda.get().getNumeroTambosPrestados();
//            return (tambosPrestados + cantidad) <= capacidadMaxima;
//        }
//        return false;
//    }
    
    @Override
    public Tienda actualizarTambosPrestados(Long tiendaId, Integer nuevaCantidad) {
        Optional<Tienda> tienda = findById(tiendaId);
        if (tienda.isPresent()) {
            if (nuevaCantidad >= 0) {
                tienda.get().setNumeroTambosPrestados(nuevaCantidad);
                return save(tienda.get());
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Cantidad de tambos inválida");
            }
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Tienda no encontrada");
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public Integer getTambosDisponiblesParaPrestamo(Long tiendaId) {
//        Optional<Tienda> tienda = findById(tiendaId);
//        if (tienda.isPresent()) {
//            return tienda.get().getNumeroTambosMaximo() - tienda.get().getNumeroTambosPrestados();
//        }
//        return 0;
//    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tienda> buscarTiendas(String termino) {
        return findByNombreContainingIgnoreCase(termino);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEstadisticasTiendas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<Tienda> tiendasActivas = findByActivoTrue();
        List<Tienda> tiendasConTambos = findTiendasConTambosPrestados();
//        List<Tienda> tiendasDisponibles = findTiendasDisponiblesParaPrestamo();
        
        estadisticas.put("totalTiendas", count());
        estadisticas.put("tiendasActivas", tiendasActivas.size());
        estadisticas.put("tiendasConTambos", tiendasConTambos.size());
//        estadisticas.put("tiendasDisponibles", tiendasDisponibles.size());
        estadisticas.put("totalTambosPrestados", sumTotalTambosPrestados());
        
        return estadisticas;
    }
}
