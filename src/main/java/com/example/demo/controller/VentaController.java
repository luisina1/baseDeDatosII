package com.example.demo.controller;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.DTO.VentaDTO;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<VentaDTO> getAll() {
        return ventaService.getAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> getById(@PathVariable("idVenta") Integer id) {
        Optional<Venta> optionalVenta = ventaService.getById(id);

        if (optionalVenta.isPresent()) {
            Venta venta = optionalVenta.get();
            VentaDTO ventaDTO = entityToDto(venta);
            return ResponseEntity.ok(ventaDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<VentaDTO> save(@RequestBody VentaDTO ventaDTO) {
        if (ventaDTO.getNombre() == null || ventaDTO.getPrecio() < 0 || ventaDTO.getCantidadTickets() < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Crear la entidad a partir del DTO
            Venta venta = dtoToEntity(ventaDTO);
            // Calcular el total directamente en la entidad
            venta.calcularTotal(); // Asegúrate de llamar a calcularTotal aquí
            // Guardar la venta en la base de datos
            Venta savedVenta = ventaService.save(venta);
            return ResponseEntity.ok(entityToDto(savedVenta)); // Retorna el DTO guardado
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> update(@PathVariable("idVenta") Integer id, @RequestBody VentaDTO ventaDTO) {
        Optional<Venta> optionalVenta = ventaService.getById(id);

        if (optionalVenta.isPresent()) {
            try {
                Venta ventaExistente = optionalVenta.get();
                ventaExistente.setNombre(ventaDTO.getNombre());
                ventaExistente.setPrecio(ventaDTO.getPrecio());
                ventaExistente.setCantidadTickets(ventaDTO.getCantidadTickets());

                // Asegúrate de recalcular el total después de modificar los valores
                ventaExistente.calcularTotal();

                if (ventaDTO.getProductos() != null) {
                    List<Producto> productos = ventaDTO.getProductos().stream()
                            .map(p -> productoService.getById(p.getIdProducto()).orElse(null))
                            .collect(Collectors.toList());
                    ventaExistente.setProductos(productos);
                }

                Venta updatedVenta = ventaService.save(ventaExistente);
                return ResponseEntity.ok(entityToDto(updatedVenta));
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> delete(@PathVariable("idVenta") Integer id) {
        if (ventaService.getById(id).isPresent()) {
            ventaService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private VentaDTO entityToDto(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setNombre(venta.getNombre());
        dto.setPrecio(venta.getPrecio());
        dto.setCantidadTickets(venta.getCantidadTickets());
        dto.setTotal(venta.getTotal()); // Asegúrate de que el total se copie aquí

        if (venta.getProductos() != null) {
            dto.setProductos(venta.getProductos().stream()
                    .map(this::entityToDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private ProductoDTO entityToDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        return dto;
    }

    private Venta dtoToEntity(VentaDTO dto) {
        Venta venta = new Venta();
        venta.setNombre(dto.getNombre());
        venta.setPrecio(dto.getPrecio());
        venta.setCantidadTickets(dto.getCantidadTickets());

        if (dto.getProductos() != null) {
            List<Producto> productos = dto.getProductos().stream()
                    .map(p -> productoService.getById(p.getIdProducto()).orElse(null)) // Convertir cada ProductoDTO a Producto
                    .collect(Collectors.toList());
            venta.setProductos(productos);
        }

        return venta;
    }
}
