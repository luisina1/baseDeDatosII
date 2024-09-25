package com.example.demo.controller;

import com.example.demo.DTO.DetalleEmpleadoDTO;
import com.example.demo.entity.DetalleEmpleado;
import com.example.demo.service.DetalleEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/detalleEmpleados")
public class DetalleEmpleadoController {

    @Autowired
    private DetalleEmpleadoService detalleEmpleadoService;

    @GetMapping
    public List<DetalleEmpleadoDTO> getAll() {
        return detalleEmpleadoService.getAll().stream()
                .map(detalleEmpleadoService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idDetalleEmpleado}")
    public ResponseEntity<DetalleEmpleadoDTO> getById(@PathVariable("idDetalleEmpleado") int idDetalleEmpleado) {
        return detalleEmpleadoService.getById(idDetalleEmpleado)
                .map(detalleEmpleadoService::entityToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<DetalleEmpleadoDTO> save(@RequestBody DetalleEmpleadoDTO detalleEmpleadoDTO) {
        DetalleEmpleado detalleEmpleado = detalleEmpleadoService.dtoToEntity(detalleEmpleadoDTO);
        DetalleEmpleado savedDetalle = detalleEmpleadoService.save(detalleEmpleado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalleEmpleadoService.entityToDto(savedDetalle));
    }

    @PutMapping("/{idDetalleEmpleado}")
    public ResponseEntity<DetalleEmpleadoDTO> update(@PathVariable("idDetalleEmpleado") Integer id, @RequestBody DetalleEmpleadoDTO detalleEmpleadoDTO) {
        DetalleEmpleado updatedDetalle = detalleEmpleadoService.update(id, detalleEmpleadoDTO);
        return ResponseEntity.ok(detalleEmpleadoService.entityToDto(updatedDetalle));
    }

    @DeleteMapping("/{idDetalleEmpleado}")
    public ResponseEntity<Void> delete(@PathVariable("idDetalleEmpleado") Integer idDetalleEmpleado) {
        if (detalleEmpleadoService.getById(idDetalleEmpleado).isPresent()) {
            detalleEmpleadoService.delete(idDetalleEmpleado);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
