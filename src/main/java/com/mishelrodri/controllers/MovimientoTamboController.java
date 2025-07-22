package com.mishelrodri.controllers;

import com.mishelrodri.dto.Mensaje;
import com.mishelrodri.entities.MovimientoTambo;
import com.mishelrodri.entities.TipoMovimientoTambo;
import com.mishelrodri.entities.Tienda;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.services.IMovimientoTamboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos-tambo")
@CrossOrigin(origins = "*")
public class MovimientoTamboController {
    
    @Autowired
    private IMovimientoTamboService movimientoTamboService;
    
    @GetMapping
    public ResponseEntity<List<MovimientoTambo>> findAll() {
        return ResponseEntity.ok(movimientoTamboService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoTambo> findById(@PathVariable Long id) {
        Optional<MovimientoTambo> movimiento = movimientoTamboService.findById(id);
        return movimiento.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MovimientoTambo> save(@RequestBody MovimientoTambo movimiento) {
        try {
            MovimientoTambo savedMovimiento = movimientoTamboService.save(movimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoTambo> update(@PathVariable Long id, @RequestBody MovimientoTambo movimiento) {
        try {
            movimiento.setId(id);
            MovimientoTambo updatedMovimiento = movimientoTamboService.update(movimiento);
            return ResponseEntity.ok(updatedMovimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable Long id) {
        try {
            movimientoTamboService.deleteById(id);
            return ResponseEntity.ok(new Mensaje("Movimiento eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Error al eliminar el movimiento"));
        }
    }
    
    // Endpoints específicos de MovimientoTambo
    
    @PostMapping("/tienda/{tiendaId}")
    public ResponseEntity<List<MovimientoTambo>> findByTienda(@PathVariable Long tiendaId) {
        // Se necesita obtener la tienda primero
        try {
            Tienda tienda = new Tienda();
            tienda.setId(tiendaId);
            return ResponseEntity.ok(movimientoTamboService.findByTienda(tienda));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoTambo>> findByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            return ResponseEntity.ok(movimientoTamboService.findByUsuario(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tipo/{tipoMovimiento}")
    public ResponseEntity<List<MovimientoTambo>> findByTipoMovimiento(@PathVariable TipoMovimientoTambo tipoMovimiento) {
        return ResponseEntity.ok(movimientoTamboService.findByTipoMovimiento(tipoMovimiento));
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<MovimientoTambo>> findByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(movimientoTamboService.findByFechaBetween(fechaInicio, fechaFin));
    }
    
    @GetMapping("/tienda-fecha/{tiendaId}")
    public ResponseEntity<List<MovimientoTambo>> findByTiendaAndFechaBetween(
            @PathVariable Long tiendaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(tiendaId);
            return ResponseEntity.ok(movimientoTamboService.findByTiendaAndFechaBetween(tienda, fechaInicio, fechaFin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/hoy")
    public ResponseEntity<List<MovimientoTambo>> findMovimientosDelDia() {
        return ResponseEntity.ok(movimientoTamboService.findMovimientosDelDia());
    }
    
    @GetMapping("/suma-tipo/{tipo}")
    public ResponseEntity<Integer> sumCantidadByTipoMovimiento(@PathVariable TipoMovimientoTambo tipo) {
        return ResponseEntity.ok(movimientoTamboService.sumCantidadByTipoMovimiento(tipo));
    }
    
    @GetMapping("/ultimos-tienda/{tiendaId}")
    public ResponseEntity<List<MovimientoTambo>> findUltimosMovimientosByTienda(@PathVariable Long tiendaId) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(tiendaId);
            return ResponseEntity.ok(movimientoTamboService.findUltimosMovimientosByTienda(tienda));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/historial-tienda/{tiendaId}")
    public ResponseEntity<List<MovimientoTambo>> findHistorialCompletoByTienda(@PathVariable Long tiendaId) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(tiendaId);
            return ResponseEntity.ok(movimientoTamboService.findHistorialCompletoByTienda(tienda));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Endpoints para registrar movimientos específicos
    
    @PostMapping("/prestamo")
    public ResponseEntity<MovimientoTambo> registrarPrestamo(@RequestBody PrestamoRequest request) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(request.tiendaId());
            Usuario usuario = new Usuario();
            usuario.setId(request.usuarioId());
            
            MovimientoTambo movimiento = movimientoTamboService.registrarPrestamo(
                tienda, usuario, request.cantidad(), request.observaciones()
            );
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/devolucion")
    public ResponseEntity<MovimientoTambo> registrarDevolucion(@RequestBody DevolucionRequest request) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(request.tiendaId());
            Usuario usuario = new Usuario();
            usuario.setId(request.usuarioId());
            
            MovimientoTambo movimiento = movimientoTamboService.registrarDevolucion(
                tienda, usuario, request.cantidad(), request.observaciones()
            );
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/ajuste")
    public ResponseEntity<MovimientoTambo> registrarAjuste(@RequestBody AjusteRequest request) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(request.tiendaId());
            Usuario usuario = new Usuario();
            usuario.setId(request.usuarioId());
            
            MovimientoTambo movimiento = movimientoTamboService.registrarAjuste(
                tienda, usuario, request.cantidad(), request.observaciones()
            );
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/ajuste-inicial")
    public ResponseEntity<MovimientoTambo> registrarAjusteInicial(@RequestBody AjusteInicialRequest request) {
        try {
            Tienda tienda = new Tienda();
            tienda.setId(request.tiendaId());
            Usuario usuario = new Usuario();
            usuario.setId(request.usuarioId());
            
            MovimientoTambo movimiento = movimientoTamboService.registrarAjusteInicial(
                tienda, usuario, request.cantidadInicial(), request.observaciones()
            );
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Records para las requests
    public record PrestamoRequest(Long tiendaId, Long usuarioId, Integer cantidad, String observaciones) {}
    public record DevolucionRequest(Long tiendaId, Long usuarioId, Integer cantidad, String observaciones) {}
    public record AjusteRequest(Long tiendaId, Long usuarioId, Integer cantidad, String observaciones) {}
    public record AjusteInicialRequest(Long tiendaId, Long usuarioId, Integer cantidadInicial, String observaciones) {}
}
