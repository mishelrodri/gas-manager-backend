package com.mishelrodri.controllers;

import com.mishelrodri.dto.CrearVentaRequest;
import com.mishelrodri.dto.Mensaje;
import com.mishelrodri.dto.VentaDTO;
import com.mishelrodri.entities.Venta;
import com.mishelrodri.entities.TipoTransaccion;
import com.mishelrodri.entities.Cliente;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.services.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {
    
    @Autowired
    private IVentaService ventaService;
    
    @GetMapping
    public ResponseEntity<List<VentaDTO>> findAll() {
        return ResponseEntity.ok(ventaService.findAllVentaDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venta> findById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.findById(id);
        return venta.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Venta> save(@RequestBody Venta venta) {
        try {
            Venta savedVenta = ventaService.save(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVenta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            venta.setId(id);
            Venta updatedVenta = ventaService.update(venta);
            return ResponseEntity.ok(updatedVenta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable Long id) {
        try {
            ventaService.deleteById(id);
            return ResponseEntity.ok(new Mensaje("Venta eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Error al eliminar la venta"));
        }
    }
    
    // Endpoints específicos de Venta
    
    @GetMapping("/numero-referencia/{numeroReferencia}")
    public ResponseEntity<Venta> findByNumeroReferencia(@PathVariable String numeroReferencia) {
        Optional<Venta> venta = ventaService.findByNumeroReferencia(numeroReferencia);
        return venta.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> findByCliente(@PathVariable Long clienteId) {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            return ResponseEntity.ok(ventaService.findByCliente(cliente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Venta>> findByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            return ResponseEntity.ok(ventaService.findByUsuario(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tipo/{tipoTransaccion}")
    public ResponseEntity<List<Venta>> findByTipoTransaccion(@PathVariable TipoTransaccion tipoTransaccion) {
        return ResponseEntity.ok(ventaService.findByTipoTransaccion(tipoTransaccion));
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> findByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(ventaService.findByFechaBetween(fechaInicio, fechaFin));
    }
    
    @GetMapping("/hoy")
    public ResponseEntity<List<VentaDTO>> findVentasDelDia() {
        List<Venta> ventas = ventaService.findVentasDelDia();
        List<VentaDTO> ventasDTO = ventas.stream()
            .map(venta -> new VentaDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getNumeroReferencia(),
                venta.getTipoTransaccion().toString(),
                venta.getMonto(),
                venta.getDescripcion(),
                venta.getCliente().getNombre(),
                venta.getUsuario().getNombre(),
                venta.getCliente().getDui()
            ))
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ventasDTO);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> estadisticasVentas(){
        return ResponseEntity.ok(ventaService.getEstadisticasVentas());
    }
    
//    @GetMapping("/suma-monto/{tipo}")
//    public ResponseEntity<BigDecimal> sumMontoByTipoTransaccion(@PathVariable TipoTransaccion tipo) {
//        return ResponseEntity.ok(ventaService.sumMontoByTipoTransaccion(tipo));
//    }
    
    @GetMapping("/suma-cantidad/{tipo}")
    public ResponseEntity<Integer> sumCantidadByTipoTransaccion(@PathVariable TipoTransaccion tipo) {
        return ResponseEntity.ok(ventaService.sumCantidadByTipoTransaccion(tipo));
    }
    
    @GetMapping("/cliente-fecha/{clienteId}")
    public ResponseEntity<List<Venta>> findByClienteAndFechaBetween(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            return ResponseEntity.ok(ventaService.findByClienteAndFechaBetween(cliente, fechaInicio, fechaFin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/top-clientes")
    public ResponseEntity<List<Object[]>> findTopClientesByMonto() {
        return ResponseEntity.ok(ventaService.findTopClientesByMonto());
    }
    
    @PostMapping("/crear")
    public ResponseEntity<Venta> crearVenta(@RequestBody CrearVentaRequest request) {
        try {
            Venta venta = ventaService.crearVenta(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/generar-numero-referencia")
    public ResponseEntity<String> generarNumeroReferencia() {
        return ResponseEntity.ok(ventaService.generarNumeroReferencia());
    }

}
