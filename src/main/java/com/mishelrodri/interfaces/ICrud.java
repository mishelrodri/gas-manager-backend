package com.mishelrodri.interfaces;

import com.mishelrodri.dto.Mensaje;

import java.util.List;

public interface ICrud<T> {

    List<T> findAll();

    Mensaje save(T obj);

    Mensaje delete(Long id);

    Mensaje update(T obj);

}
