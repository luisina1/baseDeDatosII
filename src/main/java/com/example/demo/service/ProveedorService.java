package com.example.demo.service;

import com.example.demo.IgenericService;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Proveedor;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService implements IgenericService<Proveedor, Integer> {

    @Autowired
    ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> getById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor entity) {
        return proveedorRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
     proveedorRepository.deleteById(id);
    }

}
