package com.example.demo.controller;

import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.entity.Proveedor;
import com.example.demo.service.ProveedorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<ProveedorDTO> getAll() {
        return proveedorService.getAll().stream()
                .map(proveedorService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable Integer id) {
        return proveedorService.getById(id)
                .map(proveedorService::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> save(@RequestBody ProveedorDTO proveedorDTO) {
        try {
            Proveedor proveedor = proveedorService.dtoToEntity(proveedorDTO);
            Proveedor nuevoProveedor = proveedorService.save(proveedor);
            return new ResponseEntity<>(proveedorService.entityToDto(nuevoProveedor), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> update(@PathVariable Integer id, @RequestBody ProveedorDTO proveedorDTO) {
        try {
            Proveedor updatedProveedor = proveedorService.update(id, proveedorDTO);
            return ResponseEntity.ok(proveedorService.entityToDto(updatedProveedor));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (proveedorService.getById(id).isPresent()) {
            proveedorService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
