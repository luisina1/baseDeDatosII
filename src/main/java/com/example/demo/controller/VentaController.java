package com.example.demo.controller;

import com.example.demo.DTO.VentaDTO;
import com.example.demo.entity.Venta;
import com.example.demo.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<VentaDTO> getAll() {
        return ventaService.getAll().stream()
                .map(ventaService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> getById(@PathVariable("idVenta") Integer idVenta) {
        return ventaService.getById(idVenta)
                .map(ventaService::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<VentaDTO> save(@RequestBody VentaDTO ventaDTO) {
        Venta venta = ventaService.dtoToEntity(ventaDTO);
        venta.calcularTotal();
        Venta savedVenta = ventaService.save(venta);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.entityToDto(savedVenta));
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> update(@PathVariable("idVenta") Integer id, @RequestBody VentaDTO ventaDTO) {
        Venta updatedVenta = ventaService.update(id, ventaDTO);
        return ResponseEntity.ok(ventaService.entityToDto(updatedVenta));
    }

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> delete(@PathVariable("idVenta") Integer idVenta) {
        if (ventaService.getById(idVenta).isPresent()) {
            ventaService.delete(idVenta);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
