package com.quality.service;

import java.util.List;

public interface IOperations<T, ID> {
    T save(T t);

    T update(T t, ID id);

    List<T> findAll();

    T findById(ID id);

    void delete(ID id);
}
