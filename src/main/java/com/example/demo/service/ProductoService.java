package com.example.demo.service;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Proveedor;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.ProveedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IgenericService<Producto, ProductoDTO, Integer> {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getById(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto entity) {
        return productoRepository.save(entity);
    }

    @Override
    public Producto update(Integer id, ProductoDTO dto) {
        Optional<Producto> optionalProducto = getById(id);
        if (!optionalProducto.isPresent()) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        Producto producto = optionalProducto.get();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());

        if (dto.getProveedor() != null && dto.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + dto.getProveedor().getId()));
            producto.setProveedor(proveedor);
        }
        return save(producto);
    }

    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoDTO entityToDto(Producto producto) {
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

    @Override
    public Producto dtoToEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());

        if (dto.getProveedor() != null && dto.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + dto.getProveedor().getId()));
            producto.setProveedor(proveedor);
        }
        return producto;
    }
}
