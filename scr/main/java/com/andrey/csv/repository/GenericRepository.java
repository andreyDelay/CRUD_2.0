package com.andrey.csv.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    T save(T objectToSave);

    T update(ID id, T newValue);

    T delete(ID id);

    Optional<T> find(ID id);

    List<T> findAll();
}
