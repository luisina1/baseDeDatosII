package com.example.demo.controller;

import com.example.demo.DTO.EmpleadoDTO;
import com.example.demo.entity.Empleado;
import com.example.demo.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<EmpleadoDTO> getAll() {
        return empleadoService.getAll().stream()
                .map(empleadoService::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable("idEmpleado") Integer idEmpleado) {
        return empleadoService.getById(idEmpleado)
                .map(empleadoService::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> save(@RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoService.dtoToEntity(empleadoDTO);
        Empleado nuevoEmpleado = empleadoService.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empleadoService.entityToDto(nuevoEmpleado));
    }

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> update(@PathVariable("idEmpleado") Integer idEmpleado, @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            Empleado updatedEmpleado = empleadoService.update(idEmpleado, empleadoDTO);
            return ResponseEntity.ok(empleadoService.entityToDto(updatedEmpleado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> delete(@PathVariable("idEmpleado") Integer idEmpleado) {
        if (empleadoService.getById(idEmpleado).isPresent()) {
            empleadoService.delete(idEmpleado);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
