package com.example.demo.service;

import com.example.demo.DTO.ProveedorDTO;
import com.example.demo.entity.Proveedor;
import com.example.demo.IgenericService;
import com.example.demo.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService implements IgenericService<Proveedor, ProveedorDTO, Integer> {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> getById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor entity) {
        return proveedorRepository.save(entity);
    }

    public Proveedor update(Integer id, ProveedorDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleEmpleado no encontrado"));

        proveedor.setNombreCompleto(dto.getNombreCompleto());
        proveedor.setContacto(dto.getContacto());
        proveedor.setDireccion(dto.getDireccion());
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void delete(Integer id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public ProveedorDTO entityToDto(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombreCompleto(proveedor.getNombreCompleto());
        dto.setContacto(proveedor.getContacto());
        dto.setDireccion(proveedor.getDireccion());
        return dto;
    }

    @Override
    public Proveedor dtoToEntity(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(dto.getId());
        proveedor.setNombreCompleto(dto.getNombreCompleto());
        proveedor.setContacto(dto.getContacto());
        proveedor.setDireccion(dto.getDireccion());
        return proveedor;
    }
}
