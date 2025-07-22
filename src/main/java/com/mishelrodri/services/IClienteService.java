package com.mishelrodri.services;

import com.mishelrodri.entities.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteService extends ICrud<Cliente, Long> {
    
    // Métodos específicos de Cliente
    Optional<Cliente> findByDui(String dui);
    
    List<Cliente> findByActivoTrue();
    
    List<Cliente> findByClienteLealTrue();
    
    List<Cliente> findByAñoNavidadActivo(Integer año);
    
    List<Cliente> findByNombreAndApellidoContainingIgnoreCase(String nombre, String apellido);
    
    boolean existsByDui(String dui);
    
    long countClientesLeales();
    
    List<Cliente> findClientesNavideñosDelAño(Integer año);
    
    // Métodos de negocio específicos
    Cliente activateCliente(Long id);
    
    Cliente deactivateCliente(Long id);
    
    Cliente marcarComoClienteLeal(Long id);
    
    Cliente asignarAñoNavidad(Long id, Integer año);
    
    Cliente removerAñoNavidad(Long id);
    
    List<Cliente> buscarClientes(String termino);
}
