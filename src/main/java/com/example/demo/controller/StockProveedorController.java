package com.example.demo.controller;

import com.example.demo.DTO.StockProveedorDTO;
import com.example.demo.entity.StockProveedor;
import com.example.demo.service.ProveedorService;
import com.example.demo.service.StockProveedorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stockProveedores")
public class StockProveedorController {

    @Autowired
    private StockProveedorService stockProveedorService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<StockProveedorDTO>> getAll() {
        List<StockProveedor> stockProveedores = stockProveedorService.getAll();
        List<StockProveedorDTO> stockProveedorDTOs = stockProveedores.stream()
                .map(stockProveedorService::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stockProveedorDTOs);
    }

    @GetMapping("/{idStock}")
    public ResponseEntity<StockProveedorDTO> getById(@PathVariable Integer idStock) {
        return stockProveedorService.getById(idStock)
                .map(stockProveedor -> ResponseEntity.ok(stockProveedorService.entityToDto(stockProveedor)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<StockProveedorDTO> create(@RequestBody StockProveedorDTO stockProveedorDTO) {
        try {
            StockProveedor stockProveedor = stockProveedorService.dtoToEntity(stockProveedorDTO);
            StockProveedor savedStockProveedor = stockProveedorService.save(stockProveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(stockProveedorService.entityToDto(savedStockProveedor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idStock}")
    public ResponseEntity<StockProveedorDTO> update(@PathVariable Integer idStock, @RequestBody StockProveedorDTO stockProveedorDTO) {
        try {
            StockProveedor updatedStockProveedor = stockProveedorService.update(idStock, stockProveedorDTO);
            return ResponseEntity.ok(stockProveedorService.entityToDto(updatedStockProveedor));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{idStock}")
    public ResponseEntity<Void> delete(@PathVariable Integer idStock) {
        if (stockProveedorService.getById(idStock).isPresent()) {
            stockProveedorService.delete(idStock);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
