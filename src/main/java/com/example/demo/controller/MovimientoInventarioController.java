package com.example.demo.controller;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.entity.DetalleEmpleado;
import com.example.demo.entity.MovimientoInventario;
import com.example.demo.service.MovimientoInventarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventarioProductos")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @GetMapping
    public List<MovimientoInventarioDTO> getAll() {
        return movimientoInventarioService.getAll().stream()
                .map(movimientoInventarioService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idStock}")
    public ResponseEntity<MovimientoInventarioDTO> getById(@PathVariable("idStock") int idStock) {
        return movimientoInventarioService.getById(idStock)
                .map(movimientoInventarioService::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<MovimientoInventarioDTO> save(@RequestBody MovimientoInventarioDTO movimientoInventarioDTO) {
        try {
            MovimientoInventario movimientoInventario = movimientoInventarioService.dtoToEntity(movimientoInventarioDTO);
            MovimientoInventario savedMovimiento = movimientoInventarioService.save(movimientoInventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(movimientoInventarioService.entityToDto(savedMovimiento));
        }catch(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idStock}")
    public ResponseEntity<MovimientoInventarioDTO> update(@PathVariable("idStock") Integer idStock, @RequestBody MovimientoInventarioDTO stockDTO) {
        MovimientoInventario updatedMovimiento = movimientoInventarioService.update(idStock, stockDTO);
        return ResponseEntity.ok(movimientoInventarioService.entityToDto(updatedMovimiento));
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
}
