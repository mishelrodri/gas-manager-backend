package com.mishelrodri.repositories;

import com.mishelrodri.entities.Usuario;
import com.mishelrodri.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por username
    Optional<Usuario> findByUsername(String username);
    
    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar por rol
    List<Usuario> findByRol(Rol rol);
    
    // Verificar si existe username
    boolean existsByUsername(String username);
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Contar usuarios activos
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countActiveUsers();
}
