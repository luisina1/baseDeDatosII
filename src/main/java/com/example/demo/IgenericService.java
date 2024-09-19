package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface IgenericService<G,ID> {
    List<G> getAll();
    Optional<G> getById(ID id);
    G save(G entity);
    void delete(ID id);
}
