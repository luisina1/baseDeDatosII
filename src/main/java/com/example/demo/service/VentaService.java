package com.example.demo.service;

import com.example.demo.DTO.ProductoSinProveedorDTO;
import com.example.demo.DTO.VentaDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService implements IgenericService<Venta, VentaDTO, Integer> {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> getById(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta save(Venta entity) {
        entity.setTotal(entity.getTotal());
        return ventaRepository.save(entity);
    }

    @Override
    public Venta update(Integer id, VentaDTO dto) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        venta.setNombre(dto.getNombre());
        venta.setPrecio(dto.getPrecio());
        venta.setCantidadTickets(dto.getCantidadTickets());

        if (dto.getProductos() != null) {
            List<Producto> productos = dto.getProductos().stream()
                    .map(productoDTO -> {
                        Producto producto = new Producto();
                        producto.setIdProducto(productoDTO.getIdProducto());
                        producto.setNombre(productoDTO.getNombre());
                        producto.setDescripcion(productoDTO.getDescripcion());
                        producto.setCategoria(producto.getCategoria());
                        return producto;
                    })
                    .collect(Collectors.toList());
            venta.setProductos(productos);
        }
        return ventaRepository.save(venta);
    }

    @Override
    public void delete(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public VentaDTO entityToDto(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setNombre(venta.getNombre());
        dto.setPrecio(venta.getPrecio());
        dto.setCantidadTickets(venta.getCantidadTickets());
        dto.setTotal(venta.getTotal());

        if (venta.getProductos() != null) {
            List<ProductoSinProveedorDTO> productosDTO = venta.getProductos().stream()
                    .map(producto -> {
                        ProductoSinProveedorDTO productoDTO = new ProductoSinProveedorDTO();
                        productoDTO.setIdProducto(producto.getIdProducto());
                        productoDTO.setNombre(producto.getNombre());
                        productoDTO.setDescripcion(producto.getDescripcion());
                        productoDTO.setCategoria(producto.getCategoria());
                        return productoDTO;
                    })
                    .collect(Collectors.toList());
            dto.setProductos(productosDTO);
        }
        return dto;
    }

    @Override
    public Venta dtoToEntity(VentaDTO dto) {
        Venta venta = new Venta();
        venta.setNombre(dto.getNombre());
        venta.setPrecio(dto.getPrecio());
        venta.setCantidadTickets(dto.getCantidadTickets());
        venta.setTotal(dto.getTotal());

        if (dto.getProductos() != null) {
            List<Producto> productos = dto.getProductos().stream()
                    .map(productoDTO -> productoRepository.findById(productoDTO.getIdProducto())
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + productoDTO.getIdProducto())))
                    .collect(Collectors.toList());
            venta.setProductos(productos);
        }
        return venta;
    }
}
