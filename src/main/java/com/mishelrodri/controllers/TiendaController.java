package com.mishelrodri.controllers;

import com.mishelrodri.dto.Mensaje;
import com.mishelrodri.entities.Tienda;
import com.mishelrodri.services.ITiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tiendas")
@CrossOrigin(origins = "*")
public class TiendaController {
    
    @Autowired
    private ITiendaService tiendaService;
    
    @GetMapping
    public ResponseEntity<List<Tienda>> findAll() {
        return ResponseEntity.ok(tiendaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tienda> findById(@PathVariable Long id) {
        Optional<Tienda> tienda = tiendaService.findById(id);
        return tienda.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Tienda> save(@RequestBody Tienda tienda) {
        try {
            Tienda savedTienda = tiendaService.save(tienda);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTienda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tienda> update(@PathVariable Long id, @RequestBody Tienda tienda) {
        try {
            tienda.setId(id);
            Tienda updatedTienda = tiendaService.update(tienda);
            return ResponseEntity.ok(updatedTienda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable Long id) {
        try {
            tiendaService.deleteById(id);
            return ResponseEntity.ok(new Mensaje("Tienda eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Error al eliminar la tienda"));
        }
    }
    
    // Endpoints específicos de Tienda
    
    @GetMapping("/activas")
    public ResponseEntity<List<Tienda>> findByActivoTrue() {
        return ResponseEntity.ok(tiendaService.findByActivoTrue());
    }
    
    @GetMapping("/buscar-nombre")
    public ResponseEntity<List<Tienda>> findByNombreContaining(@RequestParam String nombre) {
        return ResponseEntity.ok(tiendaService.findByNombreContainingIgnoreCase(nombre));
    }
    
    @GetMapping("/con-tambos-prestados")
    public ResponseEntity<List<Tienda>> findTiendasConTambosPrestados() {
        return ResponseEntity.ok(tiendaService.findTiendasConTambosPrestados());
    }
    
    @GetMapping("/disponibles-prestamo")
    public ResponseEntity<List<Tienda>> findTiendasDisponiblesParaPrestamo() {
        return ResponseEntity.ok(tiendaService.findTiendasDisponiblesParaPrestamo());
    }
    
    @GetMapping("/capacidad-maxima")
    public ResponseEntity<List<Tienda>> findTiendasConCapacidadMaxima() {
        return ResponseEntity.ok(tiendaService.findTiendasConCapacidadMaxima());
    }
    
    @GetMapping("/total-tambos-prestados")
    public ResponseEntity<Integer> sumTotalTambosPrestados() {
        return ResponseEntity.ok(tiendaService.sumTotalTambosPrestados());
    }
    
    @GetMapping("/buscar-contacto")
    public ResponseEntity<List<Tienda>> findByContactoContaining(@RequestParam String contacto) {
        return ResponseEntity.ok(tiendaService.findByContactoContainingIgnoreCase(contacto));
    }
    
    @GetMapping("/existe-nombre/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(tiendaService.existsByNombre(nombre));
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<Tienda> activateTienda(@PathVariable Long id) {
        try {
            Tienda tienda = tiendaService.activateTienda(id);
            return ResponseEntity.ok(tienda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Tienda> deactivateTienda(@PathVariable Long id) {
        try {
            Tienda tienda = tiendaService.deactivateTienda(id);
            return ResponseEntity.ok(tienda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
