package com.example.demo.service;

import com.example.demo.DTO.EmpleadoDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.Empleado;
import com.example.demo.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService implements IgenericService<Empleado, EmpleadoDTO, Integer> {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> getAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getById(Integer id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado save(Empleado entity) {
        return empleadoRepository.save(entity);
    }

    @Override
    public Empleado update(Integer id, EmpleadoDTO empleadoDTO) {
        Optional<Empleado> empleadoOptional = getById(id);
        if (empleadoOptional.isPresent()) {
            Empleado empleado = empleadoOptional.get();
            empleado.setNombre(empleadoDTO.getNombre());
            empleado.setPuesto(empleadoDTO.getPuesto());
            return save(empleado);
        } else {
            throw new RuntimeException("Empleado no encontrado");
        }
    }

    @Override
    public void delete(Integer id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    public EmpleadoDTO entityToDto(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setNombre(empleado.getNombre());
        dto.setPuesto(empleado.getPuesto());
        return dto;
    }

    @Override
    public Empleado dtoToEntity(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre());
        empleado.setPuesto(dto.getPuesto());
        return empleado;
    }
}
