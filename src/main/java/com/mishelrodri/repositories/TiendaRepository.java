package com.mishelrodri.repositories;

import com.mishelrodri.entities.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    
    // Buscar tiendas activas
    List<Tienda> findByActivoTrue();
    
    // Buscar por nombre (case insensitive)
    @Query("SELECT t FROM Tienda t WHERE LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Tienda> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar tiendas con tambos prestados
    @Query("SELECT t FROM Tienda t WHERE t.numeroTambosPrestados > 0 AND t.activo = true")
    List<Tienda> findTiendasConTambosPrestados();
    
    // Buscar tiendas que pueden recibir más tambos
    @Query("SELECT t FROM Tienda t WHERE t.numeroTambosPrestados < t.numeroTambosMaximo AND t.activo = true")
    List<Tienda> findTiendasDisponiblesParaPrestamo();
    
    // Buscar tiendas con capacidad máxima alcanzada
    @Query("SELECT t FROM Tienda t WHERE t.numeroTambosPrestados >= t.numeroTambosMaximo AND t.activo = true")
    List<Tienda> findTiendasConCapacidadMaxima();
    
    // Sumar total de tambos prestados
    @Query("SELECT SUM(t.numeroTambosPrestados) FROM Tienda t WHERE t.activo = true")
    Integer sumTotalTambosPrestados();
    
    // Buscar por contacto
    @Query("SELECT t FROM Tienda t WHERE LOWER(t.contacto) LIKE LOWER(CONCAT('%', :contacto, '%'))")
    List<Tienda> findByContactoContainingIgnoreCase(@Param("contacto") String contacto);
    
    // Verificar si existe nombre de tienda
    boolean existsByNombre(String nombre);
}
