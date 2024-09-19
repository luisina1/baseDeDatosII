package com.example.demo.controller;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.DTO.StockProveedorDTO;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Proveedor;
import com.example.demo.entity.StockProveedor;
import com.example.demo.service.ProveedorService;
import com.example.demo.service.StockProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    ProveedorService proveedorService;

    @Autowired
    StockProveedorService stockProveedorService;

    @GetMapping
    public List<ProveedorDTO> getAll() {
        return proveedorService.getAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idProveedor}")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable("idProveedor") Integer id) {
        return proveedorService.getById(id)
                .map(this::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> save(@RequestBody ProveedorDTO proveedorDto) {
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setNombreCompleto(proveedorDto.getNombreCompleto());
            proveedor.setContacto(proveedorDto.getContacto());
            proveedor.setDireccion(proveedorDto.getDireccion());

            if (proveedorDto.getStockProveedor() != null) {
                StockProveedor stockProveedor = new StockProveedor();
                stockProveedor.setEntrada(proveedorDto.getStockProveedor().getEntrada());
                stockProveedor.setSalida(proveedorDto.getStockProveedor().getSalida());
                stockProveedor.setFecha(proveedorDto.getStockProveedor().getFecha());

                // Guarda el StockProveedor antes de asociarlo al Proveedor
                StockProveedor savedStockProveedor = stockProveedorService.save(stockProveedor);
                proveedor.setStockProveedor(savedStockProveedor);
                savedStockProveedor.setProveedor(proveedor); // Establece la relación bidireccional
            }

            Proveedor savedProveedor = proveedorService.save(proveedor);
            return ResponseEntity.ok(entityToDto(savedProveedor));
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idProveedor}")
    public ResponseEntity<ProveedorDTO> update(@PathVariable("idProveedor") Integer id, @RequestBody ProveedorDTO proveedorDto) {
        Optional<Proveedor> optionalProveedor = proveedorService.getById(id);

        if (optionalProveedor.isPresent()) {
            try {
                Proveedor proveedorExistente = optionalProveedor.get();
                // Actualizar campos del proveedor
                proveedorExistente.setNombreCompleto(proveedorDto.getNombreCompleto());
                proveedorExistente.setContacto(proveedorDto.getContacto());
                proveedorExistente.setDireccion(proveedorDto.getDireccion());

                // Actualizar StockProveedor si se proporciona
                if (proveedorDto.getStockProveedor() != null) {
                    StockProveedor stockProveedorExistente = proveedorExistente.getStockProveedor();
                    if (stockProveedorExistente == null) {
                        stockProveedorExistente = new StockProveedor();
                    }
                    stockProveedorExistente.setEntrada(proveedorDto.getStockProveedor().getEntrada());
                    stockProveedorExistente.setSalida(proveedorDto.getStockProveedor().getSalida());
                    stockProveedorExistente.setFecha(proveedorDto.getStockProveedor().getFecha());
                    stockProveedorExistente.setProveedor(proveedorExistente);  // Configurar la relación bidireccional

                    proveedorExistente.setStockProveedor(stockProveedorExistente);
                }

                Proveedor updatedProveedor = proveedorService.save(proveedorExistente);
                return ResponseEntity.ok(entityToDto(updatedProveedor));
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<Void> delete(@PathVariable("idProveedor") Integer id) {
        if (proveedorService.getById(id).isPresent()) {
            proveedorService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private ProveedorDTO entityToDto(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombreCompleto(proveedor.getNombreCompleto());
        dto.setContacto(proveedor.getContacto());
        dto.setDireccion(proveedor.getDireccion());

        if (proveedor.getProductos() != null) {
            dto.setProductos(proveedor.getProductos().stream()
                    .map(this::entityToDto)  // Usa un método para convertir Producto a ProductoDTO
                    .collect(Collectors.toList()));
        }

        if (proveedor.getStockProveedor() != null) {
            dto.setStockProveedor(entityToDto(proveedor.getStockProveedor()));  // Usa un método para convertir StockProveedor a StockProveedorDTO
        }

        return dto;
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
            dto.setProveedor(proveedorDTO);
        }

        return dto;
    }

    private StockProveedorDTO entityToDto(StockProveedor stockProveedor) {
        StockProveedorDTO dto = new StockProveedorDTO();
        dto.setId(stockProveedor.getId());
        dto.setEntrada(stockProveedor.getEntrada());
        dto.setSalida(stockProveedor.getSalida());
        dto.setFecha(stockProveedor.getFecha());
        return dto;
    }
}
