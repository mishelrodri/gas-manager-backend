package com.mishelrodri.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_tambo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoTambo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimientoTambo tipoMovimiento;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(length = 500)
    private String observaciones;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id", nullable = false)
    private Tienda tienda;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}
