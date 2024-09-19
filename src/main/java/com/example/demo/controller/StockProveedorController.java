package com.example.demo.controller;

import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.DTO.StockProveedorDTO;
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
@RequestMapping("/inventarioProveedor")
public class StockProveedorController {

    @Autowired
    private StockProveedorService stockProveedorService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<StockProveedorDTO> getAll() {
        return stockProveedorService.getAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idProveedorStock}")
    public ResponseEntity<StockProveedorDTO> getById(@PathVariable("idProveedorStock") Integer id) {
        return stockProveedorService.getById(id)
                .map(this::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<StockProveedorDTO> save(@RequestBody StockProveedorDTO stockProveedorDto) {
        try {
            StockProveedor stockProveedor = new StockProveedor();
            stockProveedor.setEntrada(stockProveedorDto.getEntrada());
            stockProveedor.setSalida(stockProveedorDto.getSalida());
            stockProveedor.setFecha(stockProveedorDto.getFecha());

            // Calcular cantidad disponible
            stockProveedor.calcularCantidadDisponible();

            // Asociar el Proveedor si se proporciona
            if (stockProveedorDto.getProveedor() != null) {
                Integer proveedorId = stockProveedorDto.getProveedor().getId();
                Optional<Proveedor> proveedorOptional = proveedorService.getById(proveedorId);
                if (proveedorOptional.isPresent()) {
                    stockProveedor.setProveedor(proveedorOptional.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Proveedor no encontrado
                }
            }

            StockProveedor savedStockProveedor = stockProveedorService.save(stockProveedor);
            return ResponseEntity.ok(entityToDto(savedStockProveedor));
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idProveedorStock}")
    public ResponseEntity<StockProveedorDTO> update(@PathVariable("idProveedorStock") Integer id, @RequestBody StockProveedorDTO stockProveedorDto) {
        Optional<StockProveedor> stockProveedorOptional = stockProveedorService.getById(id);
        if (stockProveedorOptional.isPresent()) {
            try {
                StockProveedor stockProveedorExistente = stockProveedorOptional.get();
                stockProveedorExistente.setEntrada(stockProveedorDto.getEntrada());
                stockProveedorExistente.setSalida(stockProveedorDto.getSalida());
                stockProveedorExistente.setFecha(stockProveedorDto.getFecha());

                // Calcular cantidad disponible
                stockProveedorExistente.calcularCantidadDisponible();

                // Actualizar proveedor si se proporciona
                if (stockProveedorDto.getProveedor() != null) {
                    Integer proveedorId = stockProveedorDto.getProveedor().getId();
                    Optional<Proveedor> proveedorOptional = proveedorService.getById(proveedorId);
                    if (proveedorOptional.isPresent()) {
                        stockProveedorExistente.setProveedor(proveedorOptional.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Proveedor no encontrado
                    }
                }

                StockProveedor updatedStockProveedor = stockProveedorService.save(stockProveedorExistente);
                return ResponseEntity.ok(entityToDto(updatedStockProveedor));
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/{idProveedorStock}")
    public ResponseEntity<Void> delete(@PathVariable("idProveedorStock") Integer id) {
        if (stockProveedorService.getById(id).isPresent()) {
            stockProveedorService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private StockProveedorDTO entityToDto(StockProveedor stockProveedor) {
        StockProveedorDTO dto = new StockProveedorDTO();
        dto.setId(stockProveedor.getId());
        dto.setEntrada(stockProveedor.getEntrada());
        dto.setSalida(stockProveedor.getSalida());
        dto.setFecha(stockProveedor.getFecha());
        dto.setCantidadDisponible(stockProveedor.getCantidadDisponible()); // Agrega esta línea

        // Asumir que StockProveedorDTO tiene una propiedad para el proveedor
        if (stockProveedor.getProveedor() != null) {
            ProveedorDTO proveedorDTO = new ProveedorDTO();
            proveedorDTO.setId(stockProveedor.getProveedor().getId());
            proveedorDTO.setNombreCompleto(stockProveedor.getProveedor().getNombreCompleto());
            proveedorDTO.setContacto(stockProveedor.getProveedor().getContacto());
            proveedorDTO.setDireccion(stockProveedor.getProveedor().getDireccion());
            dto.setProveedor(proveedorDTO);
        }
        return dto;
    }

}
