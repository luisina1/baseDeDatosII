package com.example.demo.service;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.ProductoSinProveedorDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.MovimientoInventario;
import com.example.demo.entity.Producto;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.movimientoInRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService implements IgenericService<MovimientoInventario, MovimientoInventarioDTO, Integer> {

    @Autowired
    private movimientoInRepository movimientoinRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<MovimientoInventario> getAll() {
        return movimientoinRepository.findAll();
    }

    @Override
    public Optional<MovimientoInventario> getById(Integer id) {
        return movimientoinRepository.findById(id);
    }

    @Override
    public MovimientoInventario save(MovimientoInventario entity) {
        entity.setCantidadDisponible(entity.calcularCantidadDisponible());
        return movimientoinRepository.save(entity);
    }

    @Override
    public MovimientoInventario update(Integer id, MovimientoInventarioDTO dto) {
        Optional<MovimientoInventario> optionalMovimiento = getById(id);
        if (!optionalMovimiento.isPresent()) {
            throw new EntityNotFoundException("Movimiento de inventario no encontrado con ID: " + id);
        }
        MovimientoInventario movimientoInventario = optionalMovimiento.get();
        movimientoInventario.setEntrada(dto.getEntrada());
        movimientoInventario.setSalida(dto.getSalida());
        movimientoInventario.setFecha(dto.getFecha());
        if (dto.getProducto() != null && dto.getProducto().getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getProducto().getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + dto.getProducto().getIdProducto()));
            movimientoInventario.setProducto(producto);
        }
        return save(movimientoInventario);
    }

    @Override
    public void delete(Integer id) {
        movimientoinRepository.deleteById(id);
    }

    @Override
    public MovimientoInventarioDTO entityToDto(MovimientoInventario movimientoInventario) {
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setIdStock(movimientoInventario.getIdStock());
        dto.setEntrada(movimientoInventario.getEntrada());
        dto.setSalida(movimientoInventario.getSalida());
        dto.setFecha(movimientoInventario.getFecha());
        dto.setStockInicial(movimientoInventario.getStockInicial());
        dto.setCantidadDisponible(movimientoInventario.calcularCantidadDisponible());
        if (movimientoInventario.getProducto() != null) {
            dto.setProducto(convertToProductoSinProveedorDTO(movimientoInventario.getProducto()));
        }
        return dto;
    }

    @Override
    public MovimientoInventario dtoToEntity(MovimientoInventarioDTO dto) {
        MovimientoInventario movimientoInventario = new MovimientoInventario();
        movimientoInventario.setEntrada(dto.getEntrada());
        movimientoInventario.setSalida(dto.getSalida());
        movimientoInventario.setFecha(dto.getFecha());
        movimientoInventario.setStockInicial(dto.getStockInicial());

        if (dto.getProducto() != null && dto.getProducto().getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getProducto().getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + dto.getProducto().getIdProducto()));
            movimientoInventario.setProducto(producto);
        }
        return movimientoInventario;
    }

    private ProductoSinProveedorDTO convertToProductoSinProveedorDTO(Producto producto) {
        ProductoSinProveedorDTO dto = new ProductoSinProveedorDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());
        return dto;
    }
}
