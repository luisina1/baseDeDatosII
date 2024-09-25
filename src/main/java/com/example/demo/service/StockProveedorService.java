package com.example.demo.service;

import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.DTO.StockProveedorDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.Proveedor;
import com.example.demo.entity.StockProveedor;
import com.example.demo.repository.StockProveedorRepository;
import com.example.demo.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StockProveedorService implements IgenericService<StockProveedor, StockProveedorDTO, Integer> {

    @Autowired
    private StockProveedorRepository stockProveedorRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<StockProveedor> getAll() {
        return stockProveedorRepository.findAll();
    }

    @Override
    public Optional<StockProveedor> getById(Integer id) {
        return stockProveedorRepository.findById(id);
    }

    @Override
    public StockProveedor save(StockProveedor entity) {
        entity.setCantidadDisponible(entity.calcularCantidadDisponible());
        return stockProveedorRepository.save(entity);
    }

    @Override
    public StockProveedor update(Integer id, StockProveedorDTO stockProveedorDTO) {
        Optional<StockProveedor> optionalStockProveedor = stockProveedorRepository.findById(id);

        if (!optionalStockProveedor.isPresent()) {
            return null;
        }

        StockProveedor stockProveedor = optionalStockProveedor.get();
        stockProveedor.setEntrada(stockProveedorDTO.getEntrada());
        stockProveedor.setSalida(stockProveedorDTO.getSalida());
        stockProveedor.setFecha(stockProveedorDTO.getFecha());
        stockProveedor.setStockInicial(stockProveedorDTO.getStockInicial());

        if (stockProveedorDTO.getProveedor() != null && stockProveedorDTO.getProveedor().getId() != null) {
            Optional<Proveedor> proveedorOptional = proveedorRepository.findById(stockProveedorDTO.getProveedor().getId());
            proveedorOptional.ifPresent(stockProveedor::setProveedor);
        }
        return stockProveedorRepository.save(stockProveedor);
    }

    @Override
    public void delete(Integer id) {
        stockProveedorRepository.deleteById(id);
    }

    @Override
    public StockProveedorDTO entityToDto(StockProveedor stockProveedor) {
        StockProveedorDTO dto = new StockProveedorDTO();
        dto.setId(stockProveedor.getId());
        dto.setEntrada(stockProveedor.getEntrada());
        dto.setSalida(stockProveedor.getSalida());
        dto.setFecha(stockProveedor.getFecha());
        dto.setStockInicial(stockProveedor.getStockInicial());
        dto.setCantidadDisponible(stockProveedor.getCantidadDisponible());

        if (stockProveedor.getProveedor() != null) {
            dto.setProveedor(proveedorEntityToDto(stockProveedor.getProveedor()));
        }
        return dto;
    }

    @Override
    public StockProveedor dtoToEntity(StockProveedorDTO dto) {
        StockProveedor stockProveedor = new StockProveedor();
        stockProveedor.setId(dto.getId());
        stockProveedor.setEntrada(dto.getEntrada());
        stockProveedor.setSalida(dto.getSalida());
        stockProveedor.setFecha(dto.getFecha());
        stockProveedor.setStockInicial(dto.getStockInicial());

        if (dto.getProveedor() != null && dto.getProveedor().getId() != null) {
            Optional<Proveedor> proveedorOptional = proveedorRepository.findById(dto.getProveedor().getId());
            proveedorOptional.ifPresent(stockProveedor::setProveedor);
        }
        return stockProveedor;
    }

    public ProveedorDTO proveedorEntityToDto(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombreCompleto(proveedor.getNombreCompleto());
        dto.setContacto(proveedor.getContacto());
        dto.setDireccion(proveedor.getDireccion());
        return dto;
    }
}
