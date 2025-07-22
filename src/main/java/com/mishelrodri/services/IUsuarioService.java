package com.mishelrodri.services;

import com.mishelrodri.entities.Usuario;
import com.mishelrodri.entities.Rol;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService extends ICrud<Usuario, Long> {
    
    // Métodos específicos de Usuario
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByActivoTrue();
    
    List<Usuario> findByRol(Rol rol);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    long countActiveUsers();
    
    // Métodos de autenticación/validación
    boolean validateUser(String username, String password);
    
    Usuario activateUser(Long id);
    
    Usuario deactivateUser(Long id);
}
