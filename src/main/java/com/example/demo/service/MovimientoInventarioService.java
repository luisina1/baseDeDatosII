package com.example.demo.service;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.VentaDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.MovimientoInventario;
import com.example.demo.entity.Venta;
import com.example.demo.repository.movimientoInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService implements IgenericService<MovimientoInventario, Integer> {

    @Autowired
    movimientoInRepository movimientoinRepository;

    @Override
    public List<MovimientoInventario> getAll() {
        return movimientoinRepository.findAll();
    }

    @Override
    public Optional<MovimientoInventario> getById(Integer id) {
        return movimientoinRepository.findById(id);
    }

    @Override
    public MovimientoInventario save(MovimientoInventario dto) {

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setEntrada(dto.getEntrada());
        movimiento.setSalida(dto.getSalida());
        movimiento.setFecha(dto.getFecha());
        movimiento.setCantidadDisponible(movimiento.calcularCantidadDisponible());

        return movimientoinRepository.save(movimiento);
    }

    @Override
    public void delete(Integer id) {
        movimientoinRepository.deleteById(id);
    }

}
