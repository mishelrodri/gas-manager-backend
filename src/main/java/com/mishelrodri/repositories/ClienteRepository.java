package com.mishelrodri.repositories;

import com.mishelrodri.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByOrderByNombreAsc();

    // Buscar por DUI
    Optional<Cliente> findByDui(String dui);
    
    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
    
    // Buscar clientes leales
    List<Cliente> findByClienteLealTrue();
    
    // Buscar por año navideño activo
    List<Cliente> findByAñoNavidadActivo(Integer año);
    
    // Buscar por nombre y apellido (case insensitive)
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND LOWER(c.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Cliente> findByNombreAndApellidoContainingIgnoreCase(@Param("nombre") String nombre, @Param("apellido") String apellido);
    
    // Verificar si existe DUI
    boolean existsByDui(String dui);
    
    // Contar clientes leales
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.clienteLeal = true AND c.activo = true")
    long countClientesLeales();
    
    // Clientes navideños del año actual
    @Query("SELECT c FROM Cliente c WHERE c.añoNavidadActivo = :año AND c.activo = true")
    List<Cliente> findClientesNavideñosDelAño(@Param("año") Integer año);
}
