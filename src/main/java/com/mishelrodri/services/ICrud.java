package com.mishelrodri.services;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica CRUD para servicios
 * @param <T> Tipo de entidad
 * @param <ID> Tipo del identificador
 */
public interface ICrud<T, ID> {
    
    /**
     * Obtener todas las entidades
     * @return Lista de todas las entidades
     */
    List<T> findAll();
    
    /**
     * Buscar entidad por ID
     * @param id Identificador de la entidad
     * @return Optional con la entidad encontrada
     */
    Optional<T> findById(ID id);
    
    /**
     * Guardar una nueva entidad
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);
    
    /**
     * Actualizar una entidad existente
     * @param entity Entidad a actualizar
     * @return Entidad actualizada
     */
    T update(T entity);
    
    /**
     * Eliminar entidad por ID
     * @param id Identificador de la entidad a eliminar
     */
    void deleteById(ID id);
    
    /**
     * Verificar si existe una entidad por ID
     * @param id Identificador de la entidad
     * @return true si existe, false en caso contrario
     */
    boolean existsById(ID id);
    
    /**
     * Contar total de entidades
     * @return Número total de entidades
     */
    long count();
}
