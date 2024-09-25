package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface IgenericService<G, D, ID> {
    List<G> getAll();
    Optional<G> getById(ID id);
    G save(G entity);
    G update(ID id, D dto);
    void delete(ID id);
    D entityToDto(G entity);
    G dtoToEntity(D dto);
}
