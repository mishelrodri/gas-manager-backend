package com.mishelrodri.services.impl;

import com.mishelrodri.entities.Usuario;
import com.mishelrodri.entities.Rol;
import com.mishelrodri.exceptions.CustomException;
import com.mishelrodri.repositories.UsuarioRepository;
import com.mishelrodri.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Métodos CRUD básicos
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    public Usuario save(Usuario entity) {
        return usuarioRepository.save(entity);
    }
    
    @Override
    public Usuario update(Usuario entity) {
        if (entity.getId() != null && usuarioRepository.existsById(entity.getId())) {
            return usuarioRepository.save(entity);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado para actualizar");
    }
    
    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return usuarioRepository.count();
    }
    
    // Métodos específicos de Usuario
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByActivoTrue() {
        return usuarioRepository.findByActivoTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return usuarioRepository.countActiveUsers();
    }
    
    // Métodos de negocio específicos
    @Override
    @Transactional(readOnly = true)
    public boolean validateUser(String username, String password) {
        Optional<Usuario> usuario = findByUsername(username);
        return usuario.isPresent() && 
               usuario.get().getPassword().equals(password) && 
               usuario.get().getActivo();
    }
    
    @Override
    public Usuario activateUser(Long id) {
        Optional<Usuario> usuario = findById(id);
        if (usuario.isPresent()) {
            usuario.get().setActivo(true);
            return save(usuario.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }
    
    @Override
    public Usuario deactivateUser(Long id) {
        Optional<Usuario> usuario = findById(id);
        if (usuario.isPresent()) {
            usuario.get().setActivo(false);
//            usuario.get().setUltimoAcceso(LocalDateTime.now());
            return save(usuario.get());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }
}
