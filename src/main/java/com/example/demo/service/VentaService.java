package com.example.demo.service;

import com.example.demo.DTO.VentaDTO;
import com.example.demo.IgenericService;
import com.example.demo.entity.Venta;
import com.example.demo.repository.ventaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IgenericService<Venta, Integer> {

    @Autowired
    ventaRepository ventarepository;

    @Override
    public List<Venta> getAll() {
        return ventarepository.findAll();
    }

    @Override
    public Optional<Venta> getById(Integer id) {
        return ventarepository.findById(id);
    }

    @Override
    public Venta save(Venta entity) {
        return ventarepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
       ventarepository.deleteById(id);
    }

    public VentaDTO crearVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        // Asigna los valores del DTO a la entidad
        venta.setNombre(ventaDTO.getNombre());
        venta.setPrecio(ventaDTO.getPrecio());
        venta.setCantidadTickets(ventaDTO.getCantidadTickets());
        venta.calcularTotal(); // Calcula el total

        // Guarda la entidad en el repositorio
        venta = ventarepository.save(venta);

        // Convierte la entidad guardada a DTO
        VentaDTO responseDTO = new VentaDTO();
        responseDTO.setIdVenta(venta.getIdVenta());
        responseDTO.setNombre(venta.getNombre());
        responseDTO.setPrecio(venta.getPrecio());
        responseDTO.setCantidadTickets(venta.getCantidadTickets());
        responseDTO.setTotal(venta.getTotal());
        // Si tienes productos, agrégales aquí

        return responseDTO; // Retorna el DTO con el total calculado
    }
}
