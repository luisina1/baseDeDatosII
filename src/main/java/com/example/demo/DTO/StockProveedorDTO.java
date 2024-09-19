package com.example.demo.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockProveedorDTO {
    private Integer id;
    private int entrada;
    private int salida;
    private LocalDate fecha;
    private double cantidadDisponible;
    private ProductoDTO producto;
    private ProveedorDTO proveedor;
}
