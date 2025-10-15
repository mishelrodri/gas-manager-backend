package com.mishelrodri.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate fecha;
    
    @Column(name = "numero_referencia", unique = true)
    private String numeroReferencia;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", nullable = false)
    private TipoTransaccion tipoTransaccion;
    
//    @Column(nullable = false, precision = 10, scale = 2)
//    private BigDecimal monto;
    
    @Column(nullable = false)
    private Integer cantidad; // Cantidad de tambos
    
    @Column(length = 500)
    private String descripcion;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;

    @Column(name = "is_subsidio", nullable = false)
    private boolean isSubsidio = false;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "usuario_id", nullable = false)
//    private Usuario usuario;
    
//    @PrePersist
//    protected void onCreate() {
//        fecha = LocalDateTime.now();
//    }
}
