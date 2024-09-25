package com.example.demo.DTO;

import lombok.Data;
import java.util.List;

@Data
public class VentaDTO {

    private Integer idVenta;
    private String nombre;
    private Double precio;
    private Integer cantidadTickets;
    private Double total;
    private List<ProductoSinProveedorDTO> productos;
}
