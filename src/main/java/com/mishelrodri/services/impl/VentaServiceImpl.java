package com.mishelrodri.services.impl;

import com.mishelrodri.dto.CrearVentaRequest;
import com.mishelrodri.dto.VentaDTO;
import com.mishelrodri.entities.Cliente;
import com.mishelrodri.entities.TipoTransaccion;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.entities.Venta;
import com.mishelrodri.exceptions.CustomException;
import com.mishelrodri.interfaces.IVentaDTO;
import com.mishelrodri.repositories.ClienteRepository;
import com.mishelrodri.repositories.UsuarioRepository;
import com.mishelrodri.repositories.VentaRepository;
import com.mishelrodri.services.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VentaServiceImpl implements IVentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    public Cliente findClienteById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "El cliente no existe"));
    }

    public Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "El usuario no existe"));
    }

    // Métodos CRUD básicos
    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> findAllVentaDTO() {
        return ventaRepository.findAll().stream()
                .map(venta -> {
                    return VentaDTO.builder()
                            .id(venta.getId())
                            .fecha(venta.getFecha())
                            .numeroReferencia(venta.getNumeroReferencia())
                            .tipoTransaccion(venta.getTipoTransaccion().name())
                            .descripcion(venta.getDescripcion())
                            .nombreCliente(venta.getCliente().getNombre() + " " + venta.getCliente().getApellido())
                            .clienteDui(venta.getCliente().getDui())
                            .cantidad(venta.getCantidad())
                            .build();

                }).collect(Collectors.toList());
    }

    @Override
    public List<Venta> findAll() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta save(Venta entity) {
//        entity.setNumeroReferencia(null);
        System.out.println(entity.getNumeroReferencia());
        return ventaRepository.save(entity);
    }

    @Override
    public Venta update(Venta entity) {
        if (entity.getId() != null && ventaRepository.existsById(entity.getId())) {
            return ventaRepository.save(entity);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Venta no encontrada para actualizar");
    }

    @Override
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return ventaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return ventaRepository.count();
    }

    // Métodos específicos de Venta
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findByNumeroReferencia(String numeroReferencia) {
        return ventaRepository.findByNumeroReferencia(numeroReferencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findByCliente(Cliente cliente) {
        return ventaRepository.findByCliente(cliente);
    }



    @Override
    @Transactional(readOnly = true)
    public List<Venta> findByTipoTransaccion(TipoTransaccion tipoTransaccion) {
        return ventaRepository.findByTipoTransaccion(tipoTransaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findVentasDelDia() {
        return ventaRepository.findVentasDelDia();
    }



    @Override
    @Transactional(readOnly = true)
    public Long sumCantidadByTipoTransaccion(TipoTransaccion tipo) {
        Long resultado = ventaRepository.sumCantidadByTipoTransaccionByDia(tipo.name());
        return resultado != null ? resultado : 0;
    }

    @Transactional(readOnly = true)
    public Long sumCantidadByTipoTransaccionMes(TipoTransaccion tipo) {
        Long resultado = ventaRepository.sumCantidadByTipoTransaccionByMes(tipo.name());
        return resultado != null ? resultado : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findByClienteAndFechaBetween(Cliente cliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByClienteAndFechaBetween(cliente, fechaInicio, fechaFin);
    }


    // Métodos de negocio específicos
    @Override
    public Venta crearVenta(CrearVentaRequest dto) {

        Cliente cliente = findClienteById(dto.clienteId());
        Venta venta = Venta.builder()
                .cliente(cliente)
                .tipoTransaccion(dto.tipoTransaccion())
                .cantidad(dto.cantidad())
                .descripcion(dto.descripcion())
                .numeroReferencia(dto.numeroReferencia().equals("") ? null : dto.numeroReferencia())
                .isSubsidio(dto.isSubsidio())
                .fecha(dto.fecha()).build();

        return save(venta);
    }

    @Override
    public String generarNumeroReferencia() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "REF-" + timestamp;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Venta> getReporteVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return findByFechaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEstadisticasVentas() {
        Map<String, Object> estadisticas = new HashMap<>();
//        System.out.println("✅ACAAA " + ventaRepository.getFecha());
        estadisticas.put("tambosVendidosDia", sumCantidadByTipoTransaccion(TipoTransaccion.VENDIDO));
        estadisticas.put("tambosCompradosDia", sumCantidadByTipoTransaccion(TipoTransaccion.COMPRADO));
        estadisticas.put("tambosVendidosMes", sumCantidadByTipoTransaccionMes(TipoTransaccion.VENDIDO));
        estadisticas.put("tambosCompradosMes", sumCantidadByTipoTransaccionMes(TipoTransaccion.COMPRADO));

//        estadisticas.put("tambosVendidosDelDia", sumCantidadByTipoTransaccion(TipoTransaccion.VENDIDO));
//        estadisticas.put("tambosCompradosDelDia", sumCantidadByTipoTransaccion(TipoTransaccion.COMPRADO));

        return estadisticas;
    }

    @Override
    public List<IVentaDTO> buscarVentas(String busqueda, String fecha) {
        return ventaRepository.buscarVentas(busqueda, fecha);
    }
}
