package com.example.demo.controller;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Proveedor;
import com.example.demo.service.ProductoService;
import com.example.demo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class productoController {

    @Autowired
    ProductoService productoService;

    @Autowired
    ProveedorService proveedorService;

    @GetMapping
    public List<ProductoDTO> getAll() {
        return productoService.getAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable("id") Integer id) {
        return productoService.getById(id)
                .map(this::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{idProducto}")
    public void deleteProducto(@PathVariable("idProducto") Integer id) {
        productoService.delete(id);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(@RequestBody ProductoDTO productoDTO) {
        Producto producto = dtoToEntity(productoDTO);
        Producto nuevoProducto = productoService.save(producto);
        return new ResponseEntity<>(entityToDto(nuevoProducto), HttpStatus.CREATED);
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable("idProducto") Integer id, @RequestBody ProductoDTO productoDTO) {
        Optional<Producto> optionalProducto = productoService.getById(id);

        if (optionalProducto.isPresent()) {
            Producto productoExistente = optionalProducto.get();
            productoExistente.setNombre(productoDTO.getNombre());
            productoExistente.setDescripcion(productoDTO.getDescripcion());
            productoExistente.setCategoria(productoDTO.getCategoria());

            // Actualizar proveedor si está presente
            if (productoDTO.getProveedor() != null && productoDTO.getProveedor().getId() != null) {
                Optional<Proveedor> proveedorOpt = proveedorService.getById(productoDTO.getProveedor().getId());
                if (proveedorOpt.isPresent()) {
                    productoExistente.setProveedor(proveedorOpt.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Proveedor no encontrado
                }
            }

            Producto updatedProducto = productoService.save(productoExistente);
            return ResponseEntity.ok(entityToDto(updatedProducto));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private ProductoDTO entityToDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());

        if (producto.getProveedor() != null) {
            ProveedorDTO proveedorDTO = new ProveedorDTO();
            proveedorDTO.setId(producto.getProveedor().getId());
            proveedorDTO.setNombreCompleto(producto.getProveedor().getNombreCompleto());
            proveedorDTO.setContacto(producto.getProveedor().getContacto());
            proveedorDTO.setDireccion(producto.getProveedor().getDireccion());
            // Puedes añadir más campos si es necesario
            dto.setProveedor(proveedorDTO);
        }

        return dto;
    }

    private Producto dtoToEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());

        if (dto.getProveedor() != null) {
            Optional<Proveedor> proveedorOpt = proveedorService.getById(dto.getProveedor().getId());
            proveedorOpt.ifPresent(producto::setProveedor);
        }

        return producto;
    }

}