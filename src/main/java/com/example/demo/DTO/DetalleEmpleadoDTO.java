package com.example.demo.DTO;

import lombok.Data;

@Data
public class DetalleEmpleadoDTO {

    private Integer idDetalleEmpleado;
    private String direccion;
    private String telefono;
    private EmpleadoDTO empleado;
}
