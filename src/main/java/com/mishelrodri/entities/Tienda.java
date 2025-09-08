package com.mishelrodri.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tienda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "numero_tambos_prestados", nullable = false)
    private Integer numeroTambosPrestados = 0;

    
    @Column(length = 200)
    private String direccion;
    
    @Column(length = 15)
    private String telefono;
    
    @Column(length = 100)
    private String contacto; // Nombre del contacto en la tienda
    
    @Column(name = "fecha_convenio")
    private LocalDateTime fechaConvenio;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(length = 500)
    private String observaciones;
    
    // Relaciones
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoTambo> movimientos;
    
    @PrePersist
    protected void onCreate() {
        if (fechaConvenio == null) {
            fechaConvenio = LocalDateTime.now();
        }
    }
}
