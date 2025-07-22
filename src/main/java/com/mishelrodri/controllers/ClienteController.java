package com.mishelrodri.controllers;

import com.mishelrodri.dto.Mensaje;
import com.mishelrodri.entities.Cliente;
import com.mishelrodri.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
        try {
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            cliente.setId(id);
            Cliente updatedCliente = clienteService.update(cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable Long id) {
        try {
            clienteService.deleteById(id);
            return ResponseEntity.ok(new Mensaje("Cliente eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Error al eliminar el cliente"));
        }
    }
    
    // Endpoints específicos de Cliente
    
    @GetMapping("/dui/{dui}")
    public ResponseEntity<Cliente> findByDui(@PathVariable String dui) {
        Optional<Cliente> cliente = clienteService.findByDui(dui);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> findByActivoTrue() {
        return ResponseEntity.ok(clienteService.findByActivoTrue());
    }
    
    @GetMapping("/leales")
    public ResponseEntity<List<Cliente>> findByClienteLealTrue() {
        return ResponseEntity.ok(clienteService.findByClienteLealTrue());
    }
    
    @GetMapping("/navideños/{año}")
    public ResponseEntity<List<Cliente>> findByAñoNavidadActivo(@PathVariable Integer año) {
        return ResponseEntity.ok(clienteService.findByAñoNavidadActivo(año));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> findByNombreAndApellido(
            @RequestParam String nombre, 
            @RequestParam String apellido) {
        return ResponseEntity.ok(
            clienteService.findByNombreAndApellidoContainingIgnoreCase(nombre, apellido)
        );
    }
    
    @GetMapping("/existe-dui/{dui}")
    public ResponseEntity<Boolean> existsByDui(@PathVariable String dui) {
        return ResponseEntity.ok(clienteService.existsByDui(dui));
    }
    
    @GetMapping("/count-leales")
    public ResponseEntity<Long> countClientesLeales() {
        return ResponseEntity.ok(clienteService.countClientesLeales());
    }
    
    @GetMapping("/navideños-año/{año}")
    public ResponseEntity<List<Cliente>> findClientesNavideñosDelAño(@PathVariable Integer año) {
        return ResponseEntity.ok(clienteService.findClientesNavideñosDelAño(año));
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<Cliente> activateCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.activateCliente(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Cliente> deactivateCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.deactivateCliente(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
