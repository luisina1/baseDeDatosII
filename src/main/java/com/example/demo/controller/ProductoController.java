package com.example.demo.controller;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.service.ProveedorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<ProductoDTO> getAll() {
        return productoService.getAll().stream()
                .map(productoService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable("idProducto") Integer idProducto) {
        return productoService.getById(idProducto)
                .map(productoService::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(@RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = productoService.dtoToEntity(productoDTO);
            Producto savedProducto = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productoService.entityToDto(savedProducto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<ProductoDTO> update(@PathVariable("idProducto") Integer idProducto, @RequestBody ProductoDTO productoDTO) {
        try {
            Producto updatedProducto = productoService.update(idProducto, productoDTO);
            return ResponseEntity.ok(productoService.entityToDto(updatedProducto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> delete(@PathVariable("idProducto") Integer idProducto) {
        if (productoService.getById(idProducto).isPresent()) {
            productoService.delete(idProducto);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
