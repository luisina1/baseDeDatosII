package com.example.demo.controller;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.ProductoDTO;
import com.example.demo.entity.MovimientoInventario;
import com.example.demo.entity.Producto;
import com.example.demo.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventarioProductos")
public class MovimientoInventarioController {

    @Autowired
    MovimientoInventarioService movimientoInventarioService;

    @GetMapping
    public List<MovimientoInventarioDTO> getAll() {
        return movimientoInventarioService.getAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idStock}")
    public ResponseEntity<MovimientoInventarioDTO> getById(@PathVariable("idStock") int idStock) {
        Optional<MovimientoInventario> movimientoInventario = movimientoInventarioService.getById(idStock);
        return movimientoInventario
                .map(this::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    public ResponseEntity<MovimientoInventarioDTO> save(@RequestBody MovimientoInventarioDTO stockDTO) {
        if (stockDTO.getEntrada() < 0 || stockDTO.getSalida() < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            MovimientoInventario stock = dtoToEntity(stockDTO);
            MovimientoInventario savedStock = movimientoInventarioService.save(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityToDto(savedStock));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{idStock}")
    public ResponseEntity<MovimientoInventarioDTO> update(@PathVariable("idStock") Integer id, @RequestBody MovimientoInventarioDTO stockDTO) {
        Optional<MovimientoInventario> stockOptional = movimientoInventarioService.getById(id);
        if (stockOptional.isPresent()) {
            try {
                MovimientoInventario stock = stockOptional.get();
                stock.setEntrada(stockDTO.getEntrada());
                stock.setSalida(stockDTO.getSalida());
                stock.setFecha(stockDTO.getFecha());

                // Actualizar el producto si es necesario
                if (stockDTO.getProducto() != null) {
                    Producto producto = new Producto();
                    producto.setIdProducto(stockDTO.getProducto().getIdProducto());
                    stock.setProducto(producto);
                }

                // Guardar el movimiento actualizado
                MovimientoInventario updatedStock = movimientoInventarioService.save(stock);
                return ResponseEntity.ok(entityToDto(updatedStock));
            } catch (DataIntegrityViolationException e) { // Reemplaza con la excepción específica que esperas
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{idStock}")
    public ResponseEntity<Void> deleteStock(@PathVariable("idStock") Integer idStock) {
        if (movimientoInventarioService.getById(idStock).isPresent()) {
            movimientoInventarioService.delete(idStock);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private MovimientoInventarioDTO entityToDto(MovimientoInventario movimientoInventario) {
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setIdStock(movimientoInventario.getIdStock());
        dto.setEntrada(movimientoInventario.getEntrada());
        dto.setSalida(movimientoInventario.getSalida());
        dto.setFecha(movimientoInventario.getFecha());
        dto.setProducto(movimientoInventario.getProducto() != null ? productoEntityToDto(movimientoInventario.getProducto()) : null);
        dto.setCantidadDisponible(movimientoInventario.getEntrada() - movimientoInventario.getSalida()); // Calcular cantidad disponible
        return dto;
    }

    private MovimientoInventario dtoToEntity(MovimientoInventarioDTO dto) {
        MovimientoInventario movimientoInventario = new MovimientoInventario();
        movimientoInventario.setEntrada(dto.getEntrada());
        movimientoInventario.setSalida(dto.getSalida());
        movimientoInventario.setFecha(dto.getFecha());

        if (dto.getProducto() != null && dto.getProducto().getIdProducto() != null) {
            Producto producto = new Producto();
            producto.setIdProducto(dto.getProducto().getIdProducto());
            movimientoInventario.setProducto(producto);
        }
        return movimientoInventario;
    }

    private ProductoDTO productoEntityToDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        return dto;
    }
}
