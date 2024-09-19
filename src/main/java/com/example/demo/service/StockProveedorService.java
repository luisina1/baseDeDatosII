package com.example.demo.service;

import com.example.demo.IgenericService;
import com.example.demo.entity.StockProveedor;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.StockProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockProveedorService implements IgenericService<StockProveedor,Integer> {

    @Autowired
    StockProveedorRepository stockProveedorRepository;

    @Override
    public List<StockProveedor> getAll() {
        return stockProveedorRepository.findAll();
    }

    @Override
    public Optional<StockProveedor> getById(Integer id) {
        return stockProveedorRepository.findById(id);
    }

    @Override
    public StockProveedor save(StockProveedor entity) {
        entity.calcularCantidadDisponible();
        return stockProveedorRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        stockProveedorRepository.deleteById(id);
    }

    public StockProveedor update(Integer id, StockProveedor updatedEntity) {
        Optional<StockProveedor> optionalStock = stockProveedorRepository.findById(id);
        if (optionalStock.isPresent()) {
            StockProveedor existingStock = optionalStock.get();
            existingStock.setEntrada(updatedEntity.getEntrada());
            existingStock.setSalida(updatedEntity.getSalida());
            existingStock.setFecha(updatedEntity.getFecha());
            existingStock.setProveedor(updatedEntity.getProveedor());
            existingStock.calcularCantidadDisponible(); // Recalcular cantidad disponible
            return stockProveedorRepository.save(existingStock);
        }
        return null; // O lanza una excepción si no se encuentra
    }

}
