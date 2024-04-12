package org.rest.repository;

import java.util.List;

public interface Repository <T, K>{
    T save(T user);
    T findById(K id);
    List<T> findAll();
    void update(T t);
    boolean deleteById(K id);
    boolean existById(K id);
}
