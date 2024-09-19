package com.example.demo.DTO;

import lombok.Data;

@Data
public class ProductoDTO {

    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private ProveedorDTO proveedor;
}
