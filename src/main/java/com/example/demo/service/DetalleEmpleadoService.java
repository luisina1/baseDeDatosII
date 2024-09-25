package com.example.demo.service;

import com.example.demo.DTO.DetalleEmpleadoDTO;
import com.example.demo.DTO.EmpleadoDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.DetalleEmpleado;
import com.example.demo.entity.Empleado;
import com.example.demo.repository.DetalleEmpleadoRepository;
import com.example.demo.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleEmpleadoService implements IgenericService<DetalleEmpleado, DetalleEmpleadoDTO, Integer> {

    @Autowired
    private DetalleEmpleadoRepository detalleEmpleadoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<DetalleEmpleado> getAll() {
        return detalleEmpleadoRepository.findAll();
    }

    @Override
    public Optional<DetalleEmpleado> getById(Integer id) {
        return detalleEmpleadoRepository.findById(id);
    }

    @Override
    public DetalleEmpleado save(DetalleEmpleado entity) {
        return detalleEmpleadoRepository.save(entity);
    }

    @Override
    public DetalleEmpleado update(Integer id, DetalleEmpleadoDTO dto) {
        DetalleEmpleado detalleEmpleado = detalleEmpleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleEmpleado no encontrado"));

        detalleEmpleado.setDireccion(dto.getDireccion());
        detalleEmpleado.setTelefono(dto.getTelefono());

        if (dto.getEmpleado() != null && dto.getEmpleado().getIdEmpleado() != null) {
            Optional<Empleado> empleadoOptional = empleadoRepository.findById(dto.getEmpleado().getIdEmpleado());
            empleadoOptional.ifPresent(detalleEmpleado::setEmpleado);
        }
        return detalleEmpleadoRepository.save(detalleEmpleado);
    }

    @Override
    public void delete(Integer id) {
        detalleEmpleadoRepository.deleteById(id);
    }

    @Override
    public DetalleEmpleadoDTO entityToDto(DetalleEmpleado detalle) {
        DetalleEmpleadoDTO dto = new DetalleEmpleadoDTO();
        dto.setIdDetalleEmpleado(detalle.getIdDetalleEmpleado());
        dto.setDireccion(detalle.getDireccion());
        dto.setTelefono(detalle.getTelefono());

        if (detalle.getEmpleado() != null) {
            EmpleadoDTO empleadoDTO = new EmpleadoDTO();
            empleadoDTO.setIdEmpleado(detalle.getEmpleado().getIdEmpleado());
            empleadoDTO.setNombre(detalle.getEmpleado().getNombre());
            empleadoDTO.setPuesto(detalle.getEmpleado().getPuesto());
            dto.setEmpleado(empleadoDTO);
        }
        return dto;
    }

    @Override
    public DetalleEmpleado dtoToEntity(DetalleEmpleadoDTO dto) {
        DetalleEmpleado detalle = new DetalleEmpleado();
        detalle.setDireccion(dto.getDireccion());
        detalle.setTelefono(dto.getTelefono());

        if (dto.getEmpleado() != null) {
            Optional<Empleado> empleadoOptional = empleadoRepository.findById(dto.getEmpleado().getIdEmpleado());
            empleadoOptional.ifPresent(detalle::setEmpleado);
        }
        return detalle;
    }
}
