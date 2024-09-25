package com.example.demo.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MovimientoInventarioDTO {

    private Integer idStock;
    private Double entrada;
    private Double salida;
    private LocalDate fecha;
    private Double stockInicial;
    private Double cantidadDisponible;
    private ProductoSinProveedorDTO producto;
}
