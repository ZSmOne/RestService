package org.rest.service;

import java.util.List;

public interface Service <T, K> {
    T save(T t);

    T findById(K k);

    List<T> findAll();

    void update(T t);

    void delete(K k);
}

