package com.mishelrodri.services.impl;

import com.mishelrodri.entities.Cliente;
import com.mishelrodri.exceptions.CustomException;
import com.mishelrodri.repositories.ClienteRepository;
import com.mishelrodri.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements IClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    // Métodos CRUD básicos
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAllByOrderByNombreAsc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }
    
    @Override
    public Cliente save(Cliente entity) {
        return clienteRepository.save(entity);
    }
    
    @Override
    public Cliente update(Cliente entity) {
        if (entity.getId() != null && clienteRepository.existsById(entity.getId())) {
            return clienteRepository.save(entity);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado para actualizar");
    }
    
    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return clienteRepository.count();
    }
    
    // Métodos específicos de Cliente
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findByDui(String dui) {
        return clienteRepository.findByDui(dui);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findByActivoTrue() {
        return clienteRepository.findByActivoTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findByClienteLealTrue() {
        return clienteRepository.findByClienteLealTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findByAñoNavidadActivo(Integer año) {
        return clienteRepository.findByAñoNavidadActivo(año);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findByNombreAndApellidoContainingIgnoreCase(String nombre, String apellido) {
        return clienteRepository.findByNombreAndApellidoContainingIgnoreCase(nombre, apellido);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDui(String dui) {
        return clienteRepository.existsByDui(dui);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countClientesLeales() {
        return clienteRepository.countClientesLeales();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findClientesNavideñosDelAño(Integer año) {
        return clienteRepository.findClientesNavideñosDelAño(año);
    }
    
    // Métodos de negocio específicos
    @Override
    public Cliente activateCliente(Long id) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isPresent()) {
            cliente.get().setActivo(true);
            return save(cliente.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }
    
    @Override
    public Cliente deactivateCliente(Long id) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isPresent()) {
            cliente.get().setActivo(false);
            return save(cliente.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }
    
    @Override
    public Cliente marcarComoClienteLeal(Long id) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isPresent()) {
            cliente.get().setClienteLeal(true);
            return save(cliente.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }
    
    @Override
    public Cliente asignarAñoNavidad(Long id, Integer año) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isPresent()) {
            cliente.get().setAñoNavidadActivo(año);
            return save(cliente.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }
    
    @Override
    public Cliente removerAñoNavidad(Long id) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isPresent()) {
            cliente.get().setAñoNavidadActivo(null);
            return save(cliente.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientes(String termino) {
        // Buscar por nombre o apellido que contengan el término
        return clienteRepository.findByNombreAndApellidoContainingIgnoreCase(termino, termino);
    }
}
