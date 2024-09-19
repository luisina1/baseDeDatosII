package com.example.demo.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovimientoInventarioDTO {

    private Integer idStock;
    private double entrada;
    private double salida;
    private LocalDate fecha;
    private Double cantidadDisponible;
    private ProductoDTO producto;
}
