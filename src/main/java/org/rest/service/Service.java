package org.rest.service;

import org.rest.exception.NotFoundException;

import java.util.List;

public interface Service <T, K>{
    T save(T t);

    T findById(K k) throws NotFoundException;

    List<T> findAll();

    void update(T t) throws NotFoundException;

    boolean delete(K k) throws NotFoundException;
}
