package com.mishelrodri.controllers;

import com.mishelrodri.dto.Mensaje;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.entities.Rol;
import com.mishelrodri.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        try {
            Usuario savedUsuario = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            usuario.setId(id);
            Usuario updatedUsuario = usuarioService.update(usuario);
            return ResponseEntity.ok(updatedUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok(new Mensaje("Usuario eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Error al eliminar el usuario"));
        }
    }
    
    // Endpoints específicos de Usuario
    
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> findByUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> findByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> findByActivoTrue() {
        return ResponseEntity.ok(usuarioService.findByActivoTrue());
    }
    
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> findByRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(usuarioService.findByRol(rol));
    }
    
    @GetMapping("/existe-username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.existsByUsername(username));
    }
    
    @GetMapping("/existe-email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.existsByEmail(email));
    }
    
    @GetMapping("/count-activos")
    public ResponseEntity<Long> countActiveUsers() {
        return ResponseEntity.ok(usuarioService.countActiveUsers());
    }
    
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validateUser(@RequestBody ValidateUserRequest request) {
        boolean isValid = usuarioService.validateUser(request.username(), request.password());
        return ResponseEntity.ok(isValid);
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<Usuario> activateUser(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.activateUser(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> deactivateUser(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.deactivateUser(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Record para la validación de usuario
    public record ValidateUserRequest(String username, String password) {}
}
