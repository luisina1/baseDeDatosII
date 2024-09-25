package com.example.demo.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockProveedorDTO {

    private Integer id;
    private Integer entrada;
    private Integer salida;
    private LocalDate fecha;
    private Double cantidadDisponible;
    private Double stockInicial;
    private ProveedorDTO proveedor;
}
